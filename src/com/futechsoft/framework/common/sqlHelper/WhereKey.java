package com.futechsoft.framework.common.sqlHelper;

public class WhereKey {

	public String[] keys;

	public WhereKey(String... key) {
		this.keys = key;
	}

	public String[] getKeys() {
		return keys;
	}

}
