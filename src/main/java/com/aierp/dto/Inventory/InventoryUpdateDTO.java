package com.aierp.dto.Inventory;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class InventoryUpdateDTO {

    @Min(value = 0, message = "库存数量不能小于0")
    private Integer stockQuantity;

    @Min(value = 0, message = "预警库存数量不能小于0")
    private Integer warningQuantity;
}