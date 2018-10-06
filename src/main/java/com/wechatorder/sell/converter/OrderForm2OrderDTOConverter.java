package com.wechatorder.sell.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wechatorder.sell.dataobject.OrderDetail;
import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.enums.ResultEnum;
import com.wechatorder.sell.exception.SellException;
import com.wechatorder.sell.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

public class OrderForm2OrderDTOConverter {

    public static OrderDTO convert(OrderForm orderForm){

        Gson gson=new Gson();

        OrderDTO orderDTO=new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail> orderDetailList=new ArrayList<>();
        try {
            orderDetailList= gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
