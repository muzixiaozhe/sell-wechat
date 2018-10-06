package com.wechatorder.sell.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 卖家信息表
 */
@Data
@Entity
public class SellerInfo {
    @Id
    private String sellerId;

    private String username;

    private String password;

    private String openid;

    /** 创建时间*/
//    private Date createTime;

    /** 更新时间*/
//    private Date updateTime;
}
