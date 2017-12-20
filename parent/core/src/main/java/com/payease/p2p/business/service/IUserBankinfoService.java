package com.payease.p2p.business.service;

import com.payease.p2p.business.domain.UserBankinfo;

/**
 * 用户银行卡相关
 * Created by liuxiaoming on 2017/8/10.
 */
public interface IUserBankinfoService {

    /**
     * 根据用户id查询用户绑定的银行卡
     *
     * @param id
     * @return
     */
    UserBankinfo getBankInfoByUser(Long id);

    /**
     * 用户绑定银行卡
     * @param bankinfo
     */
    void bind(UserBankinfo bankinfo);
}
