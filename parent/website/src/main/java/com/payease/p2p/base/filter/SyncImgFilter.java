package com.payease.p2p.base.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * 同步图片拦截器
 * Created by liuxiaoming on 2017/8/17.
 */
public class SyncImgFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
