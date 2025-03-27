package com.futechsoft.framework.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

import egovframework.rte.fdl.logging.util.EgovResourceReleaser;

/**
 * 파일업로드를 관리하는 클래스이다
 *
 * @author futech
 *
 */
@Controller
public class FileUploadController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);


	@Autowired
	@Qualifier("framework.file.service.FileUploadService")
	FileUploadService fileUploadService;

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	/**
	 * 파일업로드를 실행한다
	 *
	 * @param model
	 * @param file
	 * @param metadata
	 * @return 파일정보
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws FileUploadException
	 */
	@RequestMapping(value = "/file/upload")
	@ResponseBody
	public Map<String, Object> upload(Model model, @RequestParam MultipartFile file, @RequestParam String metadata) throws JsonMappingException, JsonProcessingException, FileUploadException {

		String[] acceptDocs = propertiesConfiguration.getStringArray("file.upload.accept.doc");
		String[] acceptImages = propertiesConfiguration.getStringArray("file.upload.accept.image");
		String[] acceptMultimedias = propertiesConfiguration.getStringArray("file.upload.accept.multimedia");

		String acceptDoc = "";
		String acceptImage = "";
		String acceptMultimedia = "";
		for (String accept : acceptDocs) {
			acceptDoc += accept + ",";
		}
		acceptDoc = acceptDoc.substring(0, acceptDoc.lastIndexOf(","));

		for (String accept : acceptImages) {
			acceptImage += accept + ",";
		}

		acceptImage = acceptImage.substring(0, acceptImage.lastIndexOf(","));

		for (String accept : acceptMultimedias) {
			acceptMultimedia += accept + ",";
		}
		acceptMultimedia = acceptMultimedia.substring(0, acceptMultimedia.lastIndexOf(","));

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
			fileInfo.put("errorMessage", StringUtils.replace(ErrorCode.FILE_ACCEPT_ERROR.getMessage(), "{{fileExt}}", extName));
		}

		if (fileInfoVo.getFileSize() > uploadSize || file.getSize() > uploadSize || file.getSize() > chunkSize) {
			fileInfo.put("errorCode", ErrorCode.FILE_SIZE_ERROR.getCode());
			fileInfo.put("errorMessage", StringUtils.replace(ErrorCode.FILE_SIZE_ERROR.getMessage(), "{{fileSize}}", (uploadSize / 1024 / 1024) + "M"));

		} else {
			fileInfoVo = fileUploadService.upload(file, fileInfoVo);
			List<FileInfoVo> fileList = new ArrayList<FileInfoVo>();
			fileList.add(fileInfoVo);
			fileInfo.put("fileInfo", fileList);
		}

		return fileInfo;
	}

	/**
	 * 파일목록정보를 구한다
	 *
	 * @param request
	 * @return 파일목록
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/fileList")
	@ResponseBody
	public Map<String, List<FtMap>> fileList(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		List<FtMap> fileList = fileUploadService.selectFileList(params);
		Map<String, List<FtMap>> fileInfo = new HashMap<String, List<FtMap>>();
		fileInfo.put("fileInfo", fileList);
		return fileInfo;

	}

	/**
	 * 파일을 삭제한다
	 *
	 * @param fileInfoVos
	 * @return 파일정보
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/deleteFile")
	@ResponseBody
	public Map<String, List<FileInfoVo>> deleteFile(@RequestBody FileInfoVo[] fileInfoVos) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		/*
		 * Gson gson = new Gson(); FileInfoVo[] fileInfoVos = gson.fromJson(delFileInfo,
		 * FileInfoVo[].class);
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

				delFile = Paths.get(realUploadPath, StringUtils.defaultString(realFileInfoVo.getFilePath()), realFileInfoVo.getFileId() + ".FILE").toFile();

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

	/**
	 * 업로드 화면으로 이동한다
	 *
	 * @param acceptType
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/uploadForm")
	public String uploadForm(@RequestParam(required = false, defaultValue = "") String acceptType, HttpServletRequest req) throws Exception {

		setUploadForm(acceptType, req);
		return "framework/file/upload";
	}

	/**
	 * 업로드 폼 세팅
	 *
	 * @param acceptType
	 * @param req
	 * @throws Exception
	 */
	private void setUploadForm(String acceptType, HttpServletRequest req) throws Exception {

		String[] acceptDocs = propertiesConfiguration.getStringArray("file.upload.accept.doc");
		String[] acceptImages = propertiesConfiguration.getStringArray("file.upload.accept.image");
		String[] acceptMultimedias = propertiesConfiguration.getStringArray("file.upload.accept.multimedia");

		String acceptDoc = "";
		String acceptImage = "";
		String acceptMultimedia = "";
		for (String accept : acceptDocs) {
			acceptDoc += accept + ",";
		}
		acceptDoc = acceptDoc.substring(0, acceptDoc.lastIndexOf(","));

		for (String accept : acceptImages) {
			acceptImage += accept + ",";
		}

		acceptImage = acceptImage.substring(0, acceptImage.lastIndexOf(","));

		for (String accept : acceptMultimedias) {
			acceptMultimedia += accept + ",";
		}
		acceptMultimedia = acceptMultimedia.substring(0, acceptMultimedia.lastIndexOf(","));

		String uploadFormId = FileUtil.getRandomId();

		req.setAttribute("uploadFormId", uploadFormId);

		if (acceptType.equals("doc")) {
			req.setAttribute("accept", acceptDoc);

		} else if (acceptType.equals("image")) {
			req.setAttribute("accept", acceptImage);

		} else if (acceptType.equals("multimedia")) {
			req.setAttribute("accept", acceptMultimedia);

		} else {
			req.setAttribute("accept", acceptDoc + acceptImage + acceptMultimedia);
		}
	}

	/**
	 * 파일을 다운로드한다
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
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
				fileInfoVo.setFileType("PISYCAL");
			} else {
				params.put("fileId", fileInfoVo.getFileId());
				fileInfoVo = fileUploadService.getFileInfo(params);

				// 파일다운로드 권한
				boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
				if (!fileDownloadCheck) {
					throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
				}

				if (fileInfoVo.getFileType().equals("PISYCAL")) {
					downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();
				}

			}

			if (downloadFile != null && downloadFile.isFile() && downloadFile.exists() && fileInfoVo.getFileType().equals("PISYCAL")) {

				response.setContentType("application/octet-stream; charset=utf-8");

				if (downloadFile.length() > 1024 * 1024 * 20) { // 20M
					response.setHeader("Content-Transfer-Encoding", "chunked");
				} else {
					response.setContentLength((int) downloadFile.length());
					response.setHeader("Content-Transfer-Encoding", "binary");
				}
				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
					// FileCopyUtils.copy(fis, out);
					CommonUtil.copy(fis, out);
				} catch (Exception e) {
//					e.printStackTrace();
					LOGGER.error(e.toString());
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}
			} else if (fileInfoVo.getFileType().equals("BLOB")) {

				FtMap fileDataMap = fileUploadService.selectFileDataInfo(params);

				byte[] fileByte = (byte[]) fileDataMap.get("fileData");

				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream();) {
					FileCopyUtils.copy(fileByte, out);
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}

			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
			throw new FileDownloadException(e.getMessage());
		}

	}

	/**
	 * 파일을 저장한다
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/save")
	public String save(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		fileUploadService.saveFile(params, "");

		return "core/file/upload";
	}

	/**
	 * 파일을 압축해서 다운로드한다
	 *
	 * @param request
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/download/zip")
	public void downloadZip(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String tempZipPath = propertiesConfiguration.getString("file.uploadPath.temp.zip");

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

			res.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(zipFileName, "utf-8") + "\";");

			try (OutputStream out = res.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
				// FileCopyUtils.copy(fis, out);
				CommonUtil.copy(fis, out);
				if (downloadFile.exists())
					downloadFile.delete();
			} catch (IOException e) {
				if (downloadFile.exists())
					downloadFile.delete();
//				e.printStackTrace();
				LOGGER.error(e.toString());
			}

		} else {
			throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
		}
	}

	/**
	 * 파일이 존재하는지 확인한다
	 *
	 * @param request
	 * @param fileInfoVo
	 * @return 파일존재여부
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/isExistFile")
	@ResponseBody
	public Map<String, Object> isExistFile(HttpServletRequest request, FileInfoVo fileInfoVo) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);

		boolean fileDownloadCheck = true;

		File downloadFile = null;

		if (StringUtils.defaultString(params.getString("temp"), "").equals("Y")) {
			downloadFile = Paths.get(tempUploadPath, fileInfoVo.getFileId() + ".TEMP").toFile();
			fileInfoVo.setFileType("PISYCAL");

		} else {
			params.put("fileId", fileInfoVo.getFileId());

			fileInfoVo = fileUploadService.getFileInfo(params);

			// 파일다운로드 권한
			fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);

			if (fileInfoVo.getFileType().equals("PISYCAL") && fileInfoVo != null) {
				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();
			}

		}

		Map<String, Object> map = new HashMap<String, Object>();

		if (fileInfoVo != null) {

			if (fileInfoVo.getFileType().equals("PISYCAL") && !downloadFile.exists()) {
				map.put("msg", ErrorCode.FILE_NOT_FOUND.getMessage());
			} else if (!fileDownloadCheck) {
				map.put("msg", ErrorCode.FILE_ACCESS_DENIED.getMessage());
			} else {
				map.put("msg", "SUCCESS");
			}
		} else {
			map.put("msg", ErrorCode.FILE_NOT_FOUND.getMessage());
		}

		return map;

	}

	/**
	 * 한글파일을 다운로드한다 (솔루션)
	 *
	 * @param request
	 * @param response
	 * @param fileId
	 * @param fileNm
	 * @param temp
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/commonHtrlDownload/{fileId}/{fileNm}/{temp}")
	public void commonHtrlDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId, @PathVariable String fileNm, @PathVariable String temp) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);
		params.put("fileId", fileId);

		fileNm = URLDecoder.decode(fileNm, "utf-8");

		File downloadFile = null;

		FileInfoVo fileInfoVo = new FileInfoVo();

		try {
			if (StringUtils.defaultString(temp, "").equals("Y")) {
				downloadFile = Paths.get(tempUploadPath, fileId + ".TEMP").toFile();

				fileInfoVo.setFileNm(fileNm);
				fileInfoVo.setFileId(fileId);
				fileInfoVo.setTemp(temp);

			} else {

				fileInfoVo = fileUploadService.getFileInfo(params);

				// 파일다운로드 권한
				boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
				if (!fileDownloadCheck) {
					throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
				}

				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();

			}

			if (downloadFile.isFile() && downloadFile.exists()) {

				response.setContentType("application/octet-stream; charset=utf-8");

				if (downloadFile.length() > 1024 * 1024 * 20) { // 20M
					response.setHeader("Content-Transfer-Encoding", "chunked");
				} else {
					response.setContentLength((int) downloadFile.length());
					response.setHeader("Content-Transfer-Encoding", "binary");
				}
				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
					// FileCopyUtils.copy(fis, out);
					CommonUtil.copy(fis, out);
				} catch (Exception e) {
//					e.printStackTrace();
					LOGGER.error(e.toString());
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}
			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LOGGER.error(e.toString());
			throw new FileDownloadException(e.getMessage());
		}

	}

	/**
	 * 한그파일을 base64 형식으로 조회한다
	 *
	 * @param request
	 * @param response
	 * @param fileId
	 * @param fileNm
	 * @param temp
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/file/commonHtrlBase64/{fileId}/{temp}")
	public String commonHtrlBase64(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId, @PathVariable String temp) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);
		params.put("fileId", fileId);

		File downloadFile = null;

		String base64Str = "";
		try {
			if (StringUtils.defaultString(temp, "").equals("Y")) {
				downloadFile = Paths.get(tempUploadPath, fileId + ".TEMP").toFile();

			} else {

				FileInfoVo fileInfoVo = fileUploadService.getFileInfo(params);

				// 파일다운로드 권한
				boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
				if (!fileDownloadCheck) {
					throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
				}

				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();

			}

			if (downloadFile.isFile() && downloadFile.exists()) {

				byte[] fileByte = FileUtil.getFileBinary(downloadFile);
				base64Str = Base64.getEncoder().encodeToString(fileByte);

			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LOGGER.error(e.toString());
			throw new FileDownloadException(e.getMessage());
		}

		return base64Str;

	}

	/**
	 * 한글파일을 저장한다
	 *
	 * @param request
	 * @param response
	 * @return 성공실패여부
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/file/saveHwp")
	public String saveHwp(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);

		File downloadFile = null;

		String base64Str = params.getString("base64Data");
		byte[] base64DecodeFileData = Base64.getDecoder().decode(base64Str);

		String result = "";

		try {
			if (StringUtils.defaultString(params.getString("temp"), "").equals("Y")) {
				downloadFile = Paths.get(tempUploadPath, params.getString("fileId") + ".TEMP").toFile();
			} else {
				FileInfoVo fileInfoVo = fileUploadService.getFileInfo(params);
				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();
			}


			if (downloadFile.isFile() && downloadFile.exists()) {

				FileOutputStream fos = new FileOutputStream(downloadFile);
				FileCopyUtils.copy(base64DecodeFileData, fos);
				result = "OK";

				EgovResourceReleaser.close(fos);

			} else {
				result = "FAIL";
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LOGGER.error(e.toString());
			throw new FileDownloadException(e.getMessage());
		}

		return result;

	}

	/**
	 * 파일을 다운로드한다
	 *
	 * @param request
	 * @param response
	 * @param fileId
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/commonDownload/{fileId}")
	public void commonDownload(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);

		FileInfoVo fileInfoVo = null;
		File downloadFile = null;

		try {
			params.put("fileId", fileId);
			fileInfoVo = fileUploadService.getFileInfo(params);

			// 파일다운로드 권한
			boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
			if (!fileDownloadCheck) {
				throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
			}

			if (fileInfoVo.getFileType().equals("PISYCAL")) {
				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();
			}

			if (downloadFile != null && downloadFile.isFile() && downloadFile.exists() && fileInfoVo.getFileType().equals("PISYCAL")) {

				response.setContentType("application/octet-stream; charset=utf-8");

				if (downloadFile.length() > 1024 * 1024 * 20) { // 20M
					response.setHeader("Content-Transfer-Encoding", "chunked");
				} else {
					response.setContentLength((int) downloadFile.length());
					response.setHeader("Content-Transfer-Encoding", "binary");
				}
				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream(); FileInputStream fis = new FileInputStream(downloadFile);) {
					CommonUtil.copy(fis, out);
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}

			} else if (fileInfoVo.getFileType().equals("BLOB")) {

				FtMap fileDataMap = fileUploadService.selectFileDataInfo(params);

				byte[] fileByte = (byte[]) fileDataMap.get("fileData");

				response.setHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileInfoVo.getFileNm(), "utf-8") + "\";");

				try (OutputStream out = response.getOutputStream();) {
					FileCopyUtils.copy(fileByte, out);
				} catch (Exception e) {
					LOGGER.error(e.toString());
					throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
				}

			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
			throw new FileDownloadException(e.getMessage());
		}

	}

	/**
	 * 검색엔진요청 : 파일이 존재하는제 조회한다.
	 *
	 * @param request
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/isExistSearchFile/{fileId}")
	@ResponseBody
	public Map<String, Object> isExistSearchFile(HttpServletRequest request, @PathVariable String fileId) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);

		boolean fileDownloadCheck = true;

		File downloadFile = null;

		params.put("fileId", fileId);
		FileInfoVo fileInfoVo = fileUploadService.getFileInfo(params);

		Map<String, Object> map = new HashMap<String, Object>();

		if (fileInfoVo != null) {

			// 파일다운로드 권한
			fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
			downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();

			if (!downloadFile.exists()) {
				map.put("msg", ErrorCode.FILE_NOT_FOUND.getMessage());
			} else if (!fileDownloadCheck) {
				map.put("msg", ErrorCode.FILE_ACCESS_DENIED.getMessage());
			} else {
				map.put("msg", "SUCCESS");
			}
		} else {
			map.put("msg", ErrorCode.FILE_NOT_FOUND.getMessage());
		}

		return map;

	}

	@RequestMapping(value = "/file/hwpCtrlPopup")
	public String hwpCtrlPopup(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String serviceType = propertiesConfiguration.getString("service.type");

		String tempUploadPath = propertiesConfiguration.getString("file.uploadPath.real.temp");
		String realUploadPath = propertiesConfiguration.getString("file.uploadPath.real");

		if (serviceType.equals("dev")) {
			tempUploadPath = propertiesConfiguration.getString("file.uploadPath.dev.temp");
			realUploadPath = propertiesConfiguration.getString("file.uploadPath.dev");
		}

		FtMap params = getFtMap(request);

		String fileId = params.getString("hwpFileId");
		String temp = params.getString("hwpTemp");

		params.put("fileId", fileId);

		File downloadFile = null;

		String base64Str = "";
		try {
			if (StringUtils.defaultString(temp, "").equals("Y")) {
				downloadFile = Paths.get(tempUploadPath, fileId + ".TEMP").toFile();

			} else {

				FileInfoVo fileInfoVo = fileUploadService.getFileInfo(params);

				// 파일다운로드 권한
				boolean fileDownloadCheck = FileUtil.hasFileDownloadAuth(fileInfoVo);
				if (!fileDownloadCheck) {
					throw new FileDownloadException(ErrorCode.FILE_ACCESS_DENIED.getMessage());
				}

				downloadFile = Paths.get(realUploadPath, StringUtils.defaultString(fileInfoVo.getFilePath()), fileInfoVo.getFileId() + ".FILE").toFile();

			}

			if (downloadFile.isFile() && downloadFile.exists()) {

				byte[] fileByte = FileUtil.getFileBinary(downloadFile);
				base64Str = Base64.getEncoder().encodeToString(fileByte);

			} else {
				throw new FileDownloadException(ErrorCode.FILE_NOT_FOUND.getMessage());
			}
		} catch (Exception e) {
//			e.printStackTrace();
			LOGGER.error(e.toString());
			throw new FileDownloadException(e.getMessage());
		}

		request.setAttribute("hwpFileId", fileId);
		request.setAttribute("hwpFileNm", params.getString("hwpFileNm"));
		request.setAttribute("hwpTemp", temp);

		request.setAttribute("base64Str", base64Str);

		return "common/hwpCtrl/hwpCtrlEditor";

	}
}
