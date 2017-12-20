package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.RealAuth;
import com.payease.p2p.base.domain.VedioAuth;
import com.payease.p2p.base.event.RealAuthSuccessEvent;
import com.payease.p2p.base.event.VedioAuthSuccessEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * Created by liuxiaoming on 2017/8/20.
 */
@Service
public class SmsServiceImpl implements ApplicationListener<ApplicationEvent> {

    private void sendSms(RealAuth ra) {
        System.out.println("发送实名认证短信！");
    }
    private void sendSms(VedioAuth va) {
        System.out.println("发送视频认证短信！");
    }

    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof RealAuthSuccessEvent){
            this.sendSms(((RealAuthSuccessEvent)event).getRealAuth());
        }else if(event instanceof VedioAuthSuccessEvent){
            this.sendSms(((VedioAuthSuccessEvent)event).getVedioAuth());
        }
    }

}
