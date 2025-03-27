package com.futechsoft.admin.auth.vo;

public class PathAuth {

	private long pathAuthSeq;
	private long pathSeq;
	private long authSeq;
	private long upAuthSeq;
	private String authCd;
	private String authTypeCd;
	private String  authNm;
	private String path;

	private String selectYn;

	public long getPathAuthSeq() {
		return pathAuthSeq;
	}

	public void setPathAuthSeq(long pathAuthSeq) {
		this.pathAuthSeq = pathAuthSeq;
	}

	public long getPathSeq() {
		return pathSeq;
	}

	public void setPathSeq(long pathSeq) {
		this.pathSeq = pathSeq;
	}

	public long getAuthSeq() {
		return authSeq;
	}

	public void setAuthSeq(long authSeq) {
		this.authSeq = authSeq;
	}

	public long getUpAuthSeq() {
		return upAuthSeq;
	}

	public void setUpAuthSeq(long upAuthSeq) {
		this.upAuthSeq = upAuthSeq;
	}

	public String getAuthCd() {
		return authCd;
	}

	public void setAuthCd(String authCd) {
		this.authCd = authCd;
	}

	public String getAuthTypeCd() {
		return authTypeCd;
	}

	public void setAuthTypeCd(String authTypeCd) {
		this.authTypeCd = authTypeCd;
	}

	public String getAuthNm() {
		return authNm;
	}

	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSelectYn() {
		return selectYn;
	}

	public void setSelectYn(String selectYn) {
		this.selectYn = selectYn;
	}


}
