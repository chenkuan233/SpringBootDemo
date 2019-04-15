package com.springBoot.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 测试elasticsearch实体
 * @date 2019/4/11 011 11:18
 */
@Setter
@Getter
@Document(indexName = "item", type = "docs", shards = 1, replicas = 0)
public class Item {

	//@Id 注解必须是springframework包下的org.springframework.data.annotation.Id
	@Id
	private Long id;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String title; //标题

	@Field(type = FieldType.Keyword)
	private String category;// 分类

	@Field(type = FieldType.Keyword)
	private String brand; // 品牌

	@Field(type = FieldType.Double)
	private Double price; // 价格

	@Field(index = false, type = FieldType.Keyword)
	private String images; // 图片地址
}
