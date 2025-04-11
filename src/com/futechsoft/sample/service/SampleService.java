package com.futechsoft.sample.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.futechsoft.framework.annotation.CacheAccess;
import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.excel.CustomResultHandler;
import com.futechsoft.framework.excel.ExcelColumn;
import com.futechsoft.framework.excel.LargeExcel;
import com.futechsoft.framework.file.service.FileUploadService;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;
import com.futechsoft.sample.mapper.SampleMapper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * 샘플을 관리하는 Service
 * @author futech
 *
 */
@Service("sample.service.SampleService")
public class SampleService extends EgovAbstractServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleService.class);

	@Resource(name = "sample.mapper.SampleMapper")
	private SampleMapper sampleMapper;

	@Resource(name = "framework.file.service.FileUploadService")
	FileUploadService fileUploadService;



	
	/**
	 * 샘플목록을 조회한다
	 * @param pageable
	 * @param params
	 * @return 샘플목록
	 * @throws Exception
	 */
	@CacheAccess(value = "userCache")
	public Page<FtMap> selectSampleList(Pageable pageable, FtMap params) throws Exception {
		


		List<FtMap> list = sampleMapper.selectSampleList(pageable, params);
		long count = sampleMapper.countSampleList(params);

		//pageable.setTotalCount(count);
		Page<FtMap> page = new Page<FtMap>(pageable, list, count);
		
		

		return page;
	}

	/**
	 * 엑셀다운로드용 샘플목록을 조회한다
	 * @param params
	 * @param sheet
	 * @param excelColumn
	 * @param largeExcel
	 * @throws Exception
	 */
	public void selectExlSampleList(FtMap params,  SXSSFSheet sheet,  ExcelColumn excelColumn,LargeExcel largeExcel) throws Exception {

		Pageable pageable = new Pageable();
		pageable.setPaged(false);
		sampleMapper.selectSampleList(pageable, params, new CustomResultHandler(sheet, excelColumn, largeExcel));

	}

	//@PostAuthorize("isAuthenticated() and ((returnObject.userId==authentication.principal.userId) or hasRole('ROLE_ADMIN'))")
	//@PostAuthorize("isAuthenticated() and (returnObject.getString('id') == 'SAMPLE-00001' or  hasRole('ROLE_ADMIN'))")
	//@PostAuthorize("principal.userId=='jinju'")
	/**
	 * 샘플단건을 조회한다
	 * @param params
	 * @return 샘플단건
	 * @throws Exception
	 */
	public FtMap selectSample(FtMap params) throws Exception {
		return sampleMapper.selectSample(params);
	}

	/**
	 * 샘플을 등록한다
	 * @param params
	 * @throws Exception
	 */
	@Transactional
	public void insertSample(FtMap params) throws Exception {

		
		
		//업무테이블명세팅
		params.put("tblNm", "tb_propodal_master");
		params.put("userNo", SecurityUtil.getUserNo());
		
		//fileUploadService.saveFile(params, "AAAA");
		/*
		fileUploadService.saveFile(params, "AAAA","attcDocId");



		String attcDocId=fileUploadService.saveFile(params, "AAAA","attcDocId");
		LOGGER.debug("attcDocId..."+attcDocId);
		params.put("attcDocId", attcDocId);
*/

		
		
		List<String> tempDocIdList = fileUploadService.saveFiles(params, "AAAA","attcDocId");
		for(String  docInfo : tempDocIdList) {
			LOGGER.debug("mmmmmmm..."+docInfo);
		}



		sampleMapper.insertSample(params);

	}


	/**
	 * 샘플을 수정한다
	 * @param params
	 * @throws Exception
	 */
	@CacheEvict(value = "userCache", allEntries = true)
	@Transactional
	public void updateSample(FtMap params) throws Exception {

		//업무테이블명세팅
		params.put("tblNm", "tb_propodal_master");

		fileUploadService.saveFile(params, "AAAA");

		@SuppressWarnings("unchecked")
		List<String> delFileIdList = (ArrayList<String>)params.get("delFileIdList");
		@SuppressWarnings("unchecked")
		List<String> addFileIdList =(ArrayList<String>)params.get("addFileIdList");



		for(String s: delFileIdList) {

			System.out.println("delFileIdList.........."+s);
		}


		for(String s: addFileIdList) {

			System.out.println("addFileIdList.........."+s);
		}

		/*

		String attcDocId=fileUploadService.saveFile(params, "AAAA","attcDocId");
		LOGGER.debug("attcDocId..."+attcDocId);
		params.put("attcDocId", attcDocId);


		List<String> tempDocIdList = fileUploadService.saveFiles(params, "AAAA","attcDocId");
		for(String  docInfo : tempDocIdList) {
			LOGGER.debug("mmmmmmm..."+docInfo);
		}
*/


		sampleMapper.updateSample(params);
	}

	/**
	 * 샘플을 삭제한다
	 * @param params
	 * @throws Exception
	 */
	public void deleteSample(FtMap params) throws Exception {
		sampleMapper.deleteSample(params);

	}

}
