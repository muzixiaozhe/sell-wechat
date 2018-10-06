package com.wechatorder.sell.controller;

import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 支付
 */
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public void create(@RequestParam("orderId") String orderId,
                       @RequestParam("returnUrl") String returnUrl){
        //查询订单
        OrderDTO orderDTO=orderService.findOne(orderId);
        if (orderDTO==null){

        }
    }
}
