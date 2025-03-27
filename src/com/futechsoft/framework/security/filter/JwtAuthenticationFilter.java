package com.futechsoft.framework.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futechsoft.framework.security.auth.CustomUserDetailsService;
import com.futechsoft.framework.security.auth.JwtTokenProvider;
import com.futechsoft.framework.security.vo.CustomUserDetails;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
    private  JwtTokenProvider jwtTokenProvider;

    
	@Autowired
    private  CustomUserDetailsService userDetailsService;

   

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. JWT 가져오기
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;

        // Authorization 헤더 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        // Authorization 헤더가 없으면 쿠키에서 가져오기
        else if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }
        // 2. JWT가 없으면 다음 필터로 이동
        if (jwtToken == null) { // JWT가 없을 때만 바로 다음 필터로 진행
            filterChain.doFilter(request, response);
            return;
        }


        // 3. JWT 검증 및 사용자 정보 설정
        String username =  jwtTokenProvider.getUsername(jwtToken); // JWT에서 사용자 정보 추출
        
        if (username != null) {
        	CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(username);
        	
            if (jwtTokenProvider.isValidToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
            }
        }

        // 4. 다음 필터 호출
        filterChain.doFilter(request, response);
    }
}
