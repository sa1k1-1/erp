package com.aierp.controller;

import com.aierp.common.result.Result;
import com.aierp.dto.product.ProductCreateDTO;
import com.aierp.dto.product.ProductUpdateDTO;
import com.aierp.entity.Product;
import com.aierp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    public ProductController( ProductService productService){
        this.productService = productService;
    }
    @PostMapping
    public Result<Long> createProduct(@Valid @RequestBody ProductCreateDTO dto){
        return Result.success(productService.createProduct(dto));
    }
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return Result.success();
    }
    @PutMapping("/{id}")
    public Result<Void> updateProduct(@PathVariable Long id,@Valid @RequestBody ProductUpdateDTO dto){
        productService.updateProduct(id,dto);
        return Result.success();
    }
    @GetMapping("/{id}")
    public Result<Product> getProduct(@PathVariable Long id){
        return Result.success(productService.getProduct(id));
    }
}
