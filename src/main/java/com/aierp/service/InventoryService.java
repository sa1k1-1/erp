package com.aierp.service;

import com.aierp.dto.Inventory.InventoryCreateDTO;
import com.aierp.dto.Inventory.InventoryUpdateDTO;
import com.aierp.entity.Inventory;
import com.baomidou.mybatisplus.spring.service.IService;

public interface InventoryService extends IService<Inventory> {
    void createInventory(InventoryCreateDTO dto);

    void updateInventory(Long productId, InventoryUpdateDTO dto);

    void deleteInventory(Long productId);

    Inventory getInventory(Long productId);

    Inventory getByProductId(Long productId);

    void lockStock(Long productId, Integer quantity);

    void completeStock(Long productId,Integer quantity );

    void releaseStock(Long productId,Integer quantity );
}
