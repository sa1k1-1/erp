package com.aierp.service;


import com.aierp.dto.product.ProductCreateDTO;
import com.aierp.dto.product.ProductUpdateDTO;
import com.aierp.entity.Product;
import com.baomidou.mybatisplus.spring.service.IService;

public interface ProductService extends IService<Product> {

    Long createProduct(ProductCreateDTO dto);
    void deleteProduct(Long id);
    void updateProduct(Long id, ProductUpdateDTO dto);
    Product getProduct(Long id);
}
