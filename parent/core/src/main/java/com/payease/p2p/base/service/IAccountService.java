package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.Account;

/**
 * 账户相关服务
 * Created by liuxiaoming on 2017/6/14.
 */
public interface IAccountService {
    /**
     * 修改操作（处理乐观锁问题）
     * @param account
     */
    void update(Account account);

    /**
     * 添加账户信息
     * @param account
     */
    void add(Account account);

    /**
     * 得到当前登陆用户账户
     * @return
     */
    Account getCurrent();

    Account get(Long id);

}
