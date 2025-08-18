package com.company.fastweb.core.data.handler;

import com.company.fastweb.core.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * JSON Map 类型处理器
 * 用于处理数据库JSON字段与Java Map对象之间的转换
 *
 * @author FastWeb
 */
@Slf4j
@MappedTypes(Map.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // 使用通用的JsonUtils进行序列化
            ps.setString(i, JsonUtils.toJsonString(parameter));
        } catch (Exception e) {
            log.error("无法将 Map 序列化为 JSON 字符串: {}", parameter, e);
            throw new SQLException("序列化Map到JSON时出错", e);
        }
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    @Override
    public Map<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    @Override
    public Map<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    /**
     * 解析JSON字符串为Map对象。
     *
     * @param json JSON字符串
     * @return Map对象，如果json字符串为空则返回null
     * @throws SQLException 如果解析失败
     */
    private Map<String, Object> parseJson(String json) throws SQLException {
        // 使用 Spring 的 StringUtils.hasText 判断空字符串
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            // 使用通用的JsonUtils进行反序列化
            return JsonUtils.parseMap(json);
        } catch (Exception e) {
            log.error("无法将 JSON 字符串解析为 Map: '{}'", json, e);
            throw new SQLException("解析JSON到Map时出错", e);
        }
    }
}