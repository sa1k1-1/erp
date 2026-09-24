package com.aierp.dto.Order;

import lombok.Data;

@Data
public class OrderPageQueryDTO {

    private Long pageNum = 1L;

    private Long pageSize = 10L;

    // 可选条件
    private String orderNo;

    private Long userId;

    private Integer status;
}