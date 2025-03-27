package com.futechsoft.framework.security.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.futechsoft.admin.user.vo.UserAuth;
import com.futechsoft.framework.common.constant.AuthConstant;

public class CustomUserDetails implements UserDetails {

	public CustomUserDetails() {
	}

	private static final long serialVersionUID = 1L;

	private String userNo;
	private String userId;
	private String userNm;
	private String userPwd;
	private String userEmail;
	private String miniCd;
	private String dptCd;
	private String onnaraId;
	private String areumteoId;
	private int loginFailCnt;

	public int getLoginFailCnt() {
		return loginFailCnt;
	}

	public void setLoginFailCnt(int loginFailCnt) {
		this.loginFailCnt = loginFailCnt;
	}

	private boolean isEnabled;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;

	private List<UserAuth> userAuthList;

	public String getUserNo(){
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getPassword() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public void setUserAuthList(List<UserAuth> userAuthList) {
		this.userAuthList = userAuthList;
	}

	public List<UserAuth> getUserAuthList() {
		return userAuthList;
	}

	public Collection<GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();


		for (UserAuth userAuth : userAuthList) {

			if(userAuth.getAuthCd()!=null) {
				if(!userAuth.getAuthCd().startsWith((AuthConstant.ROLE_PREFIX))){
					authorities.add(new SimpleGrantedAuthority(AuthConstant.ROLE_PREFIX + userAuth.getAuthCd()));
				}else {
					authorities.add(new SimpleGrantedAuthority(userAuth.getAuthCd()));
				}
			}
		}
/*
		if(authorities.size()==0) {
			authorities.add(new SimpleGrantedAuthority(AuthConstant.ROLE_ANONYMOUS));
		}
*/
		return authorities;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CustomUserDetails) {
			return userId.equals(((CustomUserDetails) obj).getUsername());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}

	public String getDptCd() {
		return dptCd;
	}

	public void setDptCd(String dptCd) {
		this.dptCd = dptCd;
	}

	public String getMiniCd() {
		return miniCd;
	}

	public void setMiniCd(String miniCd) {
		this.miniCd = miniCd;
	}

	public String getOnnaraId() {
		return onnaraId;
	}

	public void setOnnaraId(String onnaraId) {
		this.onnaraId = onnaraId;
	}

	public String getAreumteoId() {
		return areumteoId;
	}

	public void setAreumteoId(String areumteoId) {
		this.areumteoId = areumteoId;
	}


}
