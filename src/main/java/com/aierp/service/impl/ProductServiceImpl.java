package com.aierp.service.impl;

import com.aierp.common.exception.BusinessException;
import com.aierp.common.result.ResultCode;
import com.aierp.dto.product.ProductCreateDTO;
import com.aierp.dto.product.ProductUpdateDTO;
import com.aierp.entity.Product;
import com.aierp.mapper.ProductMapper;
import com.aierp.service.ProductService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductServiceImpl
        extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    @Override
    public Long createProduct(ProductCreateDTO dto) {
        boolean exists = lambdaQuery()
                .eq(Product::getProductCode,dto.getProductCode())
                .exists();
        if(exists){throw new BusinessException(ResultCode.BUSINESS_ERROR,"商品编码已存在");}
        Product product = this.toEntity(dto);
        if(!this.save(product)){
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"商品创建失败");
        }
        return product.getId();
    }

    @Override
    public void deleteProduct(Long id) {
        if(this.getById(id) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND,"此商品不存在");
        }
        if (!this.removeById(id)) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "删除商品失败"
            );}

    }

    @Override
    public void updateProduct(Long id, ProductUpdateDTO dto) {
        if(this.getById(id) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND,"此商品不存在");
        }
        Product product = this.getById(id);
        this.toEntity(dto,product);
        if(!this.updateById(product)){
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"商品更新失败");
        }
    }

    @Override
    public Product getProduct(Long id) {
        Product product = this.getById(id);
        if(product == null) {
            throw new BusinessException(ResultCode.NOT_FOUND,"此商品不存在");
        }
        return product;
    }


    private Product toEntity(ProductCreateDTO dto){
        Product product = new Product();
        product.setProductCode(dto.getProductCode());
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setCostPrice(dto.getCostPrice());
        product.setDescription(dto.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }
    private void toEntity(ProductUpdateDTO dto, Product product){
        if (dto.getName()!=null) {
            product.setName(dto.getName());
        }
        if (dto.getCategory()!= null) {
            product.setCategory(dto.getCategory());
        }
        if (dto.getPrice()!=null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getCostPrice()!=null) {
            product.setCostPrice(dto.getCostPrice());
        }
        if (dto.getDescription()!=null) {
            product.setDescription(dto.getDescription());
        }
        product.setUpdatedAt(LocalDateTime.now());
    }
}
