package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.LoginInfo;

import java.util.List;
import java.util.Map;

/**
 * 登录相关服务
 * Created by liuxiaoming on 2017/5/16.
 */
public interface ILoginInfoService {
    /**
     * 注册
     * @param username
     * @param password
     */
    void register(String username,String password);

    /**
     * 检查用户名是否存在 用户名存在返回true 用户名不存在返回false
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 登陆
     * @param username
     * @param password
     * @param ip
     * @param userType
     * @return
     */
    LoginInfo login(String username, String password, String ip, int userType);

    /**
     * 系统初始化第一个管理员
     */
    void initAdmin();

    /**
     * 用户自动补全
     * @param keyword
     * @return
     */
    List<Map<String,Object>> autoComplate(String keyword);

    /**
     * 用户登出
     */
    void logout();
}
