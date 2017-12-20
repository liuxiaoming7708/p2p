package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.Userinfo;

/**
 * 用户信息相关服务
 * Created by liuxiaoming on 2017/6/15.
 */
public interface IUserInfoService {
    /**
     * 乐观锁处理
     * @param userinfo
     */
    void update(Userinfo userinfo);

    /**
     * 添加用户信息
     * @param userinfo
     */
    void add(Userinfo userinfo);

    /**
     * 得到当前用户信息
     * @return
     */
    Userinfo getCurrent();

    /**
     * 根据ID获取userinfo对象
     * @param id
     * @return
     */
    Userinfo get(Long id);

    /**
     * 验证手机验证码并绑定
     * @param phoneNumber
     * @param verifyCode
     */
    void bindPhone(String phoneNumber, String verifyCode);

    /**
     * 验证邮箱并绑定
     * @param uuid
     */
    void bindEmail(String uuid);

    /**
     * 保存／修改用户基本资料
     * @param userinfo
     */
    void saveBasicInfo(Userinfo userinfo);


}
