package com.futechsoft.framework.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.exception.ErrorCode;
import com.futechsoft.framework.exception.FileDownloadException;
import com.futechsoft.framework.exception.FileUploadException;
import com.futechsoft.framework.file.service.FileUploadService;
import com.futechsoft.framework.file.vo.FileInfoVo;
import com.futechsoft.framework.util.CommonUtil;
import com.futechsoft.framework.util.FileUtil;
import com.futechsoft.framework.util.FtMap;
import com.google.gson.Gson;

@Controller
public class FileUploadController extends AbstractController {

	@Resource(name = "framework.file.service.FileUploadService")
	FileUploadService fileUploadService;

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	@RequestMapping(value = "/file/upload")
	@ResponseBody
	public Map<String, Object> upload(Model model, @RequestParam MultipartFile file, @RequestParam String metadata)
			throws JsonMappingException, JsonProcessingException, FileUploadException {

		System.out.println("file upload..............................");

		String[] acceptDocs = propertiesConfiguration.getStringArray("file.upload.accept.doc");
		String[] acceptImages = propertiesConfiguration.getStringArray("file.upload.accept.image");
		String[] acceptMultimedias = propertiesConfiguration.getStringArray("file.upload.accept.multimedia");

		String acceptDoc = "";
		String acceptImage = "";
		String acceptMultimedia = "";
		for (String accept : acceptDocs) {
			acceptDoc += accept + ",";
		}
		for (String accept : acceptImages) {
			acceptImage += accept + ",";
		}
		for (String accept : acceptMultimedias) {
			acceptMultimedia += accept + ",";
		}

		Long uploadSize = propertiesConfiguration.getLong("file.upload.size");
		Long chunkSize = propertiesConfiguration.getLong("file.upload.chunkSize");

		Gson gson = new Gson();
		FileInfoVo fileInfoVo = gson.fromJson(metadata, FileInfoVo.class);

		Map<String, Object> fileInfo = new HashMap<String, Object>();

		String originFilename = fileInfoVo.getFileNm();

		String extName = "";
		if (originFilename.lastIndexOf(".") != -1) {
			extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());
		}

		if ((acceptDoc + "," + acceptImage + "," + acceptMultimedia).toUpperCase().indexOf(extName.toUpperCase()) == -1) {
			fileInfo.put("errorCode", ErrorCode.FILE_ACCEPT_ERROR.getCode());
			fileInfo.put("errorMessage",
					StringUtils.replace(ErrorCode.FILE_ACCEPT_ERROR.getMessage(), "{{fileExt}}", extName));
		}

		if (fileInfoVo.getFileSize() > uploadSize || file.getSize() > uploadSize || file.getSize() > chunkSize) {
			fileInfo.put("errorCode", ErrorCode.FILE_SIZE_ERROR.getCode());
			fileInfo.put("errorMessage", StringUtils.replace(ErrorCode.FILE_SIZE_ERROR.getMessage(), "{{fileSize}}",
					(uploadSize / 1024 / 1024) + "M"));

		} else {
			fileInfoVo = fileUploadService.upload(file, fileInfoVo);
			List<FileInfoVo> fileList = new ArrayList<FileInfoVo>();
			fileList.add(fileInfoVo);
			fileInfo.put("fileInfo", fileList);
		}

