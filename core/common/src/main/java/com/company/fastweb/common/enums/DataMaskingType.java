package com.company.fastweb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据脱敏类型枚举
 */
@Getter
@AllArgsConstructor
public enum DataMaskingType {

    /**
     * 姓名
     */
    NAME("name", "姓名"),

    /**
     * 身份证号
     */
    ID_CARD("idCard", "身份证号"),

    /**
     * 手机号
     */
    PHONE("phone", "手机号"),

    /**
     * 邮箱
     */
    EMAIL("email", "邮箱"),

    /**
     * 银行卡号
     */
    BANK_CARD("bankCard", "银行卡号"),

    /**
     * 地址
     */
    ADDRESS("address", "地址");

    private final String type;
    private final String description;
}