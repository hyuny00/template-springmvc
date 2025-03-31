package com.futechsoft.admin.auth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.futechsoft.admin.auth.service.AuthService;
import com.futechsoft.admin.auth.vo.Auth;
import com.futechsoft.admin.auth.vo.AuthMenu;
import com.futechsoft.admin.menu.service.MenuService;
import com.futechsoft.framework.common.constant.AuthConstant;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.security.auth.ResourceMenuService;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;

@Controller
public class AuthController extends AbstractController {

	@Resource(name = "auth.service.AuthService")
	private AuthService authService;

	@Resource(name = "menu.service.MenuService")
	private MenuService menuService;

	@Resource(name = "framework.menu.service.ResourceMenuService")
	private ResourceMenuService resourceMenuService;

	@RequestMapping("/admin/auth/authManager")
	public String menuManager(HttpServletRequest request) throws Exception {

		return "admin_tiles:admin/auth/authManager";
	}

	@ResponseBody
	@RequestMapping("/admin/auth/getAuthList")
	public List<FtMap> getAuthList(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		if (StringUtils.isEmpty(params.getString("authSeq")) || params.getString("authSeq").equals("#")) {
			params.put("authSeq", "-1");
			params.put("useYn", "Y");
		}
		List<Auth> authList = authService.getList(params);

		List<FtMap> authListMap = new ArrayList<FtMap>();

		FtMap map = null;
		for (Auth auth : authList) {
			map = new FtMap();

			map.put("id", String.valueOf(auth.getAuthSeq()));
			map.put("text", auth.getAuthNm());
			map.put("upAuthNm", auth.getUpAuthNm());
			map.put("upAuthSeq", auth.getUpAuthSeq());
			map.put("useYn", auth.getUseYn());
			map.put("authTypeCd", auth.getAuthTypeCd());
			map.put("authCd", auth.getAuthCd());

			map.put("children", auth.getSubAuthCnt() > 0 ? true : false);

			map.put("type", auth.getAuthTypeCd());

			/*
			 * if(auth.getAuthSeq() == params.getLong("selectedAuthSeq")) { FtMap map1 = new
			 * FtMap(); map1.put("selected", true); map.put("state", map1); }
			 */

			authListMap.add(map);
		}

		return authListMap;
	}

	@RequestMapping("/admin/auth/menuSelctor")
	public String menuSelctor(HttpServletRequest request) throws Exception {

		return "admin/auth/menuSelctor";
	}

	@ResponseBody
	@RequestMapping("/admin/menu/getAuthMenuList")
	public List<FtMap> getAuthMenuList(HttpServletRequest request) throws Exception {
		
		
		System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKK");

		FtMap params = getFtMap(request);

		if (StringUtils.isEmpty(params.getString("menuSeq")) || params.getString("menuSeq").equals("#")) {
			params.put("menuSeq", "-1");
		}
		//List<Menu> menuList = menuService.getList(params);

		List<AuthMenu> authMenuList = authService.getAuthMenuList(params);

		List<FtMap> menuListMap = new ArrayList<FtMap>();

		FtMap map = null;

		List<Long> noAuthMenuSeqList= new ArrayList<Long>();

		for (AuthMenu authMenu : authMenuList) {

			if(authMenu.getNoAuthCount() >0 ) {
				long upMenuSeq = authMenu.getUpMenuSeq();
				noAuthMenuSeqList.add(upMenuSeq);
			}
		}

		for (AuthMenu authMenu : authMenuList) {

			map = new FtMap();

			map.put("id", String.valueOf(authMenu.getMenuSeq()));
			map.put("text", authMenu.getMenuNm());
			map.put("menuUrl", authMenu.getMenuUrl());
			map.put("upMenuSeq", authMenu.getUpMenuSeq());
			map.put("parent", authMenu.getUpMenuSeq() == -1 ?"#" : authMenu.getUpMenuSeq());
			map.put("menuTypeCd", authMenu.getMenuTypeCd());
			//map.put("type", authMenu.getMenuTypeCd());

			map.put("type", authMenu.getSubMenuCnt() > 0 ? "noLast" : "last");

			FtMap map1 = new FtMap();

			if(noAuthMenuSeqList.contains( authMenu.getMenuSeq())  && authMenu.getSelectYn().equals("Y") ){
				map1.put("selected", false);
			}else if ( authMenu.getNoAuthCount() > 0  && authMenu.getSelectYn().equals("Y") ){
				map1.put("selected", false);
			}else if ( authMenu.getNoAuthCount() == 0  && authMenu.getSelectYn().equals("Y") ){
				map1.put("selected", true);
			}

			map.put("state", map1);

			menuListMap.add(map);

		}

		return menuListMap;
	}

	@ResponseBody
	@RequestMapping("/admin/auth/saveAuth")
	public FtMap saveAuth(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);
		
		params.put("userNo", SecurityUtil.getUserNo());

		//if(!params.getString("authCd").startsWith(AuthConstant.ROLE_PREFIX)) params.put("authCd", AuthConstant.ROLE_PREFIX+params.getString("authCd"));
		authService.save(params);

		params.put("isSuccess", true);
		return params;

	}

	@ResponseBody
	@RequestMapping("/admin/auth/deleteAuth")
	public FtMap deleteMenu(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());
		boolean check = authService.hasChildren(params);

		if (!check) {
			authService.delete(params);
			params.put("isSuccess", true);
		} else {
			params.put("isSuccess", false);
			params.put("msg", "하위항목이 존재합니다.");
		}

		return params;

	}

	@ResponseBody
	@RequestMapping("/admin/auth/saveAuthMenu")
	public FtMap saveAuthMenu(HttpServletRequest request, @RequestBody FtMap params) throws Exception {

		@SuppressWarnings("unchecked")
		ArrayList<String> list = (ArrayList<String>) params.get("menu_seqs");
		authService.saveAuthMenu(params, list);

		resourceMenuService.init();

		params.put("isSuccess", true);

		return params;

	}

}
