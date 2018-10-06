package com.wechatorder.sell.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层对象
 */
@Data
public class ResultVO<T> implements Serializable{

    private static final long serialVersionUID = -4113166743951175782L;
    /** 错误码. */
    private Integer code;
    /** 提示信息. */
    private String msg;
    /** 具体内容. */
    private T data;
}
