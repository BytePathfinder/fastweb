-- 权限表达式相关表结构

-- 权限表达式表
CREATE TABLE IF NOT EXISTS sys_permission_expression (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    permission_code VARCHAR(200) NOT NULL COMMENT '权限标识',
    expression_name VARCHAR(100) NOT NULL COMMENT '表达式名称',
    expression_content TEXT NOT NULL COMMENT '表达式内容',
    expression_type VARCHAR(50) NOT NULL DEFAULT 'spel' COMMENT '表达式类型：spel,groovy,javascript',
    variables JSON COMMENT '变量定义',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    tenant_id BIGINT COMMENT '租户ID',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_permission_id (permission_id),
    INDEX idx_permission_code (permission_code),
    INDEX idx_status (status),
    INDEX idx_tenant_id (tenant_id),
    UNIQUE KEY uk_permission_code_tenant (permission_code, tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表达式表';

-- 权限委托表
CREATE TABLE IF NOT EXISTS sys_permission_delegation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    delegator_id BIGINT NOT NULL COMMENT '委托人ID',
    delegatee_id BIGINT NOT NULL COMMENT '被委托人ID',
    permission_ids JSON NOT NULL COMMENT '委托权限ID列表',
    delegation_type VARCHAR(50) NOT NULL COMMENT '委托类型：temporary,permanent',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    reason VARCHAR(500) COMMENT '委托原因',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active,revoked,expired',
    tenant_id BIGINT COMMENT '租户ID',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_delegator_id (delegator_id),
    INDEX idx_delegatee_id (delegatee_id),
    INDEX idx_status (status),
    INDEX idx_end_time (end_time),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限委托表';

-- 权限策略表
CREATE TABLE IF NOT EXISTS sys_permission_strategy (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    strategy_name VARCHAR(100) NOT NULL COMMENT '策略名称',
    strategy_type VARCHAR(50) NOT NULL COMMENT '策略类型：time,condition,approval',
    strategy_config JSON NOT NULL COMMENT '策略配置',
    priority INT DEFAULT 0 COMMENT '优先级',
    status TINYINT DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    tenant_id BIGINT COMMENT '租户ID',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updater VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_strategy_type (strategy_type),
    INDEX idx_status (status),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限策略表';

-- 权限策略关联表
CREATE TABLE IF NOT EXISTS sys_permission_strategy_rel (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    strategy_id BIGINT NOT NULL COMMENT '策略ID',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型：user,role,dept',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    effect VARCHAR(20) NOT NULL COMMENT '效果：allow,deny',
    tenant_id BIGINT COMMENT '租户ID',
    creator VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_strategy_id (strategy_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_permission_id (permission_id),
    INDEX idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限策略关联表';