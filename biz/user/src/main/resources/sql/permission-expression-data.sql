-- 权限表达式初始化数据

-- 插入示例权限表达式
INSERT INTO sys_permission_expression (permission_code, expression_name, expression_content, expression_type, description, status) VALUES
('user:edit', '用户编辑权限', '#currentUser.deptId == #target.deptId && #currentUser.level >= #target.level', 'spel', '只能编辑同部门且级别不高于自己的用户', 1),
('user:delete', '用户删除权限', '#currentUser.roleCode == ''admin'' || (#currentUser.deptId == #target.deptId && #currentUser.level > #target.level)', 'spel', '管理员或上级可删除用户', 1),
('data:export', '数据导出权限', '#currentUser.level >= 3 && #now >= #startTime && #now <= #endTime', 'spel', '三级以上用户在工作时间可导出数据', 1),
('order:approve', '订单审批权限', '#currentUser.deptId == #target.deptId && #target.amount <= #currentUser.approvalLimit', 'spel', '同部门且在审批额度内可审批订单', 1),
('report:view', '报表查看权限', '#currentUser.roleCode == ''manager'' || #currentUser.deptId == #target.deptId', 'spel', '经理或同部门可查看报表', 1);

-- 插入权限委托示例数据
INSERT INTO sys_permission_delegation (delegator_id, delegatee_id, permission_ids, delegation_type, start_time, end_time, reason, status) VALUES
(1, 2, '[1,2,3]', 'temporary', '2024-01-01 09:00:00', '2024-01-07 18:00:00', '出差期间临时委托', 'active'),
(3, 4, '[4,5]', 'temporary', '2024-01-15 09:00:00', '2024-01-20 18:00:00', '休假期间权限委托', 'active');

-- 插入权限策略示例数据
INSERT INTO sys_permission_strategy (strategy_name, strategy_type, strategy_config, priority, status) VALUES
('工作时间策略', 'time', '{"startTime": "09:00", "endTime": "18:00", "workDays": [1,2,3,4,5]}', 1, 1),
('IP地址限制策略', 'condition', '{"allowedIPs": ["192.168.1.0/24", "10.0.0.0/8"]}', 2, 1),
('高风险操作审批策略', 'approval', '{"approvers": [1,2], "requiredApprovals": 2}', 3, 1);

-- 插入权限策略关联示例数据
INSERT INTO sys_permission_strategy_rel (strategy_id, target_type, target_id, permission_id, effect) VALUES
(1, 'role', 1, 1, 'allow'),
(1, 'role', 2, 2, 'allow'),
(2, 'user', 1, 3, 'allow'),
(3, 'role', 1, 4, 'allow');