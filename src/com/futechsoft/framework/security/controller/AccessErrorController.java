package com.futechsoft.framework.security.controller;

import java.util.HashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.futechsoft.framework.security.vo.CustomUserDetails;

@Controller
public class AccessErrorController {


	@RequestMapping(value = "/accessdenied")
	public String accessdenied(@RequestParam HashMap<String, Object> map) throws Exception {
		return "framework/error/accessError";
	}

	@RequestMapping(value = "/invalidCsrf")
	public String csrfAccessdenied(@RequestParam HashMap<String, Object> map) throws Exception {
		return "framework/error/csrfError";
	}
	
	

}
