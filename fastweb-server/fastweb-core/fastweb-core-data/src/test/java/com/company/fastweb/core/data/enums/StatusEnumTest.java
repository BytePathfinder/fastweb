package com.company.fastweb.core.data.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StatusEnum测试类
 *
 * @author FastWeb
 */
class StatusEnumTest {

    @Test
    void testStatusEnumValues() {
        // 测试枚举值
        assertEquals(0, StatusEnum.NORMAL.getCode());
        assertEquals("正常", StatusEnum.NORMAL.getDesc());
        
        assertEquals(1, StatusEnum.DISABLE.getCode());
        assertEquals("停用", StatusEnum.DISABLE.getDesc());
        
        assertEquals(2, StatusEnum.DELETED.getCode());
        assertEquals("删除", StatusEnum.DELETED.getDesc());
    }

    @Test
    void testFromCode() {
        assertEquals(StatusEnum.NORMAL, StatusEnum.fromCode(0));
        assertEquals(StatusEnum.DISABLE, StatusEnum.fromCode(1));
        assertEquals(StatusEnum.DELETED, StatusEnum.fromCode(2));
        
        // 测试不存在的code
        assertNull(StatusEnum.fromCode(999));
    }
}