package com.payease.p2p.base.controller;

import com.payease.p2p.base.service.IEmailService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 发送绑定邮箱
 * Created by liuxiaoming on 2017/6/22.
 */
@Controller
public class SendEmailController {

    @Autowired
    private IEmailService emailService;

    @RequestMapping("sendEmail")
    @ResponseBody
    public JSONResult sendEmail(String email){
        JSONResult jsonResult = new JSONResult();
        try{
            jsonResult = this.emailService.sendVerifyEmail(email);
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }
}
