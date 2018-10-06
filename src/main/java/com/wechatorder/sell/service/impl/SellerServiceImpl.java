package com.wechatorder.sell.service.impl;

import com.wechatorder.sell.dataobject.SellerInfo;
import com.wechatorder.sell.repository.SellerInfoRepository;
import com.wechatorder.sell.service.SellerService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
