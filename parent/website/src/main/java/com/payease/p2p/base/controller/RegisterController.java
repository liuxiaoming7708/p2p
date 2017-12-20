package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.LoginInfo;
import com.payease.p2p.base.service.ILoginInfoService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 注册
 * Created by liuxiaoming on 2017/5/18.
 */
@Controller
public class RegisterController {

    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("register")
    @ResponseBody
    public JSONResult register(String username,String password){
        JSONResult jsonResult = new JSONResult();
        try {
            this.loginInfoService.register(username,password);
        }catch (RuntimeException re){
            re.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMsg(re.getMessage());
        }
        return  jsonResult;
    }

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    @RequestMapping("checkUsername")
    @ResponseBody
    public Boolean checkUsername(String username) {
        return !this.loginInfoService.checkUsername(username);
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public JSONResult login(String username, String password, HttpServletRequest request){
        LoginInfo loginInfo = this.loginInfoService.login(username, password,request.getRemoteAddr(),LoginInfo.USER_NORMAL);
        JSONResult jsonResult = new JSONResult();
        if(loginInfo == null){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("用户名或者密码错误！！");
        }
        return jsonResult;
    }

    @RequestMapping("logout")
    public String logout(){
        this.loginInfoService.logout();
        return "login";
    }
}
