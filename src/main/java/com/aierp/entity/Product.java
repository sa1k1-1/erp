package com.aierp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@TableName("product")
@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String productCode;
    private String name;
    private String category;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
