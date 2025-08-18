package com.company.fastweb.core.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author FastWeb
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 配置ObjectMapper
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // 注册Java8时间模块
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        
        // 设置日期格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 对象转JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
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
     *
     * @param jsonString JSON字符串
     * @param clazz      目标类型
     * @param <T>        泛型
     * @return 对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("JSON字符串转对象失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转对象（支持泛型）
     *
     * @param jsonString    JSON字符串
     * @param typeReference 类型引用
     * @param <T>           泛型
     * @return 对象
     */
    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            log.error("JSON字符串转对象失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转List
     *
     * @param jsonString JSON字符串
     * @param clazz      元素类型
     * @param <T>        泛型
     * @return List对象
     */
    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, 
                OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("JSON字符串转List失败", e);
            return null;
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param jsonString JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> parseMap(String jsonString) {
        return parseObject(jsonString, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * 对象转Map
     *
     * @param obj 对象
     * @return Map对象
     */
    public static Map<String, Object> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Map转对象
     *
     * @param map   Map对象
     * @param clazz 目标类型
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * 格式化JSON字符串（美化输出）
     *
     * @param obj 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON字符串失败", e);
            return null;
        }
    }

    /**
     * 获取ObjectMapper实例
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}