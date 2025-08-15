package com.company.fastweb.core.data.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.fastweb.core.data.repository.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 基于 MyBatis-Plus 的 BaseRepository 实现
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @param <M> Mapper类型
 */
public class MyBatisBaseRepository<T, ID extends Serializable, M extends BaseMapper<T>> implements BaseRepository<T, ID> {
    
    /**
     * MyBatis-Plus 的 BaseMapper
     */
    protected final M baseMapper;
    
    /**
     * 实体类型
     */
    protected final Class<T> entityClass;
    
    /**
     * 构造函数
     *
     * @param baseMapper MyBatis-Plus 的 BaseMapper
     * @param entityClass 实体类型
     */
    public MyBatisBaseRepository(M baseMapper, Class<T> entityClass) {
        this.baseMapper = baseMapper;
        this.entityClass = entityClass;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T entity) {
        baseMapper.insert(entity);
        return entity;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> saveAll(Collection<T> entities) {
        List<T> result = new ArrayList<>(entities.size());
        for (T entity : entities) {
            baseMapper.insert(entity);
            result.add(entity);
        }
        return result;
    }
    
    @Override
    public Optional<T> findById(ID id) {
        T entity = baseMapper.selectById(id);
        return Optional.ofNullable(entity);
    }
    
    @Override
    public List<T> findAllById(Collection<ID> ids) {
        return baseMapper.selectBatchIds(ids);
    }
    
    @Override
    public List<T> findAll() {
        return baseMapper.selectList(new QueryWrapper<>());
    }
    
    @Override
    public long count() {
        return baseMapper.selectCount(new QueryWrapper<>());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(ID id) {
        baseMapper.deleteById(id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(T entity) {
        baseMapper.deleteById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Collection<T> entities) {
        for (T entity : entities) {
            baseMapper.deleteById(entity);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        baseMapper.delete(new QueryWrapper<>());
    }
    
    @Override
    public boolean existsById(ID id) {
        return baseMapper.selectById(id) != null;
    }
}