package com.company.fastweb.core.common.constant;

/**
 * 通用常量定义
 *
 * @author FastWeb
 */
public interface CommonConstants {

    /**
     * 成功标记
     */
    String SUCCESS = "SUCCESS";

    /**
     * 失败标记
     */
    String FAIL = "FAIL";

    /**
     * 默认成功消息
     */
    String SUCCESS_MSG = "操作成功";

    /**
     * 默认失败消息
     */
    String FAIL_MSG = "操作失败";

    /**
     * 默认null消息
     */
    String NULL_MSG = "暂无数据";

    /**
     * 默认分页大小
     */
    Integer DEFAULT_PAGE_SIZE = 20;

    /**
     * 最大分页大小
     */
    Integer MAX_PAGE_SIZE = 1000;

    /**
     * 树根节点ID
     */
    Long TREE_ROOT_ID = 0L;

    /**
     * 菜单树根节点ID
     */
    Long MENU_TREE_ROOT_ID = 0L;

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 默认租户ID
     */
    String DEFAULT_TENANT_ID = "000000";

    /**
     * 超级管理员角色编码
     */
    String SUPER_ADMIN_ROLE_CODE = "super_admin";

    /**
     * 管理员角色编码
     */
    String ADMIN_ROLE_CODE = "admin";

    /**
     * 普通用户角色编码
     */
    String USER_ROLE_CODE = "user";

    /**
     * 删除标记 - 已删除
     */
    Integer DELETED = 1;

    /**
     * 删除标记 - 未删除
     */
    Integer NOT_DELETED = 0;

    /**
     * 状态 - 启用
     */
    Integer STATUS_ENABLED = 1;

    /**
     * 状态 - 禁用
     */
    Integer STATUS_DISABLED = 0;

    /**
     * 是否标记 - 是
     */
    Integer YES = 1;

    /**
     * 是否标记 - 否
     */
    Integer NO = 0;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 系统用户ID
     */
    Long SYSTEM_USER_ID = 1L;

    /**
     * 匿名用户ID
     */
    Long ANONYMOUS_USER_ID = -1L;
}