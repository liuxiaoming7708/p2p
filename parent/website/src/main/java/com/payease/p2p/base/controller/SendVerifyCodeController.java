package com.payease.p2p.base.controller;

import com.payease.p2p.base.service.IVerifyCodeService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 模拟第三方短信网关接口
 * 发送验证码控制器
 * Created by liuxiaoming on 2017/6/20.
 */
@Controller
public class SendVerifyCodeController {
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @RequestMapping("sendVerifyCode")
    @ResponseBody
    public JSONResult sendVerifyCode(String phoneNumber){
        JSONResult jsonResult = new JSONResult();
        try{
            jsonResult = this.verifyCodeService.sendVerifyCode(phoneNumber);
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }
}
