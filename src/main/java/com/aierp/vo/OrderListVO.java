package com.aierp.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListVO {

    private Long id;

    private String orderNo;

    private Long userId;

    private BigDecimal totalAmount;

    private Integer status;

    private String statusName;

    private String remark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}