package com.payease.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 基础登陆信息
 * Created by liuxiaoming on 2017/5/16.
 */
@Getter
@Setter
public class LoginInfo extends BaseDomain{
    public static final int USER_NORMAL = 0; //前台用户
    public static final int USE_MANAGER = 1; //后台用户
    //用户的正常登陆状态
    public static final int STATE_NORMAL = 0;

    private String username;
    private String password;
    private int state;
    private int userType = USER_NORMAL;

    @Override
    public String toString() {
        return "LoginInfo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", userType=" + userType +
                '}';
    }
}
