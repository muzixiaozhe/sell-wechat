package com.wechatorder.sell.service.impl;

import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.service.OrderService;
import com.wechatorder.sell.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayServiceImplTest {
    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;
    @Test
    public void create() throws Exception {
        OrderDTO orderDTO=orderService.findOne("123456");
        payService.create(orderDTO);
    }

}