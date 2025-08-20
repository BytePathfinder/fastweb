package com.company.fastweb.core.data.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 表信息视图对象
 *
 * @author FastWeb
 */
@Data
public class TableInfoVO {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 表类型
     */
    private String tableType;

    /**
     * 存储引擎
     */
    private String engine;

    /**
     * 表行数
     */
    private Long tableRows;

    /**
     * 数据长度（字节）
     */
    private Long dataLength;

    /**
     * 索引长度（字节）
     */
    private Long indexLength;

    /**
     * 表大小（格式化后的字符串）
     */
    private String tableSize;

    /**
     * 平均行长度
     */
    private Long avgRowLength;

    /**
     * 最大数据长度
     */
    private Long maxDataLength;

    /**
     * 数据空闲空间
     */
    private Long dataFree;

    /**
     * 自增值
     */
    private Long autoIncrement;

    /**
     * 字符集
     */
    private String tableCollation;

    /**
     * 校验和
     */
    private Long checksum;

    /**
     * 创建选项
     */
    private String createOptions;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 检查时间
     */
    private Date checkTime;

    /**
     * 表状态
     */
    private String tableStatus;

    /**
     * 分区信息
     */
    private String partitionInfo;

    /**
     * 字段信息列表
     */
    private List<Map<String, Object>> columns;

    /**
     * 索引信息列表
     */
    private List<Map<String, Object>> indexes;

    /**
     * 外键信息列表
     */
    private List<Map<String, Object>> foreignKeys;

    /**
     * 触发器信息列表
     */
    private List<Map<String, Object>> triggers;

    /**
     * 表权限信息
     */
    private Map<String, Object> privileges;

    /**
     * 表统计信息
     */
    private Map<String, Object> statistics;

    /**
     * 表碎片信息
     */
    private Map<String, Object> fragmentInfo;

    /**
     * 表性能信息
     */
    private Map<String, Object> performanceInfo;

    /**
     * 备注信息
     */
    private String remark;
}