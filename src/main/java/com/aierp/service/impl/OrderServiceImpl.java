package com.aierp.service.impl;

import com.aierp.common.exception.BusinessException;
import com.aierp.common.result.PageResult;
import com.aierp.common.result.ResultCode;
import com.aierp.dto.Order.OrderCreateDTO;
import com.aierp.dto.Order.OrderItemCreateDTO;
import com.aierp.dto.Order.OrderPageQueryDTO;
import com.aierp.entity.Order;
import com.aierp.entity.OrderItem;
import com.aierp.enums.OrderStatus;
import com.aierp.mapper.OrderMapper;
import com.aierp.service.InventoryService;
import com.aierp.service.OrderItemService;
import com.aierp.service.OrderService;
import com.aierp.service.ProductService;
import com.aierp.vo.OrderDetailVO;
import com.aierp.vo.OrderListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final InventoryService inventoryService;
    public OrderServiceImpl(ProductService productService, OrderItemService orderItemService,InventoryService inventoryService){
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.inventoryService = inventoryService;
    }
    @Transactional
    @Override
    public String createOrder(OrderCreateDTO dto) {
        if (dto == null || dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "订单商品不能为空"
            );
        }
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setOrderNo("ORD"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+dto.getUserId()+ ThreadLocalRandom.current().nextInt(1000, 10000));
        order.setRemark(dto.getRemark());
        order.setStatus(OrderStatus.CREATED.getCode());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemCreateDTO orderItemCreateDTO:dto.getItems()) {
            //遍历商品锁定库存
            inventoryService.lockStock(orderItemCreateDTO.getProductId(), orderItemCreateDTO.getQuantity());
            BigDecimal subtotal = productService.getProduct(orderItemCreateDTO.getProductId()).getPrice().multiply(BigDecimal.valueOf(orderItemCreateDTO.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }
        order.setTotalAmount(totalAmount);
        if (!this.save(order)) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "订单创建失败"
            );
        }
        orderItemService.createOrderItemList(dto.getItems(),order.getId());
        return order.getOrderNo();
    }
    @Transactional
    @Override
    public void cancelOrder(String orderNo) {
        Order order = getOrderOrThrow(orderNo);
        if (order.getStatus() != 0) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "当前订单状态不允许取消"
            );
        }
        List<OrderItem> items =
                orderItemService.getByOrderId(order.getId());
        for (OrderItem item : items) {
            inventoryService.releaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setUpdatedAt(LocalDateTime.now());
        if(!this.updateById(order)) throw new BusinessException(ResultCode.BUSINESS_ERROR,"订单更新失败");
    }

    @Override
    public OrderDetailVO getOrder(String orderNo) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        Order order = this.getOrderOrThrow(orderNo);
        orderDetailVO.setId(order.getId());
        orderDetailVO.setOrderNo(order.getOrderNo());
        orderDetailVO.setUserId(order.getUserId());
        orderDetailVO.setTotalAmount(order.getTotalAmount());
        orderDetailVO.setStatus(order.getStatus());
        orderDetailVO.setRemark(order.getRemark());
        orderDetailVO.setCreatedAt(order.getCreatedAt());
        orderDetailVO.setUpdatedAt(order.getUpdatedAt());
        orderDetailVO.setOrderItems(
                orderItemService.getOrderItemVOList(order.getId())
        );
        return orderDetailVO;
    }
    @Transactional
    @Override
    public void completeOrder(String orderNo) {

        Order order = getOrderOrThrow(orderNo);

        if (order.getStatus() != 0) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "当前订单状态不允许完成"
            );
        }

        List<OrderItem> items =
                orderItemService.getByOrderId(order.getId());

        for (OrderItem item : items) {
            inventoryService.completeStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setUpdatedAt(LocalDateTime.now());

        if (!this.updateById(order)) {
            throw new BusinessException(
                    ResultCode.BUSINESS_ERROR,
                    "完成订单操作失败"
            );
        }
    }

    @Override
    public PageResult<OrderListVO> getOrderPage(OrderPageQueryDTO dto) {

        Page<Order> page = new Page<>(
                dto.getPageNum(),
                dto.getPageSize()
        );

        LambdaQueryWrapper<Order> wrapper =
                new LambdaQueryWrapper<>();

        // 订单号
        wrapper.like(
                dto.getOrderNo() != null
                        && !dto.getOrderNo().isBlank(),
                Order::getOrderNo,
                dto.getOrderNo()
        );

        // 用户
        wrapper.eq(
                dto.getUserId() != null,
                Order::getUserId,
                dto.getUserId()
        );

        // 状态
        wrapper.eq(
                dto.getStatus() != null,
                Order::getStatus,
                dto.getStatus()
        );

        // 最新订单在前
        wrapper.orderByDesc(Order::getCreatedAt);

        Page<Order> result =
                this.page(page, wrapper);

        List<OrderListVO> records =
                result.getRecords()
                        .stream()
                        .map(this::convertToListVO)
                        .toList();

        return new PageResult<>(
                result.getTotal(),
                result.getPages(),
                result.getCurrent(),
                result.getSize(),
                records
        );
    }

    private Order getByOrderNo(String orderNo){
        return lambdaQuery().eq(Order::getOrderNo,orderNo).one();
    }
    private Order getOrderOrThrow(String orderNo) {
        Order order = getByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(
                    ResultCode.NOT_FOUND,
                    "订单不存在"
            );
        }
        return order;
    }
    private OrderListVO convertToListVO(Order order) {

        OrderListVO vo = new OrderListVO();

        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());

        vo.setStatusName(
                OrderStatus.fromCode(order.getStatus())
                        .getDescription()
        );

        vo.setRemark(order.getRemark());
        vo.setCreatedAt(order.getCreatedAt());
        vo.setUpdatedAt(order.getUpdatedAt());

        return vo;
    }

}
