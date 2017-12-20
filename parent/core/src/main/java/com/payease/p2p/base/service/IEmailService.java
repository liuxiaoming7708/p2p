package com.payease.p2p.base.service;

import com.payease.p2p.base.util.JSONResult;

/**
 * 发送邮件相关服务
 * Created by liuxiaoming on 2017/6/22.
 */
public interface IEmailService {
    /**
     * 发送验证邮箱的邮件
     * @param email
     */
    JSONResult sendVerifyEmail(String email);
}
