package com.company.fastweb.core.data.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author FastWeb
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    NORMAL(0, "正常"),
    DISABLE(1, "停用"),
    DELETED(2, "删除");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    public static StatusEnum fromCode(Integer code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}