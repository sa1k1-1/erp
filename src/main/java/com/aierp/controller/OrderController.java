package com.aierp.controller;

import com.aierp.common.result.PageResult;
import com.aierp.common.result.Result;
import com.aierp.dto.Order.OrderCreateDTO;
import com.aierp.dto.Order.OrderPageQueryDTO;
import com.aierp.service.OrderService;
import com.aierp.vo.OrderDetailVO;
import com.aierp.vo.OrderListVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<String> createOrder(
            @Valid @RequestBody OrderCreateDTO dto) {
        return Result.success(orderService.createOrder(dto));
    }
    @GetMapping("/page")
    public Result<PageResult<OrderListVO>> getOrderPage(
            OrderPageQueryDTO dto) {

        return Result.success(
                orderService.getOrderPage(dto)
        );
    }

    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrder(
            @PathVariable String orderNo) {
        return Result.success(
                orderService.getOrder(orderNo)
        );
    }

    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(
            @PathVariable String orderNo) {
        orderService.cancelOrder(orderNo);
        return Result.success();
    }

    @PutMapping("/{orderNo}/complete")
    public Result<Void> completeOrder(
            @PathVariable String orderNo) {
        orderService.completeOrder(orderNo);
        return Result.success();
    }
}

