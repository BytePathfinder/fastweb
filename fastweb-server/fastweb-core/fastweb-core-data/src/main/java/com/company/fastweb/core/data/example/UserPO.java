package com.company.fastweb.core.data.example;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.fastweb.core.data.model.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体示例 - 演示如何继承BasePO
 *
 * @author FastWeb
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_user")
public class UserPO extends BasePO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;
}