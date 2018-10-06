package com.wechatorder.sell.repository;

import com.wechatorder.sell.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void saveTest(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("123456");
        orderMaster.setBuyerName("小哲");
        orderMaster.setBuyerPhone("17863699095");
        orderMaster.setBuyerAddress("北街家园六区");
        orderMaster.setOrderAmount(new BigDecimal(2.3));
        orderMaster.setBuyerOpenid("10254");

        OrderMaster result=repository.save(orderMaster);
        Assert.assertNotNull(result);
    }

}



















