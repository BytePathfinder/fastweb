package com.company.fastweb.core.data.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.List;

/**
 * 增强的BaseMapper
 *
 * @author FastWeb
 */
public interface BaseMapperX<T> extends BaseMapper<T> {

    /**
     * 分页查询
     */
    default IPage<T> selectPage(int current, int size) {
        return selectPage(new Page<>(current, size), null);
    }

    /**
     * 分页查询（带条件）
     */
    default IPage<T> selectPage(int current, int size, QueryWrapper<T> queryWrapper) {
        return selectPage(new Page<>(current, size), queryWrapper);
    }

    /**
     * 分页查询（Lambda条件）
     */
    default IPage<T> selectPage(int current, int size, LambdaQueryWrapper<T> lambdaQueryWrapper) {
        return selectPage(new Page<>(current, size), lambdaQueryWrapper);
    }

    /**
     * 查询单个对象（可能为null）
     */
    default T selectOneOpt(QueryWrapper<T> queryWrapper) {
        List<T> list = selectList(queryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("查询结果不唯一，期望1个，实际" + list.size() + "个");
        }
        return list.get(0);
    }

    /**
     * 查询单个对象（Lambda条件，可能为null）
     */
    default T selectOneOpt(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        List<T> list = selectList(lambdaQueryWrapper);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("查询结果不唯一，期望1个，实际" + list.size() + "个");
        }
        return list.get(0);
    }

    /**
     * 批量插入
     */
    default int insertBatch(Collection<T> entityList) {
        return insertBatch(entityList, 1000);
    }

    /**
     * 批量插入（指定批次大小）
     * <p>
     * 注意：这是模拟批量插入，实际使用Service层的saveBatch方法更高效
     */
    default int insertBatch(Collection<T> entityList, int batchSize) {
        if (entityList == null || entityList.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        List<T> list = entityList instanceof List ? (List<T>) entityList : 
            entityList.stream().toList();
        
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<T> batch = list.subList(i, end);
            for (T entity : batch) {
                count += insert(entity);
            }
        }
        return count;
    }

    /**
     * 根据ID批量删除
     */
    default int deleteBatchIds(Collection<?> idList) {
        if (idList == null || idList.isEmpty()) {
            return 0;
        }
        return BaseMapper.super.deleteBatchIds(idList);
    }

    /**
     * 物理删除（真删除）
     */
    default int deletePhysically(QueryWrapper<T> queryWrapper) {
        return delete(queryWrapper);
    }

    /**
     * 物理删除（Lambda条件）
     */
    default int deletePhysically(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        return delete(lambdaQueryWrapper);
    }
}