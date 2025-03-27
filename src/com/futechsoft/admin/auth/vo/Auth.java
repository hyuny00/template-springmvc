package com.futechsoft.admin.auth.vo;

public class Auth {

	private long authSeq;
	private long upAuthSeq;
	private String authCd;
	private String authNm;
	private String upAuthNm;
	private String authTypeCd;
	private String useYn;
	private int subAuthCnt;

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

	public String getAuthNm() {
		return authNm;
	}

	public void setAuthNm(String authNm) {
		this.authNm = authNm;
	}

	public String getUpAuthNm() {
		return upAuthNm;
	}

	public void setUpAuthNm(String upAuthNm) {
		this.upAuthNm = upAuthNm;
	}

	public String getAuthTypeCd() {
		return authTypeCd;
	}

	public void setAuthTypeCd(String authTypeCd) {
		this.authTypeCd = authTypeCd;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public int getSubAuthCnt() {
		return subAuthCnt;
	}

	public void setSubAuthCnt(int subAuthCnt) {
		this.subAuthCnt = subAuthCnt;
	}

}
