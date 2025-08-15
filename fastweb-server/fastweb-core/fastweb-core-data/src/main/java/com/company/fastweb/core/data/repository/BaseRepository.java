package com.company.fastweb.core.data.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 基础数据访问接口
 * 
 * @param <T> 实体类型
 * @param <ID> 主键类型
 */
public interface BaseRepository<T, ID extends Serializable> {
    
    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return 保存后的实体
     */
    T save(T entity);
    
    /**
     * 批量保存实体
     *
     * @param entities 实体集合
     * @return 保存后的实体集合
     */
    List<T> saveAll(Collection<T> entities);
    
    /**
     * 根据ID查询实体
     *
     * @param id 主键ID
     * @return 实体对象，如果不存在则返回空
     */
    Optional<T> findById(ID id);
    
    /**
     * 根据ID集合查询实体列表
     *
     * @param ids ID集合
     * @return 实体列表
     */
    List<T> findAllById(Collection<ID> ids);
    
    /**
     * 查询所有实体
     *
     * @return 实体列表
     */
    List<T> findAll();
    
    /**
     * 查询实体总数
     *
     * @return 实体总数
     */
    long count();
    
    /**
     * 根据ID删除实体
     *
     * @param id 主键ID
     */
    void deleteById(ID id);
    
    /**
     * 删除实体
     *
     * @param entity 实体对象
     */
    void delete(T entity);
    
    /**
     * 批量删除实体
     *
     * @param entities 实体集合
     */
    void deleteAll(Collection<T> entities);
    
    /**
     * 删除所有实体
     */
    void deleteAll();
    
    /**
     * 判断实体是否存在
     *
     * @param id 主键ID
     * @return 如果存在返回true，否则返回false
     */
    boolean existsById(ID id);
}