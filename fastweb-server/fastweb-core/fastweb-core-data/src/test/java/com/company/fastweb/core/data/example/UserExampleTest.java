package com.company.fastweb.core.data.example;

import com.company.fastweb.core.data.example.UserPO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户实体使用示例测试
 *
 * @author FastWeb
 */
class UserExampleTest {

    @Test
    void testUserPOCreation() {
        UserPO user = new UserPO();
        
        // 设置业务字段
        user.setUsername("zhangsan");
        user.setEmail("zhangsan@example.com");
        user.setPhone("13800138000");
        user.setPassword("encrypted_password");
        user.setNickname("张三");
        user.setAvatar("https://example.com/avatar.jpg");
        user.setDeptId(1L);
        
        // 设置基础字段（通常由MyBatis-Plus自动填充）
        user.setId(100L);
        user.setCreateBy("admin");
        user.setUpdateBy("admin");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(0);
        user.setIsDeleted(0);
        user.setVersion(1);
        user.setTenantId("000000");
        
        // 验证字段
        assertNotNull(user);
        assertEquals("zhangsan", user.getUsername());
        assertEquals("zhangsan@example.com", user.getEmail());
        assertEquals(100L, user.getId());
        assertEquals("admin", user.getCreateBy());
        assertEquals(0, user.getStatus());
        assertEquals(0, user.getIsDeleted());
        assertEquals(1, user.getVersion());
        assertEquals("000000", user.getTenantId());
    }

    @Test
    void testUserPOInheritance() {
        UserPO user = new UserPO();
        
        // 验证继承了BasePO的所有字段
        assertTrue(user instanceof com.company.fastweb.core.data.model.BasePO);
        
        // 测试继承的字段
        user.setId(1L);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateBy("system");
        
        assertEquals(1L, user.getId());
        assertEquals("system", user.getCreateBy());
        assertNotNull(user.getCreateTime());
    }
}