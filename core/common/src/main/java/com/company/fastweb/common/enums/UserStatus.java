package com.company.fastweb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL("0", "正常"),
    DISABLED("1", "禁用"),
    LOCKED("2", "锁定"),
    EXPIRED("3", "过期");

    private final String code;
    private final String description;

    public static UserStatus of(String code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}