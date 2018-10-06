package com.wechatorder.sell.converter;

import com.wechatorder.sell.dataobject.OrderMaster;
import com.wechatorder.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMaster2OrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList){
       List<OrderDTO> orderDTOList= orderMasterList.stream().map(e->convert(e)).collect(Collectors.toList());
       return orderDTOList;
    }
}
