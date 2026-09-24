package com.aierp.mapper;
import com.aierp.entity.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface InventoryMapper extends BaseMapper<Inventory> {
    @Update("""
UPDATE inventory
SET locked_quantity = locked_quantity + #{quantity},
    updated_at = NOW()
WHERE product_id = #{productId}
AND stock_quantity-locked_quantity >= #{quantity} """)
    int lockStock(@Param("productId") Long productId,
                  @Param("quantity") Integer quantity);

    @Update("""
    UPDATE inventory
    SET locked_quantity = locked_quantity - #{quantity},
        updated_at = NOW()
    WHERE product_id = #{productId}
      AND locked_quantity >= #{quantity}
    """)
    int releaseStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );

    @Update("""
    UPDATE inventory
    SET stock_quantity = stock_quantity - #{quantity},
        locked_quantity = locked_quantity - #{quantity},
        updated_at = NOW()
    WHERE product_id = #{productId}
      AND stock_quantity >= #{quantity}
      AND locked_quantity >= #{quantity}
    """)
    int completeStock(
            @Param("productId") Long productId,
            @Param("quantity") Integer quantity
    );
}
