package com.payease.p2p.base.event;

import com.payease.p2p.base.domain.VedioAuth;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 视频认证审核通过事件
 * Created by liuxiaoming on 2017/8/23.
 */
@Getter
public class VedioAuthSuccessEvent extends ApplicationEvent{


    private VedioAuth vedioAuth;

    public VedioAuthSuccessEvent(Object source,VedioAuth vedioAuth) {

        super(source);
        this.vedioAuth = vedioAuth;
    }

}
