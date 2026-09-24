package com.aierp.service;

import com.aierp.dto.Order.OrderItemCreateDTO;
import com.aierp.entity.OrderItem;
import com.aierp.vo.OrderDetailVO;
import com.aierp.vo.OrderItemVO;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

public interface OrderItemService extends IService<OrderItem> {
    void createOrderItemList(List<OrderItemCreateDTO> list, Long orderID);
    List<OrderItemVO> getOrderItemVOList(Long orderId);
    List<OrderItem> getByOrderId(Long orderId);

}
