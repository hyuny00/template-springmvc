package com.futechsoft.admin.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.futechsoft.admin.auth.service.AuthService;
import com.futechsoft.admin.user.service.UserService;
import com.futechsoft.admin.user.vo.UserAuth;
import com.futechsoft.framework.common.constant.ViewInfo;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.security.auth.ResourceMetaService;
import com.futechsoft.framework.security.crypto.Sha256PasswordEncoder;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;

@Controller
public class UserController extends AbstractController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Resource(name = "user.service.UserService")
	UserService service;

	@Resource(name = "auth.service.AuthService")
	private AuthService authService;

	@Resource(name = "framework.security.ResourceMetaService")
	private ResourceMetaService resourceMetaService;

	@RequestMapping("/admin/user/userList")
	public String list(Pageable pageble, HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);
		
		
		System.out.println(params.toString());
		
		
		params.put("userNo", SecurityUtil.getUserNo());

		Page<FtMap> page = service.getPageList(pageble, params);

		request.setAttribute("list", page.getList());
		request.setAttribute("pageble", page.getPageable());

		return "admin_tiles:admin/user/userList";
		
		//return "main";
	}

	@RequestMapping("/admin/user/userForm")
	public String userForm(Pageable pageble, HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);
		params.put("userNo", SecurityUtil.getUserNo());

		return "admin_tiles:admin/user/userDetail";
	}

	@RequestMapping("/admin/user/userDetail")
	public String userDetail(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		FtMap result = service.detail(params);

		request.setAttribute("result", result);
		return "admin_tiles:admin/user/userDetail";
	}

	@ResponseBody
	@RequestMapping("/admin/user/getUserAuthList")
	public List<FtMap> getUserAuthList(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		if (StringUtils.isEmpty(params.getString("authSeq")) || params.getString("authSeq").equals("#")) {
			params.put("authSeq", "-1");
		}
		//List<Auth> authList = authService.getList(params);

		List<UserAuth> userAuthList = service.getUserAuthList(params);

		List<FtMap> authListMap = new ArrayList<FtMap>();

		FtMap map = null;
		for (UserAuth userAuth : userAuthList) {
			map = new FtMap();

			map.put("id", String.valueOf(userAuth.getAuthSeq()));
			map.put("text",userAuth.getAuthNm());
			map.put("authCd", userAuth.getAuthCd());
			map.put("authSeq", userAuth.getAuthSeq());
			map.put("upAuthSeq", userAuth.getUpAuthSeq());

			map.put("parent", userAuth.getUpAuthSeq() == -1 ?"#" : userAuth.getUpAuthSeq());

			map.put("authTypeCd", userAuth.getAuthTypeCd());
			map.put("type", userAuth.getAuthTypeCd());

			FtMap map1 = new FtMap();
			map1.put("selected", userAuth.getSelectYn().equals("Y")?true : false);
			map.put("state", map1);


			authListMap.add(map);
		}

		return authListMap;
	}

	@RequestMapping("/admin/user/saveUser")
	public String saveUser(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);
		params.put("userPwd", passwordEncoder.encode(params.getString("userPwd")));

		service.create(params);

		request.setAttribute(ViewInfo.REDIRECT_URL, "/admin/user/userList");

		return ViewInfo.REDIRECT_PAGE;
	}

	@RequestMapping("/admin/user/updateUser")
	public String updateUser(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		params.put("userPwd", passwordEncoder.encode(params.getString("userPwd")));
		service.update(params);

		request.setAttribute("sendParams", "userNo");
		request.setAttribute(ViewInfo.REDIRECT_URL, "/admin/user/userDetail");

		return ViewInfo.REDIRECT_PAGE;
	}

	@RequestMapping("/admin/user/deleteUser")
	public String deleteUser(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		service.delete(params);

		request.setAttribute(ViewInfo.REDIRECT_URL, "/admin/user/userList");
		return ViewInfo.REDIRECT_PAGE;
	}
}
