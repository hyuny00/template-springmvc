package com.futechsoft.framework.common.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.futechsoft.framework.common.service.CommonService;
import com.futechsoft.framework.excel.ExcelHelper;
import com.futechsoft.framework.security.event.ResourceMenuEventListener;
import com.futechsoft.framework.util.FtMap;

public abstract class AbstractController {



	@Autowired
	@Qualifier("framework.security.auth.ResourceMenuEventListener")
	private ResourceMenuEventListener resourceMenuEventListener;


	@Autowired
	@Qualifier("framework.common.service.CommonService")
	private CommonService commonService;

	@Autowired
	private ExcelHelper excelHelper;

	@Autowired
	PropertiesConfiguration propertiesConfiguration;

	protected FtMap getFtMap(HttpServletRequest request) {
		return new FtMap(request.getParameterMap());
	}

/*
	protected void loadMenuInfo(HttpServletRequest request) throws Exception {
		List<Menu> menuList =resourceMenuEventListener.getAllMenuList();

		FtMap menuCode = commonService.selectMenuMap(menuList);
		request.setAttribute("menuCode", menuCode);
	}
*/


	protected CommonService getCommonService() {
		return commonService;
	}

	protected ExcelHelper getExcelHelper() {
		return excelHelper;
	}


	protected String getExceltemplatePath(HttpServletRequest request) {

		String serviceType = propertiesConfiguration.getString("service.type");

		String templatePath = propertiesConfiguration.getString("excel.template.path.real");

		if(serviceType.equals("dev")) {
			templatePath = propertiesConfiguration.getString("excel.template.path.dev");
		}



		return  templatePath;
	}

}
