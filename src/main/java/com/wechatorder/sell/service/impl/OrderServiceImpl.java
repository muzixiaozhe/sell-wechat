package com.wechatorder.sell.service.impl;

import com.wechatorder.sell.converter.OrderMaster2OrderDTOConverter;
import com.wechatorder.sell.dataobject.OrderDetail;
import com.wechatorder.sell.dataobject.OrderMaster;
import com.wechatorder.sell.dataobject.ProductInfo;
import com.wechatorder.sell.dto.CartDTO;
import com.wechatorder.sell.dto.OrderDTO;
import com.wechatorder.sell.enums.OrderStatusEnum;
import com.wechatorder.sell.enums.PayStatusEnum;
import com.wechatorder.sell.enums.ResultEnum;
import com.wechatorder.sell.exception.SellException;
import com.wechatorder.sell.repository.OrderDetailRepository;
import com.wechatorder.sell.repository.OrderMasterRepository;
import com.wechatorder.sell.service.OrderService;
import com.wechatorder.sell.service.ProductService;
import com.wechatorder.sell.service.PushMessageService;
import com.wechatorder.sell.service.WebSocket;
import com.wechatorder.sell.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private WebSocket webSocket;
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId=KeyUtil.genUniqueKey();
        BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);
//      List<CartDTO> cartDTOList=new ArrayList<>();
        //查询商品(数量,价格)
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
           ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
           if (productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //计算订单总价
           orderAmount=productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);
            //订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);

            orderDetailRepository.save(orderDetail);

            /*CartDTO cartDTO=new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);*/
        }
        //写入订单数据库
        OrderMaster orderMaster=new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderAmount(orderAmount);
        orderMasterRepository.save(orderMaster);

        //扣库存
        List<CartDTO> cartDTOList=orderDTO.getOrderDetailList().stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        //发送websocket消息
        webSocket.sendMessage("有新的订单");

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster=orderMasterRepository.findOne(orderId);
        if (orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList=orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyeOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage= orderMasterRepository.findByBuyerOpenid(buyeOpenid,pageable);
        List<OrderDTO> orderDTOList= OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster=new OrderMaster();

        //判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDTO.getOrderStatus())){
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if (updateResult==null){
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList=orderDTO.getOrderDetailList().stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        //如果已支付,需要退款
        if (PayStatusEnum.SUCCESS.equals(orderDTO.getPayStatus())){
            //TODO
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDTO.getOrderStatus())){
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if (updateResult==null){
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //推送微信模板消息
        pushMessageService.orderStatus(orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if (!OrderStatusEnum.NEW.getCode().equals(orderDTO.getOrderStatus())){
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!PayStatusEnum.WAIT.equals(orderDTO.getPayStatus())){
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if (updateResult==null){
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList= OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        return new PageImpl<>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }
}
