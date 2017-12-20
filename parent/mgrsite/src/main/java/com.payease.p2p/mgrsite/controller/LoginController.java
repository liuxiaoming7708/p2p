package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.domain.LoginInfo;
import com.payease.p2p.base.service.ILoginInfoService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台登陆管理
 * Created by liuxiaoming on 2017/6/16.
 */
@Controller
public class LoginController {
    @Autowired
    private ILoginInfoService loginInfoService;

    @RequestMapping("login")
    @ResponseBody
    public JSONResult login(String username, String password, HttpServletRequest request){
        JSONResult jsonResult = new JSONResult();
        LoginInfo loginInfo = this.loginInfoService.login(username, password,
                request.getRemoteAddr(), LoginInfo.USE_MANAGER);
        if (loginInfo == null){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("用户名或者密码错误！");
        }
        return jsonResult;

    }

}
