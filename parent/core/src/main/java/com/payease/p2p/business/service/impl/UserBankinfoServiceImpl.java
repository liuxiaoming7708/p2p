package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.UserBankinfo;
import com.payease.p2p.business.mapper.UserBankinfoMapper;
import com.payease.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuxiaoming on 2017/8/10.
 */
@Service
public class UserBankinfoServiceImpl implements IUserBankinfoService{

    @Autowired
    private UserBankinfoMapper userBankinfoMapper;
    @Autowired
    private IUserInfoService userInfoService;
    @Override
    public UserBankinfo getBankInfoByUser(Long id) {
        return this.userBankinfoMapper.selectByUser(id);
    }

    @Override
    public void bind(UserBankinfo bankinfo) {
        //得到当前用户 ：1. 判断当前用户是否绑定银行卡 2. 判断当前用户是否实名认证
        Userinfo current = this.userInfoService.getCurrent();
        if(!current.getIsBindBank() && current.getIsRealAuth()){
         UserBankinfo bi = new UserBankinfo();
            bi.setAccountName(current.getRealName());
            bi.setAccountNumber(bankinfo.getAccountNumber());
            bi.setBankName(bankinfo.getBankName());
            bi.setForkName(bankinfo.getForkName());
            bi.setLogininfo(UserContext.getCurrent());
            this.userBankinfoMapper.insert(bi);
            //修改用户绑定银行卡状态
            current.addState(BitStatesUtils.OP_HAS_BIND_BANK);
            this.userInfoService.update(current);
        }
    }
}
