package com.company.fastweb.biz.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

/**
 * 数据库初始化器
 * 在应用启动时自动执行SQL脚本初始化数据库表结构和基础数据
 * 
 * @author CodeBuddy
 * @since 2024-01-01
 */
@Slf4j
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 检查是否已经初始化过
            if (isAlreadyInitialized()) {
                log.info("数据库已经初始化过，跳过初始化步骤");
                return;
            }

            log.info("开始初始化数据库...");
            
            // 执行表结构脚本
            executeScript("sql/schema.sql");
            
            // 执行初始化数据脚本
            executeScript("sql/data.sql");
            
            log.info("数据库初始化完成");
            
        } catch (Exception e) {
            log.error("数据库初始化失败", e);
            throw e;
        }
    }

    /**
     * 检查数据库是否已经初始化过
     */
    private boolean isAlreadyInitialized() {
        try {
            // 检查超级管理员用户是否存在
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE id = 1 AND username = 'admin'", 
                Integer.class
            );
            return count != null && count > 0;
        } catch (Exception e) {
            // 如果表不存在或查询失败，说明还没有初始化
            return false;
        }
    }

    /**
     * 执行SQL脚本
     */
    private void executeScript(String scriptPath) {
        try {
            ClassPathResource resource = new ClassPathResource(scriptPath);
            if (!resource.exists()) {
                log.warn("SQL脚本文件不存在: {}", scriptPath);
                return;
            }

            byte[] scriptBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String script = new String(scriptBytes, StandardCharsets.UTF_8);
            
            // 分割SQL语句并执行
            String[] statements = script.split(";");
            for (String statement : statements) {
                String trimmedStatement = statement.trim();
                if (!trimmedStatement.isEmpty() && !trimmedStatement.startsWith("--")) {
                    try {
                        jdbcTemplate.execute(trimmedStatement);
                    } catch (Exception e) {
                        // 记录错误但继续执行其他语句
                        log.warn("执行SQL语句失败: {}, 错误: {}", trimmedStatement, e.getMessage());
                    }
                }
            }
            
            log.info("成功执行SQL脚本: {}", scriptPath);
            
        } catch (Exception e) {
            log.error("执行SQL脚本失败: {}", scriptPath, e);
            throw new RuntimeException("执行SQL脚本失败: " + scriptPath, e);
        }
    }
}