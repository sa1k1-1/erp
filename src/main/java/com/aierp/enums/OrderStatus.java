package com.aierp.enums;

import lombok.Getter;

import javax.swing.*;

@Getter
public enum OrderStatus {

    CREATED(0, "已创建"),
    CANCELLED(1, "已取消"),
    COMPLETED(2, "已完成");

    private final Integer code;
    private final String description;

    OrderStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatus fromCode(Integer code) {

        for (OrderStatus status : OrderStatus.values()) {

            if (status.getCode().equals(code)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "未知订单状态：" + code
        );
    }
}