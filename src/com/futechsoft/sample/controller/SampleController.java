package com.futechsoft.sample.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.futechsoft.framework.common.constant.ViewInfo;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.excel.CellVo;
import com.futechsoft.framework.excel.ExcelColumn;
import com.futechsoft.framework.excel.LargeExcel;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;
import com.futechsoft.sample.service.SampleService;

/**
 * 샘플을 관리하는 controller 클래스
 * @author futech
 *
 */
@Controller
public class SampleController extends AbstractController{

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	@Resource(name = "sample.service.SampleService")
	SampleService sampleService;


	/**
	 * 샘플목록을 조회한다
	 * @param pageble
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping("/sample/selectSampleList")
	public String selectSampleList(Pageable pageble, HttpServletRequest request) throws Exception {

		// 권한 체크 필요할때(ROLE_ 붙여야함)
		if (SecurityUtil.hasAuth("ROLE_ADMIN")) {
			LOGGER.debug("ROLE_ADMIN");
		}

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		Page<FtMap> page = sampleService.selectSampleList(pageble, params);

		/* 공통코드목록 가져오는 방법*/
		params.put("upCdSeq", 800);
		List<FtMap> codeList = super.getCommonService().selectCommonCodeList(params);
		request.setAttribute("codeList", codeList);


		params.put("upCdSeq", 77);
		List<FtMap> sampleCodeList = getCommonService().selectCommonCodeList(params);
		request.setAttribute("sampleCodeList", sampleCodeList);


		/*공통코드 목록을 map형식으로 변환*/
		//FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		FtMap etcCode = super.getCommonService().selectCommonCodeMap(codeList);
		LOGGER.debug(etcCode.toString());
		request.setAttribute("etcCode", etcCode);

		
		request.setAttribute("list", page.getList());
		request.setAttribute("pageObject", page.getPageable());
		return "tiles:sample/sampleList";
	}
 
	/**
	 * 샘플폼으로 이동한다
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/sampleForm")
	public String sampleForm(HttpServletRequest request) throws Exception {

		return "tiles:sample/sampleForm";
	}

	/**
	 * 샘플을 등록한다
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/insertSample")
	public String insertSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		sampleService.insertSample(params);

		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSampleList");
		return ViewInfo.REDIRECT_PAGE;

	}

	/**
	 * 샘플단건을 조회한다
	 * @param request
	 * @return  jsp경로
	 * @throws Exception
	 */
	@RequestMapping("/sample/selectSample")
	public String selectSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());
		FtMap result = sampleService.selectSample(params);

		request.setAttribute("result", result);


		return "tiles:sample/sampleForm";
	}

	/**
	 * 샘플을 수정한다
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping("/sample/updateSample")
	public String updateSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		sampleService.updateSample(params);

		// 조회할 key값세팅. 여러개 세팅시: request.setAttribute("sendParams","id,name");, 검색조건,페이지번호등은 세팅할 필요없음.
		request.setAttribute("sendParams", "id");
		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSample");
		return ViewInfo.REDIRECT_PAGE;
	}

	/**
	 * 샘플을 삭제한다
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping("/sample/deleteSample")
	public String deleteSample(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		sampleService.deleteSample(params);

		request.setAttribute("message", "삭제되었습니다.");
		request.setAttribute(ViewInfo.REDIRECT_URL, "/sample/selectSampleList");
		return ViewInfo.REDIRECT_PAGE;

	}

	/**
	 * 샘플 팝업을 호출한다
	 * @param request
	 * @return jsp경로
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/samplePopup")
	public String samplePopup(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		LOGGER.debug("testId.." + params.getString("testId"));

		params.put("userNo", SecurityUtil.getUserNo());

		request.setAttribute("contents", "hello....");

		return "sample/samplePopup";
	}

	@RequestMapping(value = "/dashboard")
	public String getChatViewPage(ModelAndView mav) {
		return "tiles:dashboard/dashboard";
	}


	/**
	 * 엑셀을 다운로드한다
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/excelDown")
	public void excelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {

		FtMap params = getFtMap(request);

		Pageable pageable = new Pageable();
		pageable.setPaged(false);
		Page<FtMap> page = sampleService.selectSampleList(pageable, params);

		String templateFilePath = getExceltemplatePath(request);
		File templateFile=new File(templateFilePath, "test_1.xlsx");

		//단순 엑셀다운로드
		//String[] columnValue= {"id", "name", "regUser"};
		//getExcelHelper().excelDownload(response, templateFile, page.getList(), columnValue);


		// 포맷 , 코드 ,정렬 필요시
		params.put("upCdSeq", 800);
		FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		ExcelColumn excelColumn  = new ExcelColumn(
													new CellVo(CellVo.CELL_STRING, "id", 			CellVo.ALIGN_RIGHT),
													new CellVo(CellVo.CELL_STRING, "name", 	CellVo.ALIGN_CENTER),
													new CellVo(CellVo.CELL_STRING, "etc_code",	codeMap, CellVo.ALIGN_LEFT)
							);
		getExcelHelper().excelDownload(response, templateFile, page.getList(), excelColumn);


	}


	/**
	 * 대용량 엑셀을 다운로드한다
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/file/largeExcelDown")
	public void largeExcelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {

		FtMap params = getFtMap(request);
		 params.put("userNo", SecurityUtil.getUserNo());

		String templateFilePath = getExceltemplatePath(request);
		File templateFile=new File(templateFilePath, "test_1.xlsx");

		LargeExcel largeExcel = getExcelHelper().preparedLargeExcel(templateFile);

		//String[] columnValue= {"id", "name", "etcCode"};
		params.put("upCdSeq", 800);
		FtMap codeMap = getCommonService().selectCommonCodeMap(params);
		ExcelColumn excelColumn  = new ExcelColumn(
													new CellVo(CellVo.CELL_STRING, "id", 			CellVo.ALIGN_CENTER),
													new CellVo(CellVo.CELL_STRING, "name", 	CellVo.ALIGN_RIGHT),
													new CellVo(CellVo.CELL_STRING, "etc_code",	codeMap, CellVo.ALIGN_CENTER)
							);

		sampleService.selectExlSampleList(params, largeExcel.getSheet(), excelColumn, largeExcel);

		getExcelHelper().endLargeExcel( response,  largeExcel.getWorkbook(), templateFile.getName());

	}


	@RequestMapping(value = "/file/jexcelDown")
	public void jexcelDown(HttpServletRequest request, HttpServletResponse response) throws Exception {


		try ( InputStream io = new FileInputStream(new File(getExceltemplatePath(request), "test.xlsx"));
				OutputStream os = response.getOutputStream();) {

			List<FtMap> dataList = new ArrayList<FtMap>();
			FtMap ftMap= new FtMap();
			ftMap.put("seq", "1");
			ftMap.put("name", "122");
			ftMap.put("phone", "00-22-22");

			dataList.add(ftMap);


			ftMap= new FtMap();
			ftMap.put("seq", "11");
			ftMap.put("name", "1221");
			ftMap.put("phone", "00-212-22");

			dataList.add(ftMap);

			Context context = new Context();
			context.putVar("dataList", dataList);

			response.setContentType("application/msexcel");
			response.setHeader("Set-Cookie", "fileDownload=true; path=/");
			response.setHeader("Content-Disposition", "attachment; filename=\"" +  java.net.URLEncoder.encode( "테스트.xlsx", "utf-8") + "\";");


			JxlsHelper.getInstance().processTemplate(io, os, context);
		} catch (Exception e) {
			response.setHeader("Set-Cookie", "fileDownload=false; path=/");
			LOGGER.error(e.toString());
		}

	}

}
