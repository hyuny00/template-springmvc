package com.futechsoft.framework.common.sqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.text.CaseUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import com.futechsoft.framework.util.FtMap;

public class SqlProvider {

	private static String getCamelCase(String s) {

		return CaseUtils.toCamelCase(s, false, new char[] { '_' });
	}
	/*
	 * private static String getSnakeCase(String s) { String ret =
	 * s.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])",
	 * "$1_$2"); return ret.toLowerCase(); }
	 */

	public String selectByKey(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null ) {
			throw new Exception();
		}

		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);

				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}
			}
		};

		return sql.toString();
	}

	public String selectCountByKey(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null ) {
			throw new Exception();
		}

		SQL sql = new SQL() {
			{
				SELECT("count(1) as cnt");
				FROM(tableName);
				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}
			}
		};

		return sql.toString();
	}

	public String selectListByKey(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("whereKey") final WhereKey whereKey, @Param("orderBy") final OrderBy orderBy) throws Exception {

		if (whereKey == null ) {
			throw new Exception();
		}

		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM(tableName);
				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}

				if (orderBy.getOrderBy() != null) {
					ORDER_BY(orderBy.getOrderBy());
				}

			}
		};
		return sql.toString();

	}

	public String delete(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null || whereKey.getKeys().length == 0) {
			throw new Exception();
		}

		SQL sql = new SQL() {
			{
				DELETE_FROM(tableName);
				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}
			}
		};

		return sql.toString();

	}

	public String deleteAll(@Param("tableName") final String tableName) {

		SQL sql = new SQL() {
			{
				DELETE_FROM(tableName);
			}
		};
		return sql.toString();

	}

	public String updateAll(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("columnList") final List<FtMap> columnList, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null || whereKey.getKeys().length == 0) {
			throw new Exception();
		}

		Map<String, String> tblMap = t.getTblMap(t, columnList);

		List<String> keyList = new ArrayList<String>();

		for (String column : whereKey.getKeys()) {
			keyList.add(column.toLowerCase());
		}

		SQL sql = new SQL() {
			{
				UPDATE(tableName);

				for (Entry<String, String> entry : tblMap.entrySet()) {

					if (entry.getKey().equals(Column.RGSTP_NO))
						continue;

					String value = "#{t." + getCamelCase(entry.getKey()) + "}";
					SET(entry.getKey() + "=" + value);
				}

				SET(Column.UPDT_DTTM + "=" + Column.DATE);

				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}

			}
		};
		return sql.toString();
	}

	public String updateColumn(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("updateColumn") final Column updateColumn, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null || whereKey.getKeys().length == 0) {
			throw new Exception();
		}

		Map<String, String> tblMap = t.getTblMap(t);

		List<String> keyList = new ArrayList<String>();

		for (String column : whereKey.getKeys()) {
			keyList.add(column.toLowerCase());
		}

		List<String> columnList = new ArrayList<String>();

		for (String column : updateColumn.getColumns()) {
			columnList.add(column.toLowerCase());
		}

		SQL sql = new SQL() {
			{
				UPDATE(tableName);

				for (Entry<String, String> entry : tblMap.entrySet()) {

					if (entry.getKey().equals(Column.RGSTP_NO))
						continue;
					if (columnList.contains(entry.getKey())) {
						SET(entry.getKey() + "=" + "#{t." + getCamelCase(entry.getKey()) + "}");
					}
				}

				SET(Column.UPDT_DTTM + "=" + Column.DATE);

				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}

			}
		};

		return sql.toString();
	}

	public String updateExcludeColumn(@Param("tableName") final String tableName, @Param("t") final FtMap t, @Param("columnList") final List<FtMap> columnList, @Param("excludeColumn") final Column excludeColumn, @Param("whereKey") final WhereKey whereKey) throws Exception {

		if (whereKey == null || whereKey.getKeys().length == 0) {
			throw new Exception();
		}

		Map<String, String> tblMap = t.getTblMap(t, columnList);

		List<String> keyList = new ArrayList<String>();

		for (String column : whereKey.getKeys()) {
			keyList.add(column.toLowerCase());
		}

		List<String> excludeColumnList = new ArrayList<String>();

		for (String column : excludeColumn.getColumns()) {
			excludeColumnList.add(column.toLowerCase());
		}

		SQL sql = new SQL() {
			{
				UPDATE(tableName);

				for (Entry<String, String> entry : tblMap.entrySet()) {

					if (entry.getKey().equals(Column.RGSTP_NO))
						continue;

					if (!keyList.contains(entry.getKey()) && !excludeColumnList.contains(entry.getKey())) {
						SET(entry.getKey() + "=" + "#{t." + getCamelCase(entry.getKey()) + "}");
					}
				}
				if (!excludeColumnList.contains(Column.UPDT_DTTM)) {
					SET(Column.UPDT_DTTM + "=" + Column.DATE);
				}

				for (String column : whereKey.getKeys()) {
					String value = "#{t." + getCamelCase(column) + "}";
					WHERE(column + "=" + value);
				}

			}
		};
		return sql.toString();
	}

}
