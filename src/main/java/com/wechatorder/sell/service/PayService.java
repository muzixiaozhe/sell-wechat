package com.wechatorder.sell.service;

import com.wechatorder.sell.dto.OrderDTO;

/**
 * 支付
 */
public interface PayService {

    void create(OrderDTO orderDTO);
}
