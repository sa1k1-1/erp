package com.aierp.controller;

import com.aierp.common.result.Result;
import com.aierp.dto.Inventory.InventoryCreateDTO;
import com.aierp.dto.Inventory.InventoryUpdateDTO;
import com.aierp.entity.Inventory;
import com.aierp.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }
    @PostMapping
    public Result<Void> createInventory(@Valid @RequestBody InventoryCreateDTO dto){
       inventoryService.createInventory(dto);
       return Result.success();
    }
    @DeleteMapping("/{productId}")
    public Result<Void> deleteInventory(@PathVariable Long productId){
        inventoryService.deleteInventory(productId);
        return Result.success();
    }
    @PutMapping("/{productId}")
    public Result<Void> updateInventory(@Valid @RequestBody InventoryUpdateDTO dto,@PathVariable Long productId){
        inventoryService.updateInventory(productId,dto);
        return Result.success();
    }
    @GetMapping("/{productId}")
    public Result<Inventory> getInventory(@PathVariable Long productId){
        return Result.success(inventoryService.getInventory(productId));
    }

}
