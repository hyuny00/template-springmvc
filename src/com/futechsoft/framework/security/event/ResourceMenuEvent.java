package com.futechsoft.framework.security.event;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.futechsoft.admin.auth.vo.AuthMenu;
import com.futechsoft.admin.menu.vo.Menu;

public class ResourceMenuEvent extends ApplicationEvent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, List<AuthMenu>> authMenuMap;
	private Map<String, List<Menu>> menuListMap;

	public ResourceMenuEvent(Object source, final Map<String, List<AuthMenu>> authMenuMap, Map<String, List<Menu>> menuListMap) {
		super(source);
		this.authMenuMap = authMenuMap;
		this.menuListMap = menuListMap;
	}

	public Map<String, List<AuthMenu>> getAuthMenuMap() {
		return authMenuMap;
	}

	public void setAuthMenuMap(Map<String, List<AuthMenu>> authMenuMap) {
		this.authMenuMap = authMenuMap;
	}

	public Map<String, List<Menu>> getMenuListMap() {
		return menuListMap;
	}

	public void setMenuListMap(Map<String, List<Menu>> menuListMap) {
		this.menuListMap = menuListMap;
	}

}
