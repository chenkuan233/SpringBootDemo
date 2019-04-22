package com.springBoot.utils.annotation;

import java.lang.annotation.*;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 自定义分页查询注解
 * @date 2019/4/22 022 10:24
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageQuery {
}
