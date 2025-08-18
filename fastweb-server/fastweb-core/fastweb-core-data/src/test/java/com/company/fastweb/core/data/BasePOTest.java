package com.company.fastweb.core.data;

import com.company.fastweb.core.data.model.BasePO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BasePO测试类
 * 
 * @author FastWeb
 */
class BasePOTest {

    @Test
    void testBasePOCreation() {
        BasePO basePO = new BasePO();
        
        // 测试基础字段设置
        basePO.setId(1L);
        basePO.setCreateBy("test_user");
        basePO.setUpdateBy("test_user");
        basePO.setStatus(0);
        basePO.setIsDeleted(0);
        basePO.setVersion(1);
        basePO.setTenantId("000000");
        
        // 验证字段值
        assertEquals(1L, basePO.getId());
        assertEquals("test_user", basePO.getCreateBy());
        assertEquals("test_user", basePO.getUpdateBy());
        assertEquals(0, basePO.getStatus());
        assertEquals(0, basePO.getIsDeleted());
        assertEquals(1, basePO.getVersion());
        assertEquals("000000", basePO.getTenantId());
        
        // 验证不为null
        assertNotNull(basePO);
    }

    @Test
    void testBasePOSetters() {
        BasePO basePO = new BasePO();
        basePO.setId(100L);
        basePO.setCreateBy("setter_user");
        basePO.setUpdateBy("setter_user");
        basePO.setStatus(1);
        basePO.setIsDeleted(0);
        basePO.setVersion(1);
        basePO.setTenantId("tenant_001");
                
        assertEquals(100L, basePO.getId());
        assertEquals("setter_user", basePO.getCreateBy());
        assertEquals(1, basePO.getStatus());
        assertEquals("tenant_001", basePO.getTenantId());
    }
}