package com.wechatorder.sell.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.service.PayService;
import com.wechatorder.sell.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    private static final String ORDER_NAME="微信点餐订单";
    @Autowired
    private BestPayServiceImpl bestPayService;
    @Override
    public void create(OrderDTO orderDTO) {
        PayRequest payRequest=new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信支付 request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse=bestPayService.pay(payRequest);
        log.info("微信支付 response={}",JsonUtil.toJson(payResponse));
    }
}
