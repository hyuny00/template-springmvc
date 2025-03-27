package com.futechsoft.framework.security.event;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.ConfigAttribute;

public class SecurityMetaDataSourceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private LinkedHashMap<String, List<ConfigAttribute>> requestMap;
	private HashMap<String, String> pathAuthMap;


	public SecurityMetaDataSourceEvent(Object source, final LinkedHashMap<String, List<ConfigAttribute>> requestMap, final HashMap<String, String> pathAuthMap) {
		super(source);
		this.requestMap = requestMap;
		this.pathAuthMap = pathAuthMap;
	}

	public LinkedHashMap<String, List<ConfigAttribute>> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(LinkedHashMap<String, List<ConfigAttribute>> requestMap) {
		this.requestMap = requestMap;
	}

	public HashMap<String, String> getPathAuthMap() {
		return pathAuthMap;
	}


}
