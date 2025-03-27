package com.futechsoft.admin.auth.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.futechsoft.admin.auth.mapper.AuthMapper;
import com.futechsoft.admin.auth.vo.Auth;
import com.futechsoft.admin.auth.vo.AuthMenu;
import com.futechsoft.admin.auth.vo.Path;
import com.futechsoft.admin.auth.vo.PathAuth;
import com.futechsoft.framework.util.FtMap;
import com.futechsoft.framework.util.SecurityUtil;

@Service("auth.service.AuthService")
public class AuthService {

	@Resource(name = "auth.mapper.AuthMapper")
	private AuthMapper authMapper;


	public List<Auth> getList(FtMap params) throws Exception {

		return authMapper.getAuthList(params);
	}

	@Transactional
	public void save(FtMap params) throws Exception {
		System.out.println("authManager::"+params);
		if (params.getString("mode").equals("new")) {
			authMapper.insertAuth(params);
		} else {
			authMapper.updateAuth(params);
		}

	}

	@Transactional
	public void saveAuthMenu(FtMap params, ArrayList<String> menuSeqs) throws Exception {

		
		authMapper.deleteAuthMenu(params);
		params.put("userNo", SecurityUtil.getUserNo());
		
		for (int i = 0; i < menuSeqs.size(); i++) {

			if (menuSeqs.get(i).equals("#") )
				continue;

			params.put("menuSeq", menuSeqs.get(i));
			
		

			authMapper.insertAuthMenu(params);
		}

	}

	public List<AuthMenu> getAuthMenuList(FtMap params) throws Exception {
		return authMapper.getAuthMenuList(params);
	}

	public List<Path> getPathList(FtMap params) throws Exception {
		return authMapper.getPathList(params);
	}

	public List<PathAuth> getPathAuthList(FtMap params) throws Exception {
		return authMapper.getPathAuthList(params);
	}

	@Transactional
	public void savePath(FtMap params) throws Exception {

		params.put("userNo", SecurityUtil.getUserNo());
		if (params.getString("mode").equals("new")) {
			params.put("pathOrd", 99);
			authMapper.insertPath(params);
		} else {
			authMapper.updatePath(params);
		}
	}

	@Transactional
	public void deletePath(FtMap params) throws Exception {

		authMapper.deletePathAuthByPathSeq(params);
		authMapper.deletePath(params);

	}

	@Transactional
	public void savePathAuth(FtMap params, ArrayList<String> authSeqs) throws Exception {
		
		authMapper.deletePathAuthByPathSeq(params);
		params.put("userNo", SecurityUtil.getUserNo());
		
		for (int i = 0; i < authSeqs.size(); i++) {
			if (authSeqs.get(i).equals("#") || authSeqs.get(i).equals("0"))
				continue;
			params.put("authSeq", authSeqs.get(i));
		
			authMapper.insertPathAuth(params);
		}

	}

	@Transactional
	public void savePathOrd(String[] menuSeqs) throws Exception {

		FtMap params = new FtMap();
		for (int i = 0; i < menuSeqs.length; i++) {
			params.put("pathSeq", menuSeqs[i]);
			params.put("pathOrd", i);
			
			authMapper.updatePathOrd(params);
		}

	}

	public boolean hasChildren(FtMap params) throws Exception {

		params.put("upAuthSeq", params.getString("authSeq"));
		int count = authMapper.getAuthCount(params);

		if (count == 0)
			return false;
		else
			return true;

	}

	@Transactional
	public void delete(FtMap params) throws Exception {
		
		authMapper.deleteUserAuth(params);
		authMapper.deleteAuthMenu(params);
		authMapper.deleteAuth(params);
		authMapper.deletePathAuthByAuthSeq(params);

	}
	

}
