package com.aierp.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateDTO {

    @Size(max = 30, message = "商品名称长度不能超过30个字符")
    private String name;

    @Size(max = 20, message = "商品分类长度不能超过20个字符")
    private String category;

    @DecimalMin(value = "0.00", inclusive = true, message = "销售价格不能小于0")
    private BigDecimal price;

    @DecimalMin(value = "0.00", inclusive = true, message = "成本价不能小于0")
    private BigDecimal costPrice;

    @Size(max = 500, message = "商品描述长度不能超过500个字符")
    private String description;

    private Integer status;
}