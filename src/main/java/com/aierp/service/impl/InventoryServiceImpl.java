package com.aierp.service.impl;

import com.aierp.common.exception.BusinessException;
import com.aierp.common.result.ResultCode;
import com.aierp.dto.Inventory.InventoryCreateDTO;
import com.aierp.dto.Inventory.InventoryUpdateDTO;
import com.aierp.entity.Inventory;
import com.aierp.entity.Product;
import com.aierp.mapper.InventoryMapper;
import com.aierp.service.InventoryService;
import com.aierp.service.ProductService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InventoryServiceImpl
        extends ServiceImpl<InventoryMapper, Inventory>
        implements InventoryService {
    private final ProductService productService;

    public InventoryServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createInventory(InventoryCreateDTO dto) {
        if(productService.getById(dto.getProductId())==null) throw new BusinessException(ResultCode.NOT_FOUND,"未找到此商品相关信息");
        Inventory inventory = createDTOToEntity(dto);
        if(getByProductId(inventory.getProductId())!=null) throw new BusinessException(ResultCode.BUSINESS_ERROR,"该商品的库存记录已存在");
        if(inventory.getStockQuantity()<0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"当前库存数量必须大于等于0");
        if(inventory.getWarningQuantity()<0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"预警库存数量必须大于等于0");
        inventory.setLockedQuantity(0);

        if(!this.save(inventory)) throw new BusinessException(ResultCode.BUSINESS_ERROR,"库存记录创建失败");
    }

    @Override
    public void updateInventory(Long productId, InventoryUpdateDTO dto) {
        Inventory inventory = this.getByProductId(productId);
        if (inventory==null) {
            throw new BusinessException(
                    ResultCode.NOT_FOUND,
                    "未查出该商品的库存记录"
            );
        }
        this.updateDTOToEntity(inventory,dto);
        if(inventory.getLockedQuantity()>inventory.getStockQuantity()){
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"锁定库存数量不可大于当前库存数量");
        }
        if(inventory.getWarningQuantity()<0){
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"预警库存数量不可小于0");
        }
        if (!this.updateById(inventory)) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"库存更新失败");
        }
    }

    @Override
    public void deleteInventory(Long productId) {
        Inventory inventory = getByProductId(productId);
        if(inventory==null) throw new BusinessException(ResultCode.NOT_FOUND,"该库存记录不存在");
        if(inventory.getStockQuantity()>0||inventory.getLockedQuantity()>0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"当前库存或锁定库存数量大于0,不建议删除");
        if(!this.removeById(inventory.getId())){throw new BusinessException(ResultCode.BUSINESS_ERROR,"删除失败");};
    }

    @Override
    public Inventory getInventory(Long productId) {
        Inventory inventory = getByProductId(productId);
        if(inventory==null) throw new BusinessException(ResultCode.NOT_FOUND,"未查到该商品对应的库存记录");
        return inventory;
    }
    private Inventory createDTOToEntity(InventoryCreateDTO dto){
        Inventory inventory = new Inventory();
        inventory.setProductId(dto.getProductId());
        inventory.setStockQuantity(dto.getStockQuantity());
        inventory.setWarningQuantity(dto.getWarningQuantity());
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        return inventory;
    }
    private void updateDTOToEntity(Inventory inventory,InventoryUpdateDTO dto){
        if (dto.getStockQuantity() != null) {
            inventory.setStockQuantity(dto.getStockQuantity());
        }
        if (dto.getWarningQuantity() != null) {
            inventory.setWarningQuantity(dto.getWarningQuantity());
        }
        inventory.setUpdatedAt(LocalDateTime.now());

    }
    @Override
    public Inventory getByProductId(Long productId){
        return lambdaQuery().eq(Inventory::getProductId, productId).one();

    }
    @Override
    public void lockStock(Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "锁定库存数量必须大于0"
            );
        }

        if (productService.getProduct(productId).getStatus() == 1) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "该商品当前不可购买"
            );
        }
        this.getInventory(productId);
        int affectedRows =
                baseMapper.lockStock(productId,quantity);

        if (affectedRows == 0) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "商品库存不足"
            );
        }



    }
    @Override
    public void completeStock(Long productId, Integer quantity){
        if(quantity<=0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"操作库存数量不可小于等于0");
        this.getInventory(productId);
        int affectedRows = baseMapper.completeStock(productId,quantity);
        if(affectedRows==0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"库存状态异常，扣减库存失败");
    }
    @Override
    public void releaseStock(Long productId,Integer quantity){
        if(quantity<=0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"操作库存数量必须大于0");
        this.getInventory(productId);
        int affectedRows = baseMapper.releaseStock(productId,quantity);
        if(affectedRows==0) throw new BusinessException(ResultCode.BUSINESS_ERROR,"锁定库存不足，释放库存失败");
    }
}
