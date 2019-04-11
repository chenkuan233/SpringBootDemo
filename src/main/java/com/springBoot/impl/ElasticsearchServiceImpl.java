package com.springBoot.impl;

import com.springBoot.entity.Item;
import com.springBoot.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 测试Elasticsearch
 * @date 2019/4/11 011 11:22
 */
@Service("elasticsearchService")
public class ElasticsearchServiceImpl implements ElasticsearchService {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public void createIndex() {
		elasticsearchTemplate.createIndex(Item.class);

	}
}
