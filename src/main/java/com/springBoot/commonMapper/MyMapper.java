package com.springBoot.commonMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 通用Mapper实现
 * Mapper接口：基本的增、删、改、查方法
 * MySqlMapper：针对MySQL的额外补充接口，支持批量插入
 * @date 2019/3/4 004 20:21
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
