package com.futechsoft.admin.auth.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.futechsoft.admin.auth.service.AuthService;
import com.futechsoft.admin.auth.vo.Path;
import com.futechsoft.admin.auth.vo.PathAuth;
import com.futechsoft.framework.common.controller.AbstractController;
import com.futechsoft.framework.security.auth.ResourceMetaService;
import com.futechsoft.framework.util.FtMap;

@Controller
public class PathController extends AbstractController {

	@Resource(name = "auth.service.AuthService")
	private AuthService authService;

	@Resource(name = "framework.security.ResourceMetaService")
	private ResourceMetaService resourceMetaService;

	@RequestMapping("/admin/auth/pathManager")
	public String pathManager(HttpServletRequest request) throws Exception {

		return "admin_tiles:admin/auth/pathManager";
	}

	@ResponseBody
	@RequestMapping("/admin/auth/getPathList")
	public List<FtMap> getPathList(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		if (StringUtils.isEmpty(params.getString("pathSeq")) || params.getString("pathSeq").equals("#")) {
			params.put("pathSeq", "-1");
		}
		List<Path> pathList = authService.getPathList(params);

		List<FtMap> pathListMap = new ArrayList<FtMap>();

		FtMap map = null;
		for (Path path : pathList) {
			map = new FtMap();

			map.put("id", String.valueOf(path.getPathSeq()));
			map.put("text", path.getPath());
			map.put("pathNm", path.getPathNm());
			map.put("upPath", path.getUpPath());
			map.put("upPathSeq", path.getUpPathSeq());

			if (path.getPathSeq() != 0) {
				map.put("type", "030");
			}

			map.put("children", path.getSubCnt() > 0 ? true : false);

			pathListMap.add(map);
		}

		return pathListMap;
	}

	@RequestMapping("/admin/auth/authSelctor")
	public String authSelctor(HttpServletRequest request) throws Exception {
		return "admin/auth/authSelctor";
	}

	@ResponseBody
	@RequestMapping("/admin/auth/getPathAuthList")
	public List<FtMap> getPathAuthList(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		if (StringUtils.isEmpty(params.getString("authSeq")) || params.getString("authSeq").equals("#")) {
			params.put("authSeq", "-1");
		}
	//	List<Auth> authList = authService.getList(params);

		List<PathAuth> pathAuthList = authService.getPathAuthList(params);

		List<FtMap> authListMap = new ArrayList<FtMap>();

		FtMap map = null;
		boolean selected=true;
		for (PathAuth pathAuth : pathAuthList) {
			map = new FtMap();

			map.put("id", String.valueOf(pathAuth.getAuthSeq()));
			map.put("text", pathAuth.getAuthNm());
			map.put("upAuthNm", pathAuth.getAuthNm());
			map.put("upAuthSeq", pathAuth.getUpAuthSeq());
			map.put("parent", pathAuth.getUpAuthSeq() == -1 ?"#" : pathAuth.getUpAuthSeq());

			//map.put("useYn", auth.getUseYn());
			map.put("authTypeCd", pathAuth.getAuthTypeCd());
			//map.put("children", auth.getSubAuthCnt() > 0 ? true : false);
			map.put("type", pathAuth.getAuthTypeCd());

			FtMap map1 = new FtMap();
			map1.put("selected", pathAuth.getSelectYn().equals("Y")?true : false);
			map.put("state", map1);
			if(pathAuth.getSelectYn().equals("N")) {
				selected=false;
			}

			authListMap.add(map);
		}

		if(!selected) {
			authListMap.get(0).put("selected", "N");
			//map1.put("selected", "N");
		}
		System.out.println("authListMap....."+authListMap);

		return authListMap;
	}

	@ResponseBody
	@RequestMapping("/admin/auth/updatePathOrd")
	public FtMap updateMenuOrd(HttpServletRequest request, @RequestParam(value = "path_seqs[]") String[] path_seqs)
			throws Exception {

		authService.savePathOrd(path_seqs);

		FtMap params = new FtMap();
		params.put("isSuccess", true);
		return params;

	}

	@ResponseBody
	@RequestMapping("/admin/auth/savePath")
	public FtMap savePath(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		authService.savePath(params);

		resourceMetaService.createSecurityMetaDataSource();

		params.put("isSuccess", true);
		return params;

	}

	@ResponseBody
	@RequestMapping("/admin/auth/deletePath")
	public FtMap deletePath(HttpServletRequest request) throws Exception {

		FtMap params = getFtMap(request);

		authService.deletePath(params);

		resourceMetaService.createSecurityMetaDataSource();

		params.put("isSuccess", true);
		return params;

	}

	@ResponseBody
	@RequestMapping("/admin/auth/savePathAuth")
	public FtMap savePathAuth(HttpServletRequest request, @RequestBody FtMap params) throws Exception {

		@SuppressWarnings("unchecked")
		ArrayList<String> list = (ArrayList<String>) params.get("auth_seqs");
		authService.savePathAuth(params, list);

		resourceMetaService.createSecurityMetaDataSource();

		params.put("isSuccess", true);

		return params;

	}

}
