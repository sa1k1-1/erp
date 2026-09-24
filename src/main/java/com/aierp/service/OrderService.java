package com.aierp.service;

import com.aierp.common.result.PageResult;
import com.aierp.dto.Order.OrderCreateDTO;
import com.aierp.dto.Order.OrderItemCreateDTO;
import com.aierp.dto.Order.OrderPageQueryDTO;
import com.aierp.entity.Order;
import com.aierp.vo.OrderDetailVO;
import com.aierp.vo.OrderListVO;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

public interface OrderService extends IService<Order> {
    String createOrder(OrderCreateDTO dto);
    void cancelOrder(String orderNo);
    OrderDetailVO getOrder(String orderNo);
    void completeOrder(String orderNo);
    PageResult<OrderListVO> getOrderPage(OrderPageQueryDTO dto);
}
