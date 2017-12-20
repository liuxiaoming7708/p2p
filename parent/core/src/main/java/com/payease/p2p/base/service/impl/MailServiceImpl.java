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
public class MailServiceImpl implements ApplicationListener<ApplicationEvent> {

    private void sendMail(RealAuth ra) {
        System.out.println("发送实名认证邮件！");
    }

    private void sendMail(VedioAuth va) {
        System.out.println("发送视频认证邮件！");
    }
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof RealAuthSuccessEvent){
            this.sendMail(((RealAuthSuccessEvent)event).getRealAuth());
        }else if(event instanceof VedioAuthSuccessEvent){
            this.sendMail(((VedioAuthSuccessEvent)event).getVedioAuth());
        }
    }
}
