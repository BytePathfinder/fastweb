-- =============================================
-- 基于RuoYi-Vue-Plus/Pro等优秀开源框架改进的认证鉴权表设计
-- 增加了租户、OAuth2、API接口权限、数据权限等现代化功能
-- =============================================

-- ----------------------------
-- 1. 租户表（多租户支持）
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '租户编号',
    `tenant_id` VARCHAR(20) NOT NULL COMMENT '租户编号',
    `contact_user_name` VARCHAR(30) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(500) DEFAULT NULL COMMENT '联系电话',
    `company_name` VARCHAR(30) NOT NULL COMMENT '企业名称',
    `license_number` VARCHAR(30) DEFAULT NULL COMMENT '统一社会信用代码',
    `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
    `intro` VARCHAR(500) DEFAULT NULL COMMENT '企业简介',
    `domain` VARCHAR(256) DEFAULT NULL COMMENT '域名',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `package_id` BIGINT NOT NULL COMMENT '租户套餐编号',
    `expire_time` DATETIME NOT NULL COMMENT '过期时间',
    `account_count` INT NOT NULL COMMENT '账号数量',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '租户状态（0正常 1停用）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`),
    KEY `idx_status` (`status`),
    KEY `idx_package_id` (`package_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

-- ----------------------------
-- 2. 租户套餐表
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '套餐编号',
    `name` VARCHAR(30) NOT NULL COMMENT '套餐名',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '租户套餐状态（0正常 1停用）',
    `remark` VARCHAR(256) DEFAULT '' COMMENT '备注',
    `menu_ids` VARCHAR(4096) NOT NULL COMMENT '关联的菜单编号',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户套餐表';

-- ----------------------------
-- 3. 用户信息表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    `username` VARCHAR(30) NOT NULL COMMENT '用户账号',
    `password` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码',
    `nickname` VARCHAR(30) NOT NULL COMMENT '用户昵称',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
    `post_ids` VARCHAR(255) DEFAULT NULL COMMENT '岗位编号数组',
    `email` VARCHAR(50) DEFAULT '' COMMENT '用户邮箱',
    `mobile` VARCHAR(11) DEFAULT '' COMMENT '手机号码',
    `sex` TINYINT DEFAULT 0 COMMENT '用户性别（0男 1女 2未知）',
    `avatar` VARCHAR(512) DEFAULT '' COMMENT '头像地址',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '帐号状态（0正常 1停用）',
    `login_ip` VARCHAR(50) DEFAULT '' COMMENT '最后登录IP',
    `login_date` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`, `update_time`, `tenant_id`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- ----------------------------
-- 4. 部门表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门id',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    `name` VARCHAR(30) NOT NULL COMMENT '部门名称',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父部门id',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `leader_user_id` BIGINT DEFAULT NULL COMMENT '负责人',
    `phone` VARCHAR(11) DEFAULT NULL COMMENT '联系电话',
    `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL COMMENT '部门状态（0正常 1停用）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- ----------------------------
-- 5. 岗位信息表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    `code` VARCHAR(64) NOT NULL COMMENT '岗位编码',
    `name` VARCHAR(50) NOT NULL COMMENT '岗位名称',
    `sort` INT NOT NULL COMMENT '显示顺序',
    `status` TINYINT NOT NULL COMMENT '状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`tenant_id`, `code`, `update_time`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位信息表';

-- ----------------------------
-- 6. 角色信息表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    `name` VARCHAR(30) NOT NULL COMMENT '角色名称',
    `code` VARCHAR(100) NOT NULL COMMENT '角色权限字符串',
    `sort` INT NOT NULL COMMENT '显示顺序',
    `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）',
    `data_scope_dept_ids` VARCHAR(500) DEFAULT '' COMMENT '数据范围(指定部门数组)',
    `status` TINYINT NOT NULL COMMENT '角色状态（0正常 1停用）',
    `type` TINYINT NOT NULL COMMENT '角色类型（1内置角色 2自定义角色）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`tenant_id`, `code`, `update_time`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- ----------------------------
-- 7. 菜单权限表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `permission` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '权限标识',
    `type` TINYINT NOT NULL COMMENT '菜单类型（1目录 2菜单 3按钮）',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `path` VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    `icon` VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `component_name` VARCHAR(255) DEFAULT NULL COMMENT '组件名',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '菜单状态（0正常 1停用）',
    `visible` BIT NOT NULL DEFAULT b'1' COMMENT '是否可见',
    `keep_alive` BIT NOT NULL DEFAULT b'1' COMMENT '是否缓存',
    `always_show` BIT NOT NULL DEFAULT b'1' COMMENT '是否总是显示',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- ----------------------------
-- 8. 用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增编号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户和角色关联表';

-- ----------------------------
-- 9. 角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增编号',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色和菜单关联表';

-- ----------------------------
-- 10. OAuth2授权表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth2_authorization`;
CREATE TABLE `sys_oauth2_authorization` (
    `id` VARCHAR(100) NOT NULL COMMENT '编号',
    `registered_client_id` VARCHAR(100) NOT NULL COMMENT '客户端编号',
    `principal_name` VARCHAR(200) NOT NULL COMMENT '用户名',
    `authorization_grant_type` VARCHAR(100) NOT NULL COMMENT '授权许可类型',
    `authorized_scopes` TEXT DEFAULT NULL COMMENT '授权范围',
    `attributes` TEXT DEFAULT NULL COMMENT '用户属性',
    `state` VARCHAR(500) DEFAULT NULL COMMENT '状态',
    `authorization_code_value` TEXT DEFAULT NULL COMMENT '授权码',
    `authorization_code_issued_at` TIMESTAMP DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_expires_at` TIMESTAMP DEFAULT NULL COMMENT '授权码过期时间',
    `authorization_code_metadata` TEXT DEFAULT NULL COMMENT '授权码元数据',
    `access_token_value` TEXT DEFAULT NULL COMMENT '访问令牌',
    `access_token_issued_at` TIMESTAMP DEFAULT NULL COMMENT '访问令牌签发时间',
    `access_token_expires_at` TIMESTAMP DEFAULT NULL COMMENT '访问令牌过期时间',
    `access_token_metadata` TEXT DEFAULT NULL COMMENT '访问令牌元数据',
    `access_token_type` VARCHAR(100) DEFAULT NULL COMMENT '访问令牌类型',
    `access_token_scopes` TEXT DEFAULT NULL COMMENT '访问令牌范围',
    `oidc_id_token_value` TEXT DEFAULT NULL COMMENT 'OpenID Connect ID Token',
    `oidc_id_token_issued_at` TIMESTAMP DEFAULT NULL COMMENT 'OpenID Connect ID Token签发时间',
    `oidc_id_token_expires_at` TIMESTAMP DEFAULT NULL COMMENT 'OpenID Connect ID Token过期时间',
    `oidc_id_token_metadata` TEXT DEFAULT NULL COMMENT 'OpenID Connect ID Token元数据',
    `refresh_token_value` TEXT DEFAULT NULL COMMENT '刷新令牌',
    `refresh_token_issued_at` TIMESTAMP DEFAULT NULL COMMENT '刷新令牌签发时间',
    `refresh_token_expires_at` TIMESTAMP DEFAULT NULL COMMENT '刷新令牌过期时间',
    `refresh_token_metadata` TEXT DEFAULT NULL COMMENT '刷新令牌元数据',
    `user_code_value` TEXT DEFAULT NULL COMMENT '用户码',
    `user_code_issued_at` TIMESTAMP DEFAULT NULL COMMENT '用户码签发时间',
    `user_code_expires_at` TIMESTAMP DEFAULT NULL COMMENT '用户码过期时间',
    `user_code_metadata` TEXT DEFAULT NULL COMMENT '用户码元数据',
    `device_code_value` TEXT DEFAULT NULL COMMENT '设备码',
    `device_code_issued_at` TIMESTAMP DEFAULT NULL COMMENT '设备码签发时间',
    `device_code_expires_at` TIMESTAMP DEFAULT NULL COMMENT '设备码过期时间',
    `device_code_metadata` TEXT DEFAULT NULL COMMENT '设备码元数据',
    PRIMARY KEY (`id`),
    KEY `idx_registered_client_id` (`registered_client_id`),
    KEY `idx_principal_name` (`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权表';

-- ----------------------------
-- 11. OAuth2授权同意表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth2_authorization_consent`;
CREATE TABLE `sys_oauth2_authorization_consent` (
    `registered_client_id` VARCHAR(100) NOT NULL COMMENT '客户端编号',
    `principal_name` VARCHAR(200) NOT NULL COMMENT '用户名',
    `authorities` VARCHAR(1000) NOT NULL COMMENT '权限',
    PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2授权同意表';

-- ----------------------------
-- 12. OAuth2客户端表
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth2_registered_client`;
CREATE TABLE `sys_oauth2_registered_client` (
    `id` VARCHAR(100) NOT NULL COMMENT '编号',
    `client_id` VARCHAR(100) NOT NULL COMMENT '客户端编号',
    `client_id_issued_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端编号签发时间',
    `client_secret` VARCHAR(200) DEFAULT NULL COMMENT '客户端密钥',
    `client_secret_expires_at` TIMESTAMP DEFAULT NULL COMMENT '客户端密钥过期时间',
    `client_name` VARCHAR(200) NOT NULL COMMENT '客户端名',
    `client_authentication_methods` VARCHAR(1000) NOT NULL COMMENT '客户端认证方式',
    `authorization_grant_types` VARCHAR(1000) NOT NULL COMMENT '授权许可类型',
    `redirect_uris` TEXT DEFAULT NULL COMMENT '重定向地址',
    `post_logout_redirect_uris` TEXT DEFAULT NULL COMMENT '登出重定向地址',
    `scopes` VARCHAR(1000) NOT NULL COMMENT '授权范围',
    `client_settings` VARCHAR(2000) NOT NULL COMMENT '客户端配置',
    `token_settings` VARCHAR(2000) NOT NULL COMMENT '令牌配置',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2客户端表';

-- ----------------------------
-- 13. API接口表
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(50) NOT NULL COMMENT '接口名称',
    `path` VARCHAR(500) NOT NULL COMMENT '接口路径',
    `method` VARCHAR(16) NOT NULL COMMENT '请求方法',
    `tag` VARCHAR(50) NOT NULL COMMENT '接口分组',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '接口描述',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '接口状态（0正常 1停用）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_path_method` (`path`, `method`, `update_time`),
    KEY `idx_tag` (`tag`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API接口表';

-- ----------------------------
-- 14. 用户会话表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_session`;
CREATE TABLE `sys_user_session` (
    `id` VARCHAR(32) NOT NULL COMMENT '会话编号',
    `user_id` BIGINT NOT NULL COMMENT '用户编号',
    `user_type` TINYINT NOT NULL DEFAULT 1 COMMENT '用户类型',
    `session_timeout` DATETIME NOT NULL COMMENT '会话超时时间',
    `username` VARCHAR(30) NOT NULL COMMENT '用户账号',
    `user_ip` VARCHAR(50) NOT NULL COMMENT '用户 IP',
    `user_agent` VARCHAR(512) NOT NULL COMMENT '浏览器 UA',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_session_timeout` (`session_timeout`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话表';

-- ----------------------------
-- 15. 操作日志记录（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    `trace_id` VARCHAR(64) DEFAULT '' COMMENT '链路追踪编号',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户编号',
    `user_type` TINYINT DEFAULT 0 COMMENT '用户类型',
    `type` BIGINT NOT NULL COMMENT '操作分类',
    `sub_type` BIGINT NOT NULL COMMENT '操作明细',
    `biz_id` BIGINT DEFAULT NULL COMMENT '业务编号',
    `action` VARCHAR(2000) DEFAULT '' COMMENT '操作内容',
    `extra` VARCHAR(2000) DEFAULT '' COMMENT '拓展字段',
    `request_method` VARCHAR(16) DEFAULT '' COMMENT '请求方法名',
    `request_url` VARCHAR(512) DEFAULT '' COMMENT '请求地址',
    `user_ip` VARCHAR(50) DEFAULT NULL COMMENT '用户 IP',
    `user_agent` VARCHAR(1024) DEFAULT NULL COMMENT '浏览器 UA',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志记录';

-- ----------------------------
-- 16. 登录日志表（增强版）
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `log_type` BIGINT NOT NULL COMMENT '日志类型',
    `trace_id` VARCHAR(64) DEFAULT '' COMMENT '链路追踪编号',
    `user_id` BIGINT DEFAULT 0 COMMENT '用户编号',
    `user_type` TINYINT NOT NULL DEFAULT 0 COMMENT '用户类型',
    `username` VARCHAR(50) DEFAULT '' COMMENT '用户账号',
    `result` TINYINT NOT NULL COMMENT '登陆结果',
    `user_ip` VARCHAR(50) NOT NULL COMMENT '用户 IP',
    `user_agent` VARCHAR(512) NOT NULL COMMENT '浏览器 UA',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_log_type` (`log_type`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统访问记录';

-- ----------------------------
-- 17. 敏感词表
-- ----------------------------
DROP TABLE IF EXISTS `sys_sensitive_word`;
CREATE TABLE `sys_sensitive_word` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(255) NOT NULL COMMENT '敏感词',
    `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
    `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签数组',
    `status` TINYINT NOT NULL COMMENT '状态（0正常 1停用）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词';

-- ----------------------------
-- 18. 错误码表
-- ----------------------------
DROP TABLE IF EXISTS `sys_error_code`;
CREATE TABLE `sys_error_code` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '错误码编号',
    `type` TINYINT NOT NULL DEFAULT 0 COMMENT '错误码类型',
    `application_name` VARCHAR(50) NOT NULL COMMENT '应用名',
    `code` INT NOT NULL COMMENT '错误码编码',
    `message` VARCHAR(512) NOT NULL COMMENT '错误码错误提示',
    `memo` VARCHAR(512) DEFAULT '' COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_application_name_code` (`application_name`, `code`, `update_time`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错误码表';

-- ----------------------------
-- 19. 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    `category` VARCHAR(50) NOT NULL COMMENT '参数分组',
    `type` TINYINT NOT NULL COMMENT '参数类型',
    `name` VARCHAR(100) NOT NULL COMMENT '参数名称',
    `config_key` VARCHAR(100) NOT NULL COMMENT '参数键名',
    `value` VARCHAR(500) NOT NULL COMMENT '参数键值',
    `visible` BIT NOT NULL COMMENT '是否可见',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`, `update_time`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='参数配置表';

-- ----------------------------
-- 20. 字典类型表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    `name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `deleted_time` DATETIME DEFAULT NULL COMMENT '删除时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type` (`type`, `update_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- ----------------------------
-- 21. 字典数据表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '字典排序',
    `label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `value` VARCHAR(100) NOT NULL COMMENT '字典键值',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
    `color_type` VARCHAR(100) DEFAULT '' COMMENT '颜色类型',
    `css_class` VARCHAR(100) DEFAULT '' COMMENT 'css 样式',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_dict_type` (`dict_type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- ----------------------------
-- 22. 通知公告表
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(50) NOT NULL COMMENT '公告标题',
    `content` TEXT NOT NULL COMMENT '公告内容',
    `type` TINYINT NOT NULL COMMENT '公告类型（1通知 2公告）',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '公告状态（0正常 1关闭）',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表';

-- ----------------------------
-- 23. 社交用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_social_user`;
CREATE TABLE `sys_social_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键(自增策略)',
    `type` TINYINT NOT NULL COMMENT '社交平台的类型',
    `openid` VARCHAR(32) NOT NULL COMMENT '社交 openid',
    `token` VARCHAR(256) DEFAULT NULL COMMENT '社交 token',
    `raw_token_info` VARCHAR(1024) NOT NULL COMMENT '原始 Token 数据，一般是 JSON 格式',
    `nickname` VARCHAR(32) NOT NULL COMMENT '用户昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '用户头像',
    `raw_user_info` VARCHAR(1024) NOT NULL COMMENT '原始用户数据，一般是 JSON 格式',
    `code` VARCHAR(256) NOT NULL COMMENT '最后一次的认证 code',
    `state` VARCHAR(256) DEFAULT NULL COMMENT '最后一次的认证 state',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_type_openid` (`type`, `openid`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交用户表';

-- ----------------------------
-- 24. 社交用户绑定表
-- ----------------------------
DROP TABLE IF EXISTS `sys_social_user_bind`;
CREATE TABLE `sys_social_user_bind` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键(自增策略)',
    `user_id` BIGINT NOT NULL COMMENT '用户编号',
    `user_type` TINYINT NOT NULL COMMENT '用户类型',
    `social_type` TINYINT NOT NULL COMMENT '社交平台的类型',
    `social_user_id` BIGINT NOT NULL COMMENT '社交用户的编号',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` VARCHAR(20) NOT NULL DEFAULT '000000' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_user_id_user_type` (`user_id`, `user_type`),
    KEY `idx_social_user_id` (`social_user_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交用户绑定表';

-- ----------------------------
-- 索引优化建议
-- ----------------------------

-- 为高频查询字段添加复合索引
ALTER TABLE `sys_user` ADD INDEX `idx_tenant_status_deleted` (`tenant_id`, `status`, `deleted`);
ALTER TABLE `sys_role` ADD INDEX `idx_tenant_status_deleted` (`tenant_id`, `status`, `deleted`);
ALTER TABLE `sys_menu` ADD INDEX `idx_type_status_deleted` (`type`, `status`, `deleted`);
ALTER TABLE `sys_dept` ADD INDEX `idx_tenant_status_deleted` (`tenant_id`, `status`, `deleted`);

-- 为日志表添加时间范围查询索引
ALTER TABLE `sys_operate_log` ADD INDEX `idx_tenant_create_time` (`tenant_id`, `create_time`);
ALTER TABLE `sys_login_log` ADD INDEX `idx_tenant_create_time` (`tenant_id`, `create_time`);

-- 为会话表添加超时时间索引（用于清理过期会话）
ALTER TABLE `sys_user_session` ADD INDEX `idx_session_timeout_deleted` (`session_timeout`, `deleted`);

-- =============================================
-- 表设计改进说明
-- =============================================

/*
基于RuoYi-Vue-Plus/Pro等优秀开源框架的改进点：

1. 多租户支持
   - 所有核心表都增加了 tenant_id 字段
   - 支持租户套餐管理
   - 租户数据完全隔离

2. 现代化字段设计
   - 统一使用 creator/create_time/updater/update_time 字段
   - 使用 deleted 位字段替代 del_flag
   - 字段类型更加规范（TINYINT、BIT等）

3. OAuth2 完整支持
   - 标准的 OAuth2 授权表结构
   - 支持多种授权模式
   - 完整的客户端管理

4. API 接口权限管理
   - sys_api 表管理所有接口
   - 支持接口级权限控制
   - 接口分组管理

5. 增强的日志系统
   - 操作日志支持链路追踪
   - 登录日志类型化管理
   - 用户会话完整记录

6. 社交登录支持
   - 社交用户信息管理
   - 用户绑定关系
   - 支持多平台登录

7. 数据权限增强
   - 角色支持数据范围控制
   - 部门数据权限
   - 个人数据权限

8. 系统管理完善
   - 敏感词管理
   - 错误码管理
   - 系统配置分类管理
   - 字典数据增强

9. 性能优化
   - 合理的索引设计
   - 复合索引优化
   - 查询性能提升

10. 扩展性设计
    - 预留扩展字段
    - 灵活的配置机制
    - 模块化设计
*/
