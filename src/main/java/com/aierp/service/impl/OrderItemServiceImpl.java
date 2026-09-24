package com.aierp.service.impl;

import com.aierp.common.exception.BusinessException;
import com.aierp.common.result.ResultCode;
import com.aierp.dto.Order.OrderItemCreateDTO;
import com.aierp.entity.OrderItem;
import com.aierp.entity.Product;
import com.aierp.mapper.OrderItemMapper;
import com.aierp.service.OrderItemService;
import com.aierp.service.ProductService;
import com.aierp.vo.OrderItemVO;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper,OrderItem>
        implements OrderItemService {
    private final ProductService productService;
    public OrderItemServiceImpl(ProductService productService){
        this.productService = productService;
    }
    @Override
    public void createOrderItemList(List<OrderItemCreateDTO> list,Long orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for(OrderItemCreateDTO dto:list){
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setProductId(dto.getProductId());
            orderItem.setOrderId(orderId);
            Product product = productService.getProduct(dto.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItem.setUpdatedAt(LocalDateTime.now());
            orderItems.add(orderItem);
        }

        if(!this.saveBatch(orderItems))
            throw new BusinessException(ResultCode.BUSINESS_ERROR,"订单明细创建失败");
    }

    @Override
    public List<OrderItemVO> getOrderItemVOList(Long orderId) {
        List<OrderItem> list1 = lambdaQuery().eq(OrderItem::getOrderId,orderId).list();
        List<OrderItemVO> list2 = new ArrayList<>();
        for(OrderItem item:list1){
            OrderItemVO orderItemVO = new OrderItemVO();
            orderItemVO.setProductId(item.getProductId());
            orderItemVO.setId(item.getId());
            orderItemVO.setQuantity(item.getQuantity());
            orderItemVO.setProductName(item.getProductName());
            orderItemVO.setProductPrice(item.getProductPrice());
            orderItemVO.setSubtotal(item.getSubtotal());
            orderItemVO.setOrderId(orderId);
            orderItemVO.setCreatedAt(item.getCreatedAt());
            orderItemVO.setUpdatedAt(item.getUpdatedAt());
            list2.add(orderItemVO);
        }
        return list2;
    }

    @Override
    public List<OrderItem> getByOrderId(Long orderId) {
        return lambdaQuery().eq(OrderItem::getOrderId,orderId).list();
    }


}
