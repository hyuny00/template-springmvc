package com.futechsoft.admin.auth.mapper;

import java.util.List;

import com.futechsoft.admin.auth.vo.Auth;
import com.futechsoft.admin.auth.vo.AuthMenu;
import com.futechsoft.admin.auth.vo.Path;
import com.futechsoft.admin.auth.vo.PathAuth;
import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.common.mapper.GenericMapper;
import com.futechsoft.framework.util.FtMap;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author futech
 * @version $Revision$
 */
@Mapper("auth.mapper.AuthMapper")
public interface AuthMapper extends GenericMapper<FtMap> {

	public List<Auth> getAuthList(FtMap params);

	public List<Auth> getAuthCdList();

	public List<AuthMenu> getAuthMenuList(FtMap params);

	public List<Path> getPathList(FtMap params);

	public List<PathAuth> getPathAuthList(FtMap params);
	
	 
	public void insertAuth(FtMap params) throws Exception;
	public void updateAuth(FtMap params) throws Exception;
	public void insertPath(FtMap params) throws Exception;
	public void updatePath(FtMap params) throws Exception;
	
	
	
	public void deleteUserAuth(FtMap params) throws Exception;
	public void deleteAuthMenu(FtMap params) throws Exception;
	public void deleteAuth(FtMap params) throws Exception;
	public void deletePathAuthByAuthSeq(FtMap params) throws Exception;
	
	int getAuthCount(FtMap params) throws Exception;
	
	public void insertAuthMenu(FtMap params) throws Exception;
	
	public void updatePathOrd(FtMap params) throws Exception;
	
	
	
	public void deletePathAuthByPathSeq(FtMap params) throws Exception;
	public void deletePath(FtMap params) throws Exception;
	
 	
	public void insertPathAuth(FtMap params) throws Exception;
}
