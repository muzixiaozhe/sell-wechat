package com.wechatorder.sell.service;

public interface SeckillService {

    String querySeckillProductInfo(String productId);

    void orderProductMockDiffUser(String productId);
}
