package com.futechsoft.framework.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.common.sqlHelper.Column;
import com.futechsoft.framework.common.sqlHelper.InsertParam;
import com.futechsoft.framework.common.sqlHelper.OrderBy;
import com.futechsoft.framework.common.sqlHelper.SqlProvider;
import com.futechsoft.framework.common.sqlHelper.WhereKey;
import com.futechsoft.framework.util.FtMap;

@Mapper("framework.common.mapper.GenericMapper")
public interface GenericMapper<T> {

	public List<T> getColumnList(@Param("tableName") String tableName) throws Exception;

	public int insertByMaxPk(InsertParam insertParam) throws Exception;

	public void insert(InsertParam insertParam) throws Exception;

	@SelectProvider(type = SqlProvider.class, method = "selectByKey")
	public T selectByKey(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("whereKey") WhereKey whereKey) throws Exception;

	@SelectProvider(type = SqlProvider.class, method = "selectCountByKey")
	public int selectCountByKey(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("whereKey") WhereKey whereKey) throws Exception;

	@SelectProvider(type = SqlProvider.class, method = "selectListByKey")
	public List<T> selectListByKey(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("whereKey") WhereKey whereKey, @Param("orderBy") OrderBy orderBy) throws Exception;

	@DeleteProvider(type = SqlProvider.class, method = "delete")
	public void delete(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("whereKey") WhereKey whereKey) throws Exception;

	@DeleteProvider(type = SqlProvider.class, method = "deleteAll")
	public void deleteAll(@Param("tableName") String tableName) throws Exception;

	@UpdateProvider(type = SqlProvider.class, method = "updateAll")
	public void updateAll(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("columnList") List<FtMap> columnList, @Param("whereKey") WhereKey whereKey) throws Exception;

	@UpdateProvider(type = SqlProvider.class, method = "updateColumn")
	public void updateColumn(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("updateColumn") Column updateColumn, @Param("whereKey") WhereKey whereKey) throws Exception;

	@UpdateProvider(type = SqlProvider.class, method = "updateExcludeColumn")
	public void updateExcludeColumn(@Param("tableName") String tableName, @Param("t") FtMap t, @Param("columnList") List<FtMap> columnList, @Param("excludeColumn") Column excludeColumn, @Param("whereKey") WhereKey whereKey) throws Exception;

}
