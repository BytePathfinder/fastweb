package com.company.fastweb.biz.user.mapper;

import com.company.fastweb.biz.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查找用户（包含角色和权限信息）
     * @param username 用户名
     * @return 用户信息
     */
    User findByUsername(@Param("username") String username);

    /**
     * 根据用户ID查找用户（包含角色和权限信息）
     * @param id 用户ID
     * @return 用户信息
     */
    User findById(@Param("id") Long id);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @return 用户信息
     */
    User findByPhone(@Param("phone") String phone);

    /**
     * 插入用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据用户名统计用户数量
     * @param username 用户名
     * @return 用户数量
     */
    int countByUsername(@Param("username") String username);

    /**
     * 根据邮箱统计用户数量
     * @param email 邮箱
     * @return 用户数量
     */
    int countByEmail(@Param("email") String email);

    /**
     * 根据手机号统计用户数量
     * @param phone 手机号
     * @return 用户数量
     */
    int countByPhone(@Param("phone") String phone);
}