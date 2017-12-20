package com.payease.p2p.base.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 包装 验证码，手机，发送时间
 * Created by liuxiaoming on 2017/6/20.
 */
@Setter
@Getter
public class VerifyCodeVO {
    private String code;
    private String phoneNumber;
    private Date sendTime;

    public VerifyCodeVO(String code, String phoneNumber, Date sendTime) {
        super();
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.sendTime = sendTime;
    }

    public VerifyCodeVO() {
        super();
    }
}
