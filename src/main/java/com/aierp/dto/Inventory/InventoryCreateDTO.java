package com.aierp.dto.Inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryCreateDTO {
    @NotNull(message = "商品id不能为空")
    private long productId;
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0,message = "库存数量不能小于0")
    private Integer stockQuantity;
    @NotNull(message = "预警库存数量不能为空")
    @Min(value = 0,message = "预警库存数量不能小于0")
    private Integer warningQuantity;
}
