package com.futechsoft.framework.common.sqlHelper;

import java.util.List;
import java.util.Map;

import com.futechsoft.framework.util.FtMap;

public class InsertParam {

	private String tableName;
	private String keyColumn;
	private Map<String, String> tblMap;
	private List<FtMap> columnList;

	/*
	 * public InsertParam(String tableName, FtMap param, String keyColumn) throws
	 * Exception { this.tableName=tableName; this.tblMap=param.getTblMap(param);
	 *
	 * tblMap.remove(Column.UPDTP_NO);
	 *
	 * this.keyColumn=keyColumn;
	 *
	 * }
	 */
	public InsertParam(String tableName, FtMap param, List<FtMap> columnList, String keyColumn) throws Exception {
		this.tableName = tableName;
		this.tblMap = param.getTblMap(param, columnList);

		tblMap.remove(Column.UPDTP_NO);

		this.keyColumn = keyColumn;
		this.columnList = columnList;

	}

	public List<FtMap> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<FtMap> columnList) {
		this.columnList = columnList;
	}

	private int id;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}

	public Map<String, String> getTblMap() {
		return tblMap;
	}

	public void setTblMap(Map<String, String> tblMap) {
		this.tblMap = tblMap;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
