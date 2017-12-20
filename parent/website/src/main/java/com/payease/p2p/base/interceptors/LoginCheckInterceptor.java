package com.payease.p2p.base.interceptors;

import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.base.util.UserContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆检查过滤器(前台)
 * Created by liuxiaoming on 2017/6/20.
 */
public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(handler instanceof HandlerMethod){
           HandlerMethod handlerMethod = (HandlerMethod)handler;
           //如果方法上面有标签，并且当前UserContext是空的，跳转到登陆页面
           if(handlerMethod.getMethodAnnotation(RequireLogin.class) != null&& UserContext.getCurrent() == null){
               System.out.println("前台。。登陆拦截器拦截。。。");
               response.sendRedirect("/login.html");
               return false;
           }
       }

        return super.preHandle(request,response,handler);
    }
}
