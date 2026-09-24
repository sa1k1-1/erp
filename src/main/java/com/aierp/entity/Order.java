    package com.aierp.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableName;
    import lombok.Data;
    import java.math.BigDecimal;
    import java.time.LocalDateTime;

    @TableName("orders")
    @Data
    public class Order {
        @TableId(type = IdType.AUTO)
        private Long id;
        private String orderNo;
        private Long userId;
        private BigDecimal totalAmount;
        private Integer status;
        private String remark;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
