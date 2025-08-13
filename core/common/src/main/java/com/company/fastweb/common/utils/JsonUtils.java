package com.company.fastweb.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * JSON工具类
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 对象转JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转对象（带异常抛出）
     */
    public static <T> T parseObjectThrow(String json, Class<T> clazz) throws JsonProcessingException {
        if (json == null || clazz == null) {
            return null;
        }
        return OBJECT_MAPPER.readValue(json, clazz);
    }
}