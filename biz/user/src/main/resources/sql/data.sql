-- 初始化数据脚本

-- 插入超级管理员用户 (ID=1)
INSERT INTO sys_user (id, username, password, email, real_name, nickname, status, enabled, account_non_expired, account_non_locked, credentials_non_expired, create_by) 
VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIUi', 'admin@company.com', '超级管理员', '管理员', 0, TRUE, TRUE, TRUE, TRUE, 'system')
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 插入超级管理员角色
INSERT INTO sys_role (id, code, name, description, status, sort, create_by) 
VALUES (1, 'SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 0, 0, 'system')
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 插入普通管理员角色
INSERT INTO sys_role (id, code, name, description, status, sort, create_by) 
VALUES (2, 'ADMIN', '管理员', '系统管理员', 0, 1, 'system')
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 插入普通用户角色
INSERT INTO sys_role (id, code, name, description, status, sort, create_by) 
VALUES (3, 'USER', '普通用户', '系统普通用户', 0, 2, 'system')
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 插入系统管理权限
INSERT INTO sys_permission (id, code, name, description, type, parent_id, path, url, method, status, sort, create_by) VALUES
(1, 'SYSTEM', '系统管理', '系统管理根权限', 1, 0, '/system', NULL, NULL, 0, 0, 'system'),
(2, 'SYSTEM:USER', '用户管理', '用户管理权限', 1, 1, '/system/user', NULL, NULL, 0, 1, 'system'),
(3, 'SYSTEM:USER:LIST', '用户列表', '查看用户列表', 3, 2, NULL, '/api/system/user/list', 'GET', 0, 1, 'system'),
(4, 'SYSTEM:USER:CREATE', '创建用户', '创建新用户', 3, 2, NULL, '/api/system/user', 'POST', 0, 2, 'system'),
(5, 'SYSTEM:USER:UPDATE', '更新用户', '更新用户信息', 3, 2, NULL, '/api/system/user/*', 'PUT', 0, 3, 'system'),
(6, 'SYSTEM:USER:DELETE', '删除用户', '删除用户', 3, 2, NULL, '/api/system/user/*', 'DELETE', 0, 4, 'system'),
(7, 'SYSTEM:ROLE', '角色管理', '角色管理权限', 1, 1, '/system/role', NULL, NULL, 0, 2, 'system'),
(8, 'SYSTEM:ROLE:LIST', '角色列表', '查看角色列表', 3, 7, NULL, '/api/system/role/list', 'GET', 0, 1, 'system'),
(9, 'SYSTEM:ROLE:CREATE', '创建角色', '创建新角色', 3, 7, NULL, '/api/system/role', 'POST', 0, 2, 'system'),
(10, 'SYSTEM:ROLE:UPDATE', '更新角色', '更新角色信息', 3, 7, NULL, '/api/system/role/*', 'PUT', 0, 3, 'system'),
(11, 'SYSTEM:ROLE:DELETE', '删除角色', '删除角色', 3, 7, NULL, '/api/system/role/*', 'DELETE', 0, 4, 'system'),
(12, 'SYSTEM:PERMISSION', '权限管理', '权限管理权限', 1, 1, '/system/permission', NULL, NULL, 0, 3, 'system'),
(13, 'SYSTEM:PERMISSION:LIST', '权限列表', '查看权限列表', 3, 12, NULL, '/api/system/permission/list', 'GET', 0, 1, 'system'),
(14, 'SYSTEM:PERMISSION:CREATE', '创建权限', '创建新权限', 3, 12, NULL, '/api/system/permission', 'POST', 0, 2, 'system'),
(15, 'SYSTEM:PERMISSION:UPDATE', '更新权限', '更新权限信息', 3, 12, NULL, '/api/system/permission/*', 'PUT', 0, 3, 'system'),
(16, 'SYSTEM:PERMISSION:DELETE', '删除权限', '删除权限', 3, 12, NULL, '/api/system/permission/*', 'DELETE', 0, 4, 'system')
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 插入认证相关权限
INSERT INTO sys_permission (id, code, name, description, type, parent_id, path, url, method, status, sort, create_by) VALUES
(17, 'AUTH', '认证管理', '认证管理根权限', 1, 0, '/auth', NULL, NULL, 0, 1, 'system'),
(18, 'AUTH:LOGIN', '用户登录', '用户登录权限', 3, 17, NULL, '/api/auth/login', 'POST', 0, 1, 'system'),
(19, 'AUTH:LOGOUT', '用户登出', '用户登出权限', 3, 17, NULL, '/api/auth/logout', 'POST', 0, 2, 'system'),
(20, 'AUTH:REFRESH', '刷新令牌', '刷新访问令牌', 3, 17, NULL, '/api/auth/refresh', 'POST', 0, 3, 'system'),
(21, 'AUTH:SESSION', '会话管理', '会话管理权限', 1, 17, '/auth/session', NULL, NULL, 0, 4, 'system'),
(22, 'AUTH:SESSION:LIST', '会话列表', '查看会话列表', 3, 21, NULL, '/api/auth/session/list', 'GET', 0, 1, 'system'),
(23, 'AUTH:SESSION:KICK', '强制下线', '强制用户下线', 3, 21, NULL, '/api/auth/session/kick/*', 'POST', 0, 2, 'system')
ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 为超级管理员分配角色
INSERT INTO sys_user_role (user_id, role_id, create_by) 
VALUES (1, 1, 'system')
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 为超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id, create_by)
SELECT 1, id, 'system' FROM sys_permission
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 为管理员角色分配基础权限
INSERT INTO sys_role_permission (role_id, permission_id, create_by) VALUES
(2, 1, 'system'),  -- 系统管理
(2, 2, 'system'),  -- 用户管理
(2, 3, 'system'),  -- 用户列表
(2, 4, 'system'),  -- 创建用户
(2, 5, 'system'),  -- 更新用户
(2, 7, 'system'),  -- 角色管理
(2, 8, 'system'),  -- 角色列表
(2, 17, 'system'), -- 认证管理
(2, 18, 'system'), -- 用户登录
(2, 19, 'system'), -- 用户登出
(2, 20, 'system'), -- 刷新令牌
(2, 21, 'system'), -- 会话管理
(2, 22, 'system'), -- 会话列表
(2, 23, 'system')  -- 强制下线
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 为普通用户角色分配基础权限
INSERT INTO sys_role_permission (role_id, permission_id, create_by) VALUES
(3, 17, 'system'), -- 认证管理
(3, 18, 'system'), -- 用户登录
(3, 19, 'system'), -- 用户登出
(3, 20, 'system')  -- 刷新令牌
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 创建测试用户
INSERT INTO sys_user (id, username, password, email, real_name, nickname, status, enabled, account_non_expired, account_non_locked, credentials_non_expired, create_by) VALUES
(2, 'test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIUi', 'test@company.com', '测试用户', '测试', 0, TRUE, TRUE, TRUE, TRUE, 'admin'),
(3, 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIUi', 'user@company.com', '普通用户', '用户', 0, TRUE, TRUE, TRUE, TRUE, 'admin')
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 为测试用户分配管理员角色
INSERT INTO sys_user_role (user_id, role_id, create_by) 
VALUES (2, 2, 'admin')
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 为普通用户分配普通用户角色
INSERT INTO sys_user_role (user_id, role_id, create_by) 
VALUES (3, 3, 'admin')
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);