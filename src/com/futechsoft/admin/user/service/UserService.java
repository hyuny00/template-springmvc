package com.futechsoft.admin.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.futechsoft.admin.user.mapper.UserMapper;
import com.futechsoft.admin.user.vo.UserAuth;
import com.futechsoft.framework.common.page.Page;
import com.futechsoft.framework.common.page.Pageable;
import com.futechsoft.framework.util.FtMap;

@Service("user.service.UserService")
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Resource(name = "user.mapper.UserMapper")
	private UserMapper mapper;

	
	public Page<FtMap> getPageList(Pageable pageable, FtMap params) throws Exception {

		List<FtMap> list = mapper.selectUserList(pageable, params);
		long count = mapper.countUserList(params);

		Page<FtMap> page = new Page<FtMap>(pageable, list, count);

		return page;
	}

	public FtMap detail(FtMap params) throws Exception {

		return mapper.detailUser(params);

	}

	public List<UserAuth> getUserAuthList(FtMap params) throws Exception {
		return mapper.getUserAuthList(params);
	}


	@Transactional
	public void create(FtMap params) throws Exception {

		mapper.insertUser(params);
		// 등록된 키값 세팅
		String userNo = params.getString("userNo");
		params.put("userNo", userNo);

		saveUserAuth(params);
		
	}

	@Transactional
	public void update(FtMap params) throws Exception {

		mapper.deleteUserAuth(params);
		saveUserAuth(params);
		
	}

	@Transactional
	private void saveUserAuth(FtMap params) throws Exception {

		String[] authSeqs = params.getString("authSeqs").split(",");

	
		for (String authSeq : authSeqs) {
			if (authSeq.equals("0") || StringUtils.isEmpty(authSeq))
				continue;
			params.put("authSeq", authSeq);
			mapper.insertUserAuth(params);
		}
		mapper.updateUser(params);
	}

	@Transactional
	public void delete(FtMap params) throws Exception {
		mapper.deleteUserAuth(params);
		mapper.deleteUser(params);
	}

}
