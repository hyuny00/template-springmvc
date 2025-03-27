
package com.futechsoft.framework.security.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.futechsoft.admin.auth.mapper.AuthMapper;
import com.futechsoft.admin.auth.vo.Auth;
import com.futechsoft.admin.auth.vo.AuthMenu;
import com.futechsoft.admin.menu.vo.Menu;
import com.futechsoft.framework.security.mapper.SecurityMapper;
import com.futechsoft.framework.exception.BizException;
import com.futechsoft.framework.security.event.ResourceMenuEvent;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author futech
 * @version $Revision$
 */
@Component("framework.menu.service.ResourceMenuService")
public class ResourceMenuService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceMenuService.class);

	@Autowired
	private ApplicationEventPublisher publisher;

	@Resource(name = "framework.security.mapper.SecurityMapper")
	private SecurityMapper securityMapper;

	//@Resource(name = "admin.logging.menu.service.MenuLogService")
//	private MenuLogService menuLogService;


	@Resource(name = "auth.mapper.AuthMapper")
	private AuthMapper authMapper;

	Map<String, List<AuthMenu>> authMenuMap = null;
	Map<String, List<Menu>> menuListMap = null;

	@PostConstruct
	public void init() throws Exception {

		authMenuMap = new HashMap<String, List<AuthMenu>>();

		List<Auth> list = authMapper.getAuthCdList();
		for (Auth auth : list) {
			List<AuthMenu> menuList = securityMapper.getAuthMenu(auth.getAuthSeq());
			authMenuMap.put(auth.getAuthCd().toUpperCase(), menuList);
		}

		menuListMap = new HashMap<String, List<Menu>>();
		List<Menu> topMenuList = securityMapper.getTopMenuList();
		menuListMap.put("topMenuList", topMenuList);

		List<Menu> subMenuList = null;
		for (Menu topMenu : topMenuList) {
			subMenuList = securityMapper.getSubMenuList(topMenu.getMenuSeq());
			menuListMap.put(String.valueOf(topMenu.getMenuSeq()), subMenuList);
		}

		publisher.publishEvent(new ResourceMenuEvent(this, authMenuMap, menuListMap));
	}

}



