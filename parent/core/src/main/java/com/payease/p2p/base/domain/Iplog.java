package com.payease.p2p.base.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 登陆日志
 * Created by liuxiaoming on 2017/6/15.
 */
@Getter
@Setter
public class Iplog extends BaseDomain{

    public static final int STATE_SUCCESS = 0;
    public static final int STATE_FAILED = 1;

    private String ip;
    private Date loginTime;
    private int state = STATE_SUCCESS;
    private String username;
    private int userType;

    public String getStateDisplay(){
        return state == STATE_SUCCESS ?"登陆成功":"登陆失败";
    }
    public String getUserTypeDisplay(){return userType == LoginInfo.USE_MANAGER?"管理员":"用户";}

}
