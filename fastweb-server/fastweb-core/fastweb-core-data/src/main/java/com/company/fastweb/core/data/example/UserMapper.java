package com.company.fastweb.core.data.example;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.fastweb.core.data.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper示例 - 演示如何继承BaseMapperX
 *
 * @author FastWeb
 */
@Mapper
public interface UserMapper extends BaseMapperX<UserPO> {
    
    // BaseMapperX已经提供了常用的CRUD方法
    // 这里可以添加自定义的查询方法
}