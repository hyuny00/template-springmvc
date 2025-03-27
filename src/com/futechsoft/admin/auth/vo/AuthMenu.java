package com.futechsoft.admin.auth.vo;

public class AuthMenu {

	private long authMenuSeq;

	private long menuSeq;
	private long upMenuSeq;
	private String menuUrl;
	private String menuNm;
	private String menuTypeCd;
	private int menuOrd;
	private long authSeq;
	private String authCd;

	private String openYn;

	private long topMenuSeq;

	private String selectYn;

	private int subMenuCnt;

	//하위메뉴의 권한이 지정되지않은 카운트
	private int noAuthCount;

	public long getAuthMenuSeq() {
		return authMenuSeq;
	}

	public void setAuthMenuSeq(long authMenuSeq) {
		this.authMenuSeq = authMenuSeq;

	}

	public long getAuthSeq() {
		return authSeq;
	}

	public void setAuthSeq(long authSeq) {
		this.authSeq = authSeq;
	}

	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	public long getMenuSeq() {
		return menuSeq;
	}

	public void setMenuSeq(long menuSeq) {
		this.menuSeq = menuSeq;
	}

	public long getUpMenuSeq() {
		return upMenuSeq;
	}

	public void setUpMenuCd(long upMenuSeq) {
		this.upMenuSeq = upMenuSeq;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	public String getMenuTypeCd() {
		return menuTypeCd;
	}

	public void setMenuTypeCd(String menuTypeCd) {
		this.menuTypeCd = menuTypeCd;
	}

	public int getMenuOrd() {
		return menuOrd;
	}

	public void setMenuOrd(int menuOrd) {
		this.menuOrd = menuOrd;
	}

	public long getTopMenuSeq() {
		return topMenuSeq;
	}

	public void setTopMenuSeq(long topMenuSeq) {
		this.topMenuSeq = topMenuSeq;
	}

	public void setUpMenuSeq(long upMenuSeq) {
		this.upMenuSeq = upMenuSeq;
	}

	public String getOpenYn() {
		return openYn;
	}

	public void setOpenYn(String openYn) {
		this.openYn = openYn;
	}

	public String getSelectYn() {
		return selectYn;
	}

	public void setSelectYn(String selectYn) {
		this.selectYn = selectYn;
	}

	public int getSubMenuCnt() {
		return subMenuCnt;
	}

	public void setSubMenuCnt(int subMenuCnt) {
		this.subMenuCnt = subMenuCnt;
	}

	public int getNoAuthCount() {
		return noAuthCount;
	}

	public void setNoAuthCount(int noAuthCount) {
		this.noAuthCount = noAuthCount;
	}

}
