package com.payease.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 绑定邮箱的验证后对象
 * Created by liuxiaoming on 2017/6/22.
 */
@Getter
@Setter
public class MailVerify extends BaseDomain{

    private String uuid;
    private Long logininfoId;
    private Date sendTime;
    private String email;
}
