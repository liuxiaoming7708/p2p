package com.payease.p2p.mgrsite.listeners;

import com.payease.p2p.base.service.ILoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by liuxiaoming on 2017/6/16.
 */
@Component
public class InitAdminListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private ILoginInfoService loginInfoService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("spring监听器！！！！");
        this.loginInfoService.initAdmin();
    }
}
