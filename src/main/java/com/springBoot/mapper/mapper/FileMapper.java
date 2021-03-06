package com.springBoot.mapper.mapper;

import com.springBoot.entity.FileInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/27 027 12:55
 */
public interface FileMapper {

	List<FileInfo> findAllFile();

	void deleteFile(@Param("id") Long id);

	void saveFile(@Param("file") FileInfo file);

}
