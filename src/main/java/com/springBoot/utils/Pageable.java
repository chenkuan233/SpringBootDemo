package com.springBoot.utils;

import java.io.Serializable;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 分页参数实体类
 * @date 2019/4/22 022 11:43
 */
public class Pageable implements Serializable {

	private static final long serialVersionUID = 1L;

	// 默认页号
	private static final Integer defaultPageNum = 1;
	// 默认每页行数
	private static final Integer defaultPageSize = 10;

	// 页号
	private Integer pageNum;
	// 每页行数
	private Integer pageSize;

	public Integer getPageNum() {
		return pageNum == null ? defaultPageNum : pageNum;
	}

	public Integer getPageSize() {
		return pageSize == null ? defaultPageSize : pageSize;
	}
}
