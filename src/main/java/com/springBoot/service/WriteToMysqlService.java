package com.springBoot.service;

import com.springBoot.entity.Man;

import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/4 004 15:31
 */
public interface WriteToMysqlService {
	void writeMan(List<Man> manList);

	void writeManDB2(List<Man> manList);
}
