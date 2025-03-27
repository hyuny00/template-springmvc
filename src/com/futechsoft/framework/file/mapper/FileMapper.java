package com.futechsoft.framework.file.mapper;

import java.util.List;

import com.futechsoft.framework.annotation.Mapper;
import com.futechsoft.framework.common.mapper.GenericMapper;
import com.futechsoft.framework.file.vo.FileInfoVo;
import com.futechsoft.framework.util.FtMap;

@Mapper("framework.file.mapper.FileMapper")
public interface FileMapper extends GenericMapper<FtMap> {

	FileInfoVo selectFileInfo(FtMap params) throws Exception;

	List<FtMap> selectFileList(FtMap params) throws Exception;


	void updateMergeDocId(FtMap params)throws Exception;

	void updateThumbNnailFileInfo(FileInfoVo fileInfoVo)throws Exception;



	FtMap selectFileDataInfo(FtMap params) throws Exception;

	FtMap selectFile(FtMap params) throws Exception;
}
