package com.aierp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@TableName("inventory")
@Data
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Integer stockQuantity;
    private Integer lockedQuantity;
    private Integer warningQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
