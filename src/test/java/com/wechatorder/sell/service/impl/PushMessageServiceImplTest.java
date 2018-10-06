package com.wechatorder.sell.service.impl;

import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PushMessageServiceImplTest {
    @Autowired
    private PushMessageServiceImpl pushMessageService;
    @Autowired
    private OrderService orderService;
    @Test
    public void orderStatus() throws Exception {
        OrderDTO orderDTO = orderService.findOne("123456");
        pushMessageService.orderStatus(orderDTO);
    }

}