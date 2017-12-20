package com.payease.p2p.base.util;

import com.payease.p2p.base.domain.LoginInfo;
import com.payease.p2p.base.vo.VerifyCodeVO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 用户上下文
 * Created by liuxiaoming on 2017/6/13.
 */
public class UserContext {
    public static final String LOGININFO_IN_SESSION = "logininfo";
    public static final String VERIFYCODE_IN_SESSION = "verifycode_in_session";

    public static HttpSession getSession(){
        return ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes()).getRequest().getSession();
    }

    public static void putCurrent(LoginInfo current){
        getSession().setAttribute(LOGININFO_IN_SESSION,current);
    }

    public static void logOutCurrent(){
        System.out.println("退出登陆！");
        getSession().removeAttribute(LOGININFO_IN_SESSION);
    }
    public static LoginInfo getCurrent(){
        return (LoginInfo)getSession().getAttribute(LOGININFO_IN_SESSION);
    }
    /**
     * 存放VerifyCodeVO
     */
    public static void putVerifyCode(VerifyCodeVO vo){
            getSession().setAttribute(VERIFYCODE_IN_SESSION,vo);
    }
    public static VerifyCodeVO getVerifyCode(){
        return (VerifyCodeVO)getSession().getAttribute(VERIFYCODE_IN_SESSION);
    }
}
