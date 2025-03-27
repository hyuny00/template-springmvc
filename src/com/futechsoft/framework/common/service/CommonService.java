package com.futechsoft.framework.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.futechsoft.admin.menu.vo.Menu;
import com.futechsoft.framework.common.mapper.CommonMapper;
import com.futechsoft.framework.security.vo.CustomUserDetails;
import com.futechsoft.framework.util.FtMap;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("framework.common.service.CommonService")
public class CommonService extends EgovAbstractServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);


	@Autowired
	@Qualifier("framework.common.mapper.CommonMapper")
	private CommonMapper commonMapper;

	//@Autowired(required=true)
	//private SessionRegistry sessionRegistry;

	/**
	 * 공통코드 목록을 가져온다
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<FtMap> selectCommonCodeList(FtMap params) throws Exception {
		return commonMapper.selectCommonCodeList(params);
	}

	/**
	 * 공통코드 목록을 ftmap형식으로 바꾼다
	 * @param codeList
	 * @return
	 * @throws Exception
	 */
	public FtMap selectCommonCodeMap(List<FtMap> codeList) throws Exception {

		FtMap codeMap = new FtMap();

		for(FtMap ftMap : codeList) {
			codeMap.put( ftMap.getString("code"), ftMap.getString("value"));
		}

		return codeMap;
	}


	public FtMap selectMenuMap(List<Menu> menuList) throws Exception {

		FtMap menuMap = new FtMap();

		for(Menu menu : menuList) {
			menuMap.put(String.valueOf( menu.getMenuSeq()),	menu);
		}

		return menuMap;
	}

	public FtMap selectMenuUrlMap(List<Menu> menuList) throws Exception {

		FtMap menuUrlMap = new FtMap();

		for(Menu menu : menuList) {
			if(menu.getMenuUrl()!=null) {
				menuUrlMap.putOrgin(menu.getMenuUrl(),	menu);
			}
		}

		return menuUrlMap;
	}


	/**
	 * 공통코드를 맵형식으로 가져온다
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public FtMap selectCommonCodeMap(FtMap params) throws Exception {
		List<FtMap> codeList = commonMapper.selectCommonCodeList(params);
		return selectCommonCodeMap(codeList);
	}


	/**
	 * 권한을 저장한다
	 * @param params
	 * @param authCd
	 * @throws Exception
	 */
	@Transactional
	public void insertUserAuth(FtMap params, String authCd) throws Exception {

		params.put("authCd", authCd);

		commonMapper.deleteUserAuth(params);
		commonMapper.insertUserAuth(params);
	}



	/**
	 * 현재 로그인한 사람목록을 가져온다
	 * @return
	 */
	/*
	public  List<CustomUserDetails> getAllPrincipals(){
		List<Object> principals = sessionRegistry.getAllPrincipals();

		List<CustomUserDetails> customUserDetailsList=new ArrayList<CustomUserDetails>();


		for (Object principal: principals) {

		    if (principal instanceof CustomUserDetails) {
		    	CustomUserDetails customUserDetails = (CustomUserDetails)principal;
		    	customUserDetailsList.add(customUserDetails);
		    }
		}
		return customUserDetailsList;

	}
*/
}
