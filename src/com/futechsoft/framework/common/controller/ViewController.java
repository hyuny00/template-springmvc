package com.futechsoft.framework.common.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;
import com.futechsoft.sample.controller.SampleController;

@Controller
public class ViewController  extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

	@RequestMapping(value = "/common/view")
	public String commonPupup(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		return params.getString("view");
	}


	@RequestMapping(path = {"/common/{pathName1}", "/common/{pathName1}/{pathName2}",  "/common/{pathName1}/{pathName2}/{pathName3}" })
	public String commonPage(HttpServletRequest request, @PathVariable String  pathName1, @PathVariable Optional<String>  pathName2, @PathVariable Optional<String>  pathName3) throws Exception {


		String path="";


		path="/"+pathName1;

		if(pathName2.isPresent()) {
			path+="/"+pathName2.get();
		}

		if(pathName3.isPresent()) {
			path+="/"+pathName3.get();
		}

		FtMap params = super.getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		return "tiles:common/"+path;
	}




	/**
	 * 코드 목록을 가져온다
	 * @param request
	 * @return 코드
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/selectCode")
	@ResponseBody
	public List<FtMap> selectCode(HttpServletRequest request) throws Exception {

		FtMap params = super.getFtMap(request);

		LOGGER.debug("code.." + params.getString("code"));

		int code= params.getInt("code");

		params.put("userNo", SecurityUtil.getUserNo());

		params.put("upCdSeq", code);
		List<FtMap> codeList = getCommonService().selectCommonCodeList(params);


		return codeList;

	}
}
