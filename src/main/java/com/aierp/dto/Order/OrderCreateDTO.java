package com.aierp.dto.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @Size(max = 100,message = "订单备注不能超过个字符")
    private String remark;
    @Valid
    @NotEmpty(message = "订单商品不可为空")
    private List<OrderItemCreateDTO> items;
}
