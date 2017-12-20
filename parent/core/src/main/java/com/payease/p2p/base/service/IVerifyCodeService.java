package com.payease.p2p.base.service;

import com.payease.p2p.base.util.JSONResult;

/**
 * 验证码相关服务
 * Created by liuxiaoming on 2017/6/20.
 */
public interface IVerifyCodeService {
    /**
     * 发送验证码
     * @param phoneNumber
     */
    JSONResult sendVerifyCode(String phoneNumber);

    /**
     * 验证验证码
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    boolean validate(String phoneNumber, String verifyCode);
}
