package com.wechatorder.sell.service.impl;

import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.enums.ResultEnum;
import com.wechatorder.sell.exception.SellException;
import com.wechatorder.sell.service.BuyerService;
import com.wechatorder.sell.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return  checkOrderOwer(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwer(openid, orderId);
        if (orderDTO==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwer(String openid, String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        /*if (orderDTO==null){
            return null;
        }*/
        //判断是否是自己的订单
        if (orderDTO.getBuyerOpenid().equalsIgnoreCase(openid)){
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }

}
