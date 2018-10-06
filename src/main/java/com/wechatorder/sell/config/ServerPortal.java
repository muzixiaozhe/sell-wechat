package com.wechatorder.sell.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/weixin")
public class ServerPortal {
    private static final Logger LOG = LoggerFactory.getLogger(ServerPortal.class);
    @Autowired
    WeChatService wechatService;
    @RequestMapping(value="/verify",method= RequestMethod.GET)
    public void index(HttpServletRequest request, HttpServletResponse response){
        LOG.info("微信接入服务器");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String token = "weixin";
        String echostr = request.getParameter("echostr");
        if (wechatService.verifyInfo(signature, timestamp, nonce, token)) {
            LOG.info("echostr为:{}", echostr);
            if (echostr != null) {
                try {
                    response.getWriter().write(echostr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            LOG.info("signature为:{}", signature);
            LOG.info("timestamp为:{}", timestamp);
            LOG.info("nonce为:{}", nonce);
            LOG.info("token为:{}", token);
        }
    }
}