		return fileInfo;
	}

	@RequestMapping(value = "/file/fileList")
	@ResponseBody
	public Map<String, List<FtMap>> fileList(@RequestParam String docId) throws Exception {
		
		FtMap params = new FtMap();
		params.put("docId", docId);
		
		List<FtMap> fileList = fileUploadService.selectFileList(params);
		Map<String, List<FtMap>> fileInfo = new HashMap<String, List<FtMap>>();
		fileInfo.put("fileInfo", fileList);
		return fileInfo;

	}

	
	

	
	@RequestMapping(value = "/file/deleteFile")
	@ResponseBody
	public Map<String, List<FileInfoVo>> deleteFile(@RequestBody FileInfoVo[] fileInfoVos) throws Exception {


		String serviceType = propertiesConfiguration.getString("service.type");
		
		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");
		
		if(serviceType.equals("dev")) {
			 tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			 realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}
		
		
		/*
		 * Gson gson = new Gson();
		 * FileInfoVo[] fileInfoVos = gson.fromJson(delFileInfo, FileInfoVo[].class);
		 */
		List<FileInfoVo> fileList = new ArrayList<FileInfoVo>();

		for (FileInfoVo fileInfoVo : fileInfoVos) {

			File delFile = Paths.get(tempUploadPath, fileInfoVo.getFileId() + ".TEMP").toFile();

			if (delFile.exists()) {
				delFile.delete();
			}

			fileInfoVo.setDelYn("Y");
			fileList.add(fileInfoVo);

			// 실제파일삭제
			if (!(StringUtils.defaultString(fileInfoVo.getTemp()).equals("Y")) && fileInfoVo.getDelYn().equals("Y")) {

				FtMap params = new FtMap();
				params.put("fileId", fileInfoVo.getFileId());

				FileInfoVo realFileInfoVo = fileUploadService.getFileInfo(params);

				// 파일삭제권한
				boolean deleteFileCheck = true;
				deleteFileCheck = FileUtil.hasFileDeleteAuth(fileInfoVo, realFileInfoVo);

				delFile = Paths.get(realUploadPath, realFileInfoVo.getFilePath(), realFileInfoVo.getFileId() + ".FILE")
						.toFile();

				if (delFile.exists() && deleteFileCheck) {
					delFile.delete();
				}
				if (deleteFileCheck) {
					fileUploadService.deleteFile(params);
				}

			}

		}

		Map<String, List<FileInfoVo>> fileInfo = new HashMap<String, List<FileInfoVo>>();
		fileInfo.put("fileInfo", fileList);

		return fileInfo;
	}

	@RequestMapping(value = "/file/uploadForm")
	public String uploadForm(@RequestParam(required = false, defaultValue = "") String acceptType, HttpServletRequest req)
			throws Exception {

		setuploadForm(acceptType, req);
		return "framework/file/upload";
	}

	/**
	 * 업로드 폼 세팅
	 * 
	 * @param acceptType
	 * @param req
	 * @throws Exception
	 */
	private void setuploadForm(String acceptType, HttpServletRequest req) throws Exception {

		String[] acceptDocs = propertiesConfiguration.getStringArray("file.upload.accept.doc");
		String[] acceptImages = propertiesConfiguration.getStringArray("file.upload.accept.image");
		String[] acceptMultimedias = propertiesConfiguration.getStringArray("file.upload.accept.multimedia");

		String acceptDoc = "";
		String acceptImage = "";
		String acceptMultimedia = "";
		for (String accept : acceptDocs) {
			acceptDoc += accept + ",";
		}
		for (String accept : acceptImages) {
			acceptImage += accept + ",";
		}
		for (String accept : acceptMultimedias) {
			acceptMultimedia += accept + ",";
		}

		String uploadFormId = FileUtil.getRandomId();

		req.setAttribute("uploadFormId", uploadFormId);

		if (acceptType.equals("doc")) {
			req.setAttribute("accept", acceptDoc);

		} else if (acceptType.equals("image")) {
			req.setAttribute("accept", acceptImage);

		} else if (acceptType.equals("multimedia")) {
			req.setAttribute("accept", acceptMultimedia);

		} else {
			req.setAttribute("accept", acceptDoc + "," + acceptImage + "," + acceptMultimedia);
		}
	}

	@RequestMapping(value = "/file/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		String serviceType = propertiesConfiguration.getString("service.type");
		
		
		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");
		
		if(serviceType.equals("dev")) {
			 tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			 realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}
		

		

		FtMap params = getFtMap(request);

		String downloadFileInfo = params.getString("downloadFileInfo");
		Gson gson = new Gson();
		FileInfoVo fileInfoVo = gson.fromJson(downloadFileInfo, FileInfoVo.class);

		File downloadFile = null;

		try {
			if (StringUtils.defaultString(fileInfoVo.getTemp(), "").equals("Y")) {
				downloadFile = Paths.get(tempUploadPath, fileInfoVo.getFileId() + ".TEMP").toFile();

			} else {
				params.put("fileId", fileInfoVo.getFileId());
				fileInfoVo = fileUploadService.getFileInfo(params);

				// 파일다운로드 권한
				boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
				if (!fileDownloadCheck) {
					throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
				}

				downloadFile = Paths.get(realUploadPath, fileInfoVo.getFilePath(), fileInfoVo.getFileId() + ".FILE").toFile();

			}

			if (downloadFile.isFile() && downloadFile.exists()) {

				response.setContentType("application/octet-stream; charset=utf-8");

				if (downloadFile.length() > 1024 * 1024 * 20) { // 20M
					response.setHeader("Content-Transfer-Encoding", "chunked");
				} else {
					response.setContentLength((int) downloadFile.length());
					response.setHeader("Content-Transfer-Encoding", "binary");
				}
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
					// FileCopyUtils.copy(fis, out);
					CommonUtil.copy(fis, out);
				} catch (Exception e) {
					e.printStackTrace();
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}
			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
			throw new FileDownloadException(e.getMessage());
		}

	}

	@RequestMapping(value = "/file/save")
	public String save(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		fileUploadService.saveFile(params, "");

		return "core/file/upload";
	}

	@RequestMapping(value = "/file/download/zip")
	public void downloadZip(HttpServletRequest request, HttpServletResponse res) throws Exception {
		
		String serviceType = propertiesConfiguration.getString("service.type");
		String tempZipPath =propertiesConfiguration.getString("file.uploadPath.temp.zip.real");
	
		
		if(serviceType.equals("dev")) {
			tempZipPath = propertiesConfiguration.getString("file.uploadPath.temp.zip.dev");
		}

		

		FtMap params = getFtMap(request);

		String downloadFileInfo = params.getString("downloadFileInfo");

		Gson gson = new Gson();
		FileInfoVo[] fileInfoVos = gson.fromJson(downloadFileInfo, FileInfoVo[].class);

		String zipFileName = FileUtil.getRandomId() + ".zip";

		fileUploadService.createZip(fileInfoVos, tempZipPath + File.separator + zipFileName);

		// 파일다운로드
		File downloadFile = Paths.get(tempZipPath, zipFileName).toFile();

		if (downloadFile.isFile() && downloadFile.exists()) {

			res.setContentType("application/octet-stream; charset=utf-8");

			if (downloadFile.length() > 1024 * 1024 * 20) { // 20M
				res.setHeader("Content-Transfer-Encoding", "chunked");
			} else {
				res.setContentLength((int) downloadFile.length());
				res.setHeader("Content-Transfer-Encoding", "binary");
			}

			res.setHeader("Content-Disposition",
					"attachment; filename=\"" + java.net.URLEncoder.encode(zipFileName, "utf-8") + "\";");

			try (OutputStream out = res.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
				// FileCopyUtils.copy(fis, out);
				CommonUtil.copy(fis, out);
				if (downloadFile.exists())
					downloadFile.delete();
			} catch (IOException e) {
				if (downloadFile.exists())
					downloadFile.delete();
				e.printStackTrace();
			}

		} else {
			throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
		}
	}

	@RequestMapping(value = "/file/isExistFile")
	@ResponseBody
	public Map<String, Object> isExistFile(HttpServletRequest request, FileInfoVo fileInfoVo) throws Exception {
		
		String serviceType = propertiesConfiguration.getString("service.type");

		
		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");
		
		if(serviceType.equals("dev")) {
			 tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			 realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		

		FtMap params = getFtMap(request);

		boolean fileDownloadCheck = true;

		File downloadFile = null;

		if (StringUtils.defaultString(params.getString("temp"), "").equals("Y")) {
			downloadFile = Paths.get(tempUploadPath, fileInfoVo.getFileId() + ".TEMP").toFile();

		} else {
			params.put("fileId", fileInfoVo.getFileId());
			fileInfoVo = fileUploadService.getFileInfo(params);

			// 파일다운로드 권한
			fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);

			downloadFile = Paths.get(realUploadPath, fileInfoVo.getFilePath(), fileInfoVo.getFileId() + ".FILE").toFile();

		}

		Map<String, Object> map = new HashMap<String, Object>();

		if (!downloadFile.exists()) {
			map.put("msg", ErrorCode.FILE_NOT_FOUND.getMessage());
		} else if (!fileDownloadCheck) {
			map.put("msg", ErrorCode.FILE_ACCESS_DENIED.getMessage());
		} else {
			map.put("msg", "SUCCESS");
		}

		return map;

	}

	@RequestMapping(value = "/file/uploadFormAjax", method = RequestMethod.GET)
	public String uploadForm(HttpServletRequest req) throws Exception {

		FtMap params = getFtMap(req);

		setuploadForm(params.getString("acceptType"), req);
		return "framework/file/upload"; // 이 경로는 서버에서 반환할 HTML 페이지 경로
	}

}
