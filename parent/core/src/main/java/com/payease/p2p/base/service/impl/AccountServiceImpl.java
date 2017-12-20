package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.mapper.AccountMapper;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxiaoming on 2017/6/14.
 */
@Service
public class AccountServiceImpl implements IAccountService{

    @Autowired
    private AccountMapper accountMapper;
    @Override
    public void update(Account account) {
            int ret = this.accountMapper.updateByPrimaryKey(account);
        if(ret<=0){
            throw new RuntimeException("乐观锁失败：Account："+account.getId());
        }
    }

    @Override
    public void add(Account account) {
        this.accountMapper.insert(account);
    }

    @Override
    public Account getCurrent() {
        return this.accountMapper.selectByPrimaryKey(UserContext.getCurrent().getId());
    }

    @Override
    public Account get(Long id) {
        return this.accountMapper.selectByPrimaryKey(id);
    }

}
