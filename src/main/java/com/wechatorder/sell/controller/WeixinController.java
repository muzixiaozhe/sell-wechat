package com.wechatorder.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code){
        log.info("进入auth方法...");
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxa317db869fbd562d&secret=f1be9b7f74aa540a44a69048e42babbf&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate=new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response={}",response);
    }
}
