package com.company.fastweb.core.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 增强的基础Mapper接口
 * 在MyBatis-Plus BaseMapper基础上扩展常用方法
 * 借鉴RuoYi的BaseMapperPlus设计理念
 * 
 * @param <T> 实体类型
 */
public interface FastWebBaseMapper<T> extends BaseMapper<T> {
    
    /**
     * 批量插入（忽略null值）
     */
    int insertBatchSomeColumn(@Param(Constants.COLLECTION) Collection<T> entityList);
    
    /**
     * 批量更新（根据ID）
     */
    int updateBatchById(@Param(Constants.COLLECTION) Collection<T> entityList);
    
    /**
     * 批量删除（逻辑删除）
     */
    int deleteLogicByIds(@Param(Constants.COLLECTION) Collection<Long> idList);
    

    
    /**
     * 根据条件查询一条记录
     */
    T selectOneByCondition(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    
    /**
     * 根据条件查询记录数
     */
    long selectCountByCondition(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
}