package com.springBoot.repository;

import com.springBoot.entity.Man;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author chenkuan
 * @version v1.0
 * @desc
 * @date 2019/4/4 004 16:23
 */
@Repository
public interface ManRepository extends CrudRepository<Man, Long> {

}
