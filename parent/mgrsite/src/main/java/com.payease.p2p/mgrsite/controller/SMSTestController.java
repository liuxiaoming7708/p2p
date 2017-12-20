package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.util.JSONResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 短信验证模拟接口
 * Created by liuxiaoming on 2017/6/21.
 */
@Controller
public class SMSTestController {

    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    public JSONResult send(String username,String password,String apikey,String mobile,String content){
        JSONResult jsonResult = new JSONResult();
        if (!(username.equals("root"))&&!(password.equals("111"))){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("短信用户名密码填写错误！");
            return  jsonResult;
        }
        if(!apikey.equals("1111")){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("短信网关填写错误！");
            return jsonResult;
        }
        System.out.println("用户名："+username+"，密码："+password+"，APIkey："+apikey
                +"，电话号码："+mobile +"，短信内容："+content);
        jsonResult.setSuccess(true);
        jsonResult.setMsg("success");
        return  jsonResult;
    }
}
