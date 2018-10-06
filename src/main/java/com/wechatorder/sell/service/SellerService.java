package com.wechatorder.sell.service;

import com.wechatorder.sell.dataobject.SellerInfo;

public interface SellerService {
    /**
     * 通过openid查询卖家端信息
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
