-- 按钮级权限数据补充脚本

-- 用户管理按钮权限
INSERT INTO sys_permission (id, code, name, description, type, parent_id, path, url, method, status, sort, create_by) VALUES
-- 用户管理页面按钮
(100, 'SYSTEM:USER:ADD_BTN', '新增用户按钮', '用户管理页面新增按钮权限', 2, 2, NULL, NULL, NULL, 0, 1, 'system'),
(101, 'SYSTEM:USER:EDIT_BTN', '编辑用户按钮', '用户管理页面编辑按钮权限', 2, 2, NULL, NULL, NULL, 0, 2, 'system'),
(102, 'SYSTEM:USER:DELETE_BTN', '删除用户按钮', '用户管理页面删除按钮权限', 2, 2, NULL, NULL, NULL, 0, 3, 'system'),
(103, 'SYSTEM:USER:RESET_PWD_BTN', '重置密码按钮', '用户管理页面重置密码按钮权限', 2, 2, NULL, NULL, NULL, 0, 4, 'system'),
(104, 'SYSTEM:USER:ENABLE_BTN', '启用/禁用按钮', '用户管理页面启用禁用按钮权限', 2, 2, NULL, NULL, NULL, 0, 5, 'system'),
(105, 'SYSTEM:USER:ASSIGN_ROLE_BTN', '分配角色按钮', '用户管理页面分配角色按钮权限', 2, 2, NULL, NULL, NULL, 0, 6, 'system'),
(106, 'SYSTEM:USER:EXPORT_BTN', '导出用户按钮', '用户管理页面导出按钮权限', 2, 2, NULL, NULL, NULL, 0, 7, 'system'),
(107, 'SYSTEM:USER:IMPORT_BTN', '导入用户按钮', '用户管理页面导入按钮权限', 2, 2, NULL, NULL, NULL, 0, 8, 'system'),

-- 角色管理页面按钮
(110, 'SYSTEM:ROLE:ADD_BTN', '新增角色按钮', '角色管理页面新增按钮权限', 2, 7, NULL, NULL, NULL, 0, 1, 'system'),
(111, 'SYSTEM:ROLE:EDIT_BTN', '编辑角色按钮', '角色管理页面编辑按钮权限', 2, 7, NULL, NULL, NULL, 0, 2, 'system'),
(112, 'SYSTEM:ROLE:DELETE_BTN', '删除角色按钮', '角色管理页面删除按钮权限', 2, 7, NULL, NULL, NULL, 0, 3, 'system'),
(113, 'SYSTEM:ROLE:ASSIGN_PERMISSION_BTN', '分配权限按钮', '角色管理页面分配权限按钮权限', 2, 7, NULL, NULL, NULL, 0, 4, 'system'),
(114, 'SYSTEM:ROLE:COPY_BTN', '复制角色按钮', '角色管理页面复制角色按钮权限', 2, 7, NULL, NULL, NULL, 0, 5, 'system'),

-- 权限管理页面按钮
(120, 'SYSTEM:PERMISSION:ADD_BTN', '新增权限按钮', '权限管理页面新增按钮权限', 2, 12, NULL, NULL, NULL, 0, 1, 'system'),
(121, 'SYSTEM:PERMISSION:EDIT_BTN', '编辑权限按钮', '权限管理页面编辑按钮权限', 2, 12, NULL, NULL, NULL, 0, 2, 'system'),
(122, 'SYSTEM:PERMISSION:DELETE_BTN', '删除权限按钮', '权限管理页面删除按钮权限', 2, 12, NULL, NULL, NULL, 0, 3, 'system'),
(123, 'SYSTEM:PERMISSION:SORT_BTN', '排序权限按钮', '权限管理页面排序按钮权限', 2, 12, NULL, NULL, NULL, 0, 4, 'system'),

-- 会话管理页面按钮
(130, 'AUTH:SESSION:KICK_BTN', '强制下线按钮', '会话管理页面强制下线按钮权限', 2, 21, NULL, NULL, NULL, 0, 1, 'system'),
(131, 'AUTH:SESSION:KICK_ALL_BTN', '批量下线按钮', '会话管理页面批量下线按钮权限', 2, 21, NULL, NULL, NULL, 0, 2, 'system'),
(132, 'AUTH:SESSION:REFRESH_BTN', '刷新列表按钮', '会话管理页面刷新按钮权限', 2, 21, NULL, NULL, NULL, 0, 3, 'system')

ON DUPLICATE KEY UPDATE code = VALUES(code);

-- 为超级管理员角色分配所有按钮权限
INSERT INTO sys_role_permission (role_id, permission_id, create_by)
SELECT 1, id, 'system' FROM sys_permission WHERE type = 2
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);

-- 为管理员角色分配部分按钮权限
INSERT INTO sys_role_permission (role_id, permission_id, create_by) VALUES
-- 用户管理按钮权限
(2, 100, 'system'), -- 新增用户按钮
(2, 101, 'system'), -- 编辑用户按钮
(2, 104, 'system'), -- 启用/禁用按钮
(2, 105, 'system'), -- 分配角色按钮
(2, 106, 'system'), -- 导出用户按钮
-- 角色管理按钮权限
(2, 110, 'system'), -- 新增角色按钮
(2, 111, 'system'), -- 编辑角色按钮
(2, 113, 'system'), -- 分配权限按钮
-- 会话管理按钮权限
(2, 130, 'system'), -- 强制下线按钮
(2, 132, 'system')  -- 刷新列表按钮
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);