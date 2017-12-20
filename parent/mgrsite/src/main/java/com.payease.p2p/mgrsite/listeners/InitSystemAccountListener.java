package com.payease.p2p.mgrsite.listeners;

import com.payease.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 创建系统账户
 * Created by liuxiaoming on 2017/8/4.
 */
@Component
public class InitSystemAccountListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private ISystemAccountService systemAccountService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
            this.systemAccountService.initAccount();
    }
}
