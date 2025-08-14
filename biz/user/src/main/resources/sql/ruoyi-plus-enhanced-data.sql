-- =============================================
-- 基于RuoYi-Vue-Plus/Pro等优秀开源框架的初始化数据
-- 包含租户、用户、角色、菜单权限等基础数据
-- =============================================

-- ----------------------------
-- 1. 租户套餐数据
-- ----------------------------
INSERT INTO `sys_tenant_package` VALUES (1, '入门版', 0, '适合个人开发者', '[1,2,5,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30]', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_tenant_package` VALUES (2, '标准版', 0, '适合小团队', '[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40]', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_tenant_package` VALUES (3, '企业版', 0, '适合大型企业', '[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50]', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 2. 租户数据
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1, '000000', '芋道', '15601691300', '芋道源码', '91310115MA1J3QJQ3X', '上海市', '我是介绍', 'https://www.iocoder.cn', '我是备注', 1, '2030-11-01 00:00:00', 9999, 0, 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 3. 部门数据
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, '000000', '芋道源码', 0, 0, 104, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (101, '000000', '深圳总公司', 100, 1, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (102, '000000', '长沙分公司', 100, 2, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (103, '000000', '研发部门', 101, 1, 104, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (104, '000000', '市场部门', 101, 2, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (105, '000000', '测试部门', 101, 3, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (106, '000000', '财务部门', 101, 4, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (107, '000000', '运维部门', 101, 5, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (108, '000000', '市场部门', 102, 1, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dept` VALUES (109, '000000', '财务部门', 102, 2, NULL, '15888888888', 'ry@qq.com', 0, 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 4. 岗位数据
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, '000000', 'ceo', '董事长', 1, 0, '董事长', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_post` VALUES (2, '000000', 'se', '项目经理', 2, 0, '项目经理', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_post` VALUES (3, '000000', 'hr', '人力资源', 3, 0, '人力资源', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_post` VALUES (4, '000000', 'user', '普通员工', 4, 0, '普通员工', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 5. 角色数据
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '000000', '超级管理员', 'super_admin', 1, 1, '', 0, 1, '超级管理员', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_role` VALUES (2, '000000', '普通角色', 'common', 2, 2, '', 0, 2, '普通角色', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_role` VALUES (100, '000000', '租户管理员', 'tenant_admin', 1, 1, '', 0, 1, '租户管理员', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_role` VALUES (101, '000000', '测试账号', 'test', 0, 1, '', 0, 2, '测试账号', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 6. 用户数据
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '000000', 'admin', '$2a$10$7JB720yubVSOfvVWbfXCL.VqGOZTH4ybSKlKkDOjkT9VTEwWgK4ZC', '芋道源码', '管理员', 103, '[1,2]', 'aoteman@126.com', '15612345678', 1, 'http://test.yudao.iocoder.cn/96c787a2ce88bf6d0ce3cd8b6cf5314e80e7703cd41bf4af8cd2e2909dbd6b6d.png', 0, '127.0.0.1', NOW(), 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_user` VALUES (100, '000000', 'yudao', '$2a$10$11U48RhyJ5pSBYWSn12AD./ld671.ycSzJHbyrtpeoMeYiw31eo8a', '芋道', '不要吓我', 104, '[1]', 'yudao@iocoder.cn', '15601691300', 1, '', 0, '127.0.0.1', NOW(), 'admin', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (103, '000000', 'yuanma', '$2a$10$YMpimV4T6BtDhIaA8jSW.u8UTGBeGhc/qwXP4oxoMr4mOw9.qttt6', '源码', '源码', 106, NULL, 'yuanma@iocoder.cn', '15601701300', 0, '', 0, '0:0:0:0:0:0:0:1', NOW(), 'admin', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (104, '000000', 'test', '$2a$04$KhExCYl7lx6eWWZYKsibKOZ8IBJRyuNuCcEOLQ11RYhJKgHmlSwK.', '测试号', '我就是一个测试', 107, '[3]', '', '15601691200', 1, '', 0, '0:0:0:0:0:0:0:1', NOW(), 'admin', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (107, '000000', 'admin107', '$2a$10$dYOOBKMO93v/.ReCqzyFg.o67Tqk.bbc2bhrpyBGkIw9aypCtr2pm', 'admin107', '', 100, NULL, '', '15601691300', 0, '', 0, '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (108, '000000', 'admin108', '$2a$10$y6mfvKoNYL1GXWak8nYwVOH.kCWqjactkzdoIDgiKl93WN3Ejg.Lu', 'admin108', '', 100, NULL, '', '15601691300', 0, '', 0, '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (109, '000000', 'admin109', '$2a$10$JAqvH0tEc0I7dfDVBI7zyOWsjlW6UArUMjpzJoB1/YvdXTfKBCafe', 'admin109', '', 100, NULL, '', '15601691300', 0, '', 0, '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (110, '000000', 'admin110', '$2a$10$mRMIYLDtRHlf6.9ipiqH1.Z.bh/R9dO9d5iHiGYPigi6r5KOoR2Wm', 'admin110', '', 100, NULL, '', '15601691300', 0, '', 0, '127.0.0.1', NOW(), '1', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (111, '000000', 'test', '$2a$10$mExveopHUuEKcHQzbZRsAuBmzS8RdMckqPNjsOEwjOpaEMvIcicGa', 'test', '', 100, '[]', '', '', 0, '', 0, '127.0.0.1', NOW(), '1', NOW(), '1', NOW(), b'0');
INSERT INTO `sys_user` VALUES (112, '000000', 'newobject', '$2a$04$dB0z8Q819fJWz0hbaLe6B.VfHCjYgWx6LFfET5lyz3JwcqlyCkQ4C', '新对象', '', 100, '[]', '', '', 2, '', 0, '127.0.0.1', NOW(), '1', NOW(), '1', NOW(), b'0');

-- ----------------------------
-- 7. 菜单权限数据
-- ----------------------------
-- 一级菜单
INSERT INTO `sys_menu` VALUES (1, '系统管理', '', 1, 10, 0, '/system', 'system', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (2, '基础设施', '', 1, 20, 0, '/infra', 'monitor', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (3, '研发工具', '', 1, 30, 0, '/tool', 'tool', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 系统管理子菜单
INSERT INTO `sys_menu` VALUES (100, '用户管理', 'system:user:list', 2, 1, 1, 'user', 'user', 'system/user/index', 'SystemUser', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 'system:role:list', 2, 2, 1, 'role', 'peoples', 'system/role/index', 'SystemRole', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 'system:menu:list', 2, 3, 1, 'menu', 'tree-table', 'system/menu/index', 'SystemMenu', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 'system:dept:list', 2, 4, 1, 'dept', 'tree', 'system/dept/index', 'SystemDept', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 'system:post:list', 2, 5, 1, 'post', 'post', 'system/post/index', 'SystemPost', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 'system:dict:list', 2, 6, 1, 'dict', 'dict', 'system/dict/index', 'SystemDictType', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 'system:config:list', 2, 7, 1, 'config', 'edit', 'system/config/index', 'SystemConfig', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 'system:notice:list', 2, 8, 1, 'notice', 'message', 'system/notice/index', 'SystemNotice', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (108, '审计日志', '', 1, 9, 1, 'log', 'log', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 'system:user-session:list', 2, 10, 1, 'user-session', 'people', 'system/user-session/index', 'SystemUserSession', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 审计日志子菜单
INSERT INTO `sys_menu` VALUES (500, '操作日志', 'system:operate-log:list', 2, 1, 108, 'operate-log', 'form', 'system/operatelog/index', 'SystemOperateLog', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 'system:login-log:list', 2, 2, 108, 'login-log', 'logininfor', 'system/loginlog/index', 'SystemLoginLog', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 基础设施子菜单
INSERT INTO `sys_menu` VALUES (200, 'API 接口', 'infra:api:list', 2, 1, 2, 'api', 'swagger', 'infra/api/index', 'InfraApi', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (201, '数据源配置', 'infra:data-source-config:list', 2, 2, 2, 'data-source-config', 'rate', 'infra/dataSourceConfig/index', 'InfraDataSourceConfig', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (202, 'Redis 监控', 'infra:redis:list', 2, 3, 2, 'redis', 'redis', 'infra/redis/index', 'InfraRedis', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (203, '任务调度', 'infra:job:list', 2, 4, 2, 'job', 'job', 'infra/job/index', 'InfraJob', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (204, 'Java 监控', 'infra:server:list', 2, 5, 2, 'server', 'server', 'infra/server/index', 'InfraServer', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 研发工具子菜单
INSERT INTO `sys_menu` VALUES (300, '系统接口', 'infra:swagger:list', 2, 1, 3, 'swagger', 'swagger', 'tool/swagger/index', 'ToolSwagger', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (301, '代码生成', 'infra:codegen:list', 2, 2, 3, 'codegen', 'code', 'tool/codegen/index', 'ToolCodegen', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 用户管理按钮权限
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 'system:user:query', 3, 1, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 'system:user:create', 3, 2, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 'system:user:update', 3, 3, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 'system:user:delete', 3, 4, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 'system:user:export', 3, 5, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 'system:user:import', 3, 6, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 'system:user:reset-password', 3, 7, 100, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 角色管理按钮权限
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 'system:role:query', 3, 1, 101, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 'system:role:create', 3, 2, 101, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 'system:role:update', 3, 3, 101, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 'system:role:delete', 3, 4, 101, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 'system:role:export', 3, 5, 101, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 菜单管理按钮权限
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 'system:menu:query', 3, 1, 102, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 'system:menu:create', 3, 2, 102, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 'system:menu:update', 3, 3, 102, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 'system:menu:delete', 3, 4, 102, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 部门管理按钮权限
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 'system:dept:query', 3, 1, 103, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 'system:dept:create', 3, 2, 103, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 'system:dept:update', 3, 3, 103, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 'system:dept:delete', 3, 4, 103, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 岗位管理按钮权限
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 'system:post:query', 3, 1, 104, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 'system:post:create', 3, 2, 104, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 'system:post:update', 3, 3, 104, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 'system:post:delete', 3, 4, 104, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 'system:post:export', 3, 5, 104, '', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- 字典管理按钮权限
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 'system:dict:query', 3, 1, 105, '#', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 'system:dict:create', 3, 2, 105, '#', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 'system:dict:update', 3, 3, 105, '#', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 'system:dict:delete', 3, 4, 105, '#', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 'system:dict:export', 3, 5, 105, '#', '#', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 8. 用户角色关联数据
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_user_role` VALUES (2, 2, 2, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_user_role` VALUES (4, 100, 101, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_user_role` VALUES (5, 103, 1, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_user_role` VALUES (6, 104, 101, 'admin', NOW(), '', NOW(), b'0', '000000');

-- ----------------------------
-- 9. 角色菜单关联数据
-- ----------------------------
-- 超级管理员拥有所有权限
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (4, 1, 100, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (5, 1, 101, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (6, 1, 102, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (7, 1, 103, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (8, 1, 104, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (9, 1, 105, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (10, 1, 106, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (11, 1, 107, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (12, 1, 108, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (13, 1, 109, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (14, 1, 500, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (15, 1, 501, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (16, 1, 200, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (17, 1, 201, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (18, 1, 202, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (19, 1, 203, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (20, 1, 204, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (21, 1, 300, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (22, 1, 301, 'admin', NOW(), '', NOW(), b'0', '000000');

-- 超级管理员拥有所有按钮权限
INSERT INTO `sys_role_menu` VALUES (100, 1, 1001, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (101, 1, 1002, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (102, 1, 1003, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (103, 1, 1004, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (104, 1, 1005, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (105, 1, 1006, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (106, 1, 1007, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (107, 1, 1008, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (108, 1, 1009, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (109, 1, 1010, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (110, 1, 1011, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (111, 1, 1012, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (112, 1, 1013, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (113, 1, 1014, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (114, 1, 1015, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (115, 1, 1016, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (116, 1, 1017, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (117, 1, 1018, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (118, 1, 1019, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (119, 1, 1020, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (120, 1, 1021, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (121, 1, 1022, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (122, 1, 1023, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (123, 1, 1024, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (124, 1, 1025, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (125, 1, 1026, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (126, 1, 1027, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (127, 1, 1028, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (128, 1, 1029, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (129, 1, 1030, 'admin', NOW(), '', NOW(), b'0', '000000');

-- 普通角色权限（只有查询权限）
INSERT INTO `sys_role_menu` VALUES (200, 2, 1, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (201, 2, 100, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (202, 2, 101, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (203, 2, 102, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (204, 2, 103, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (205, 2, 104, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (206, 2, 1001, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (207, 2, 1008, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (208, 2, 1013, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (209, 2, 1017, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_role_menu` VALUES (210, 2, 1021, 'admin', NOW(), '', NOW(), b'0', '000000');

-- ----------------------------
-- 10. 系统配置数据
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'ui', 1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', b'0', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_config` VALUES (2, 'ui', 1, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', b'0', '初始化密码 123456', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_config` VALUES (3, 'ui', 1, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', b'0', '深色主题theme-dark，浅色主题theme-light', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_config` VALUES (4, 'account', 1, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', b'1', '是否开启验证码功能（true开启，false关闭）', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_config` VALUES (5, 'account', 1, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', b'1', '是否开启注册用户功能（true开启，false关闭）', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_config` VALUES (6, 'file', 1, '文件上传-文件大小', 'sys.uploadFile.baseDir', 'uploadPath', b'0', '文件上传路径', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 11. 字典类型数据
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 0, '用户性别列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 0, '菜单状态列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 0, '系统开关列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 0, '任务状态列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 0, '任务分组列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 0, '系统是否列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', 0, '通知类型列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', 0, '通知状态列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', 0, '操作类型列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', 0, '登录状态列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (11, '数据范围', 'sys_data_scope', 0, '数据范围列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (12, '菜单类型', 'sys_menu_type', 0, '菜单类型列表', 'admin', NOW(), '', NOW(), b'0', NULL);
INSERT INTO `sys_dict_type` VALUES (13, '角色类型', 'sys_role_type', 0, '角色类型列表', 'admin', NOW(), '', NOW(), b'0', NULL);

-- ----------------------------
-- 12. 字典数据
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', 0, '', '', '性别男', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', 0, '', '', '性别女', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', 0, '', '', '性别未知', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', 0, 'primary', '', '显示菜单', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', 0, 'danger', '', '隐藏菜单', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', 0, 'primary', '', '正常状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', 0, 'danger', '', '停用状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', 0, 'primary', '', '正常状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', 0, 'danger', '', '停用状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', 0, '', '', '默认分组', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', 0, '', '', '系统分组', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', 0, 'primary', '', '系统默认是', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', 0, 'danger', '', '系统默认否', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', 0, 'warning', '', '通知', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', 0, 'success', '', '公告', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', 0, 'primary', '', '正常状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', 0, 'danger', '', '关闭状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', 0, 'info', '', '其他操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', 0, 'info', '', '新增操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', 0, 'info', '', '修改操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', 0, 'danger', '', '删除操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', 0, 'primary', '', '授权操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', 0, 'warning', '', '导出操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', 0, 'warning', '', '导入操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', 0, 'danger', '', '强退操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', 0, 'warning', '', '生成操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', 0, 'danger', '', '清空操作', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', 0, 'primary', '', '正常状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', 0, 'danger', '', '停用状态', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (30, 1, '全部数据权限', '1', 'sys_data_scope', 0, '', '', '', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (31, 2, '自定数据权限', '2', 'sys_data_scope', 0, '', '', '', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (32, 3, '本部门数据权限', '3', 'sys_data_scope', 0, '', '', '', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (33, 4, '本部门及以下数据权限', '4', 'sys_data_scope', 0, '', '', '', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (34, 5, '仅本人数据权限', '5', 'sys_data_scope', 0, '', '', '', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (35, 1, '目录', '1', 'sys_menu_type', 0, '', '', '目录', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (36, 2, '菜单', '2', 'sys_menu_type', 0, '', '', '菜单', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (37, 3, '按钮', '3', 'sys_menu_type', 0, '', '', '按钮', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (38, 1, '内置', '1', 'sys_role_type', 0, 'danger', '', '内置角色', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_dict_data` VALUES (39, 2, '自定义', '2', 'sys_role_type', 0, 'primary', '', '自定义角色', 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 13. 通知公告数据
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2021-07-01 芋道新版本发布啦', '各位同学，随着芋道商城的不断迭代，已经实现了多租户、数据权限、SaaS 等重要功能，欢迎大家体验！', 1, 0, 'admin', NOW(), '', NOW(), b'0', '000000');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2021-07-01 系统凌晨维护', '各位同学，由于相关环境变更，系统将于 2021-07-01 23:30-24:00 进行维护，届时将无法访问，请大家做好准备。', 2, 0, 'admin', NOW(), '', NOW(), b'0', '000000');

-- ----------------------------
-- 14. OAuth2客户端数据
-- ----------------------------
INSERT INTO `sys_oauth2_registered_client` VALUES ('1', 'default', NOW(), '$2a$10$k/rKRDPXBOlb4T.MfLjKxOKGF8gJpJQONTFPJGqJz8QY8VQOJqJQO', NULL, 'FastWeb默认客户端', 'client_secret_basic', 'refresh_token,authorization_code,password', 'http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logout', 'user.read,user.write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",1800.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000]}');

-- ----------------------------
-- 15. API接口数据
-- ----------------------------
INSERT INTO `sys_api` VALUES (1, '用户登录', '/api/auth/login', 'POST', '认证管理', '用户登录接口', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (2, '用户登出', '/api/auth/logout', 'POST', '认证管理', '用户登出接口', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (3, '获取用户信息', '/api/auth/user-info', 'GET', '认证管理', '获取当前用户信息', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (4, '刷新令牌', '/api/auth/refresh-token', 'POST', '认证管理', '刷新访问令牌', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (5, '用户列表', '/api/system/users', 'GET', '用户管理', '获取用户列表', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (6, '创建用户', '/api/system/users', 'POST', '用户管理', '创建新用户', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (7, '更新用户', '/api/system/users/{id}', 'PUT', '用户管理', '更新用户信息', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (8, '删除用户', '/api/system/users/{id}', 'DELETE', '用户管理', '删除用户', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (9, '角色列表', '/api/system/roles', 'GET', '角色管理', '获取角色列表', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_api` VALUES (10, '创建角色', '/api/system/roles', 'POST', '角色管理', '创建新角色', 0, 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 16. 敏感词数据
-- ----------------------------
INSERT INTO `sys_sensitive_word` VALUES (1, '敏感词', '测试敏感词', '测试', 0, 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_sensitive_word` VALUES (2, '违禁词', '测试违禁词', '测试', 0, 'admin', NOW(), '', NOW(), b'0');

-- ----------------------------
-- 17. 错误码数据
-- ----------------------------
INSERT INTO `sys_error_code` VALUES (1, 1, 'fastweb-auth', 1001001000, '用户账号或密码不正确', '用户登录时，账号或密码不正确', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (2, 1, 'fastweb-auth', 1001001001, '账号被禁用', '用户登录时，账号被禁用', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (3, 1, 'fastweb-auth', 1001001002, '验证码不存在', '用户登录时，验证码不存在', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (4, 1, 'fastweb-auth', 1001001003, '验证码不正确', '用户登录时，验证码不正确', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (5, 1, 'fastweb-system', 1002001000, '用户不存在', '用户不存在', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (6, 1, 'fastweb-system', 1002001001, '用户已存在', '用户已存在', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (7, 1, 'fastweb-system', 1002002000, '角色不存在', '角色不存在', 'admin', NOW(), '', NOW(), b'0');
INSERT INTO `sys_error_code` VALUES (8, 1, 'fastweb-system', 1002002001, '角色已存在', '角色已存在', 'admin', NOW(), '', NOW(), b'0');

-- =============================================
-- 数据初始化说明
-- =============================================

/*
基于RuoYi-Vue-Plus/Pro等优秀开源框架的初始化数据特点：

1. 多租户数据
   - 默认租户 000000（系统租户）
   - 租户套餐支持不同功能权限
   - 所有数据都关联租户

2. 完整的权限体系
   - 超级管理员拥有所有权限
   - 普通角色只有查询权限
   - 按钮级权限精确控制

3. 标准化数据结构
   - 统一的创建者/创建时间字段
   - 软删除标记
   - 租户隔离

4. 丰富的字典数据
   - 用户性别、菜单状态等
   - 操作类型、数据范围等
   - 支持前端下拉选择

5. OAuth2支持
   - 默认客户端配置
   - 支持多种授权模式
   - 令牌配置完整

6. API接口管理
   - 接口分组管理
   - 权限关联
   - 状态控制

7. 系统配置
   - UI配置
   - 账号配置
   - 文件上传配置

8. 审计功能
   - 敏感词管理
   - 错误码管理
   - 通知公告

这套初始化数据为系统提供了完整的基础功能支持！
*/
