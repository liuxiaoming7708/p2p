package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.business.domain.*;
import com.payease.p2p.business.mapper.SystemAccountFlowMapper;
import com.payease.p2p.business.mapper.SystemAccountMapper;
import com.payease.p2p.business.service.ISystemAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liuxiaoming on 2017/8/4.
 */
@Service
public class SystemAccountServiceImpl implements ISystemAccountService {

    @Autowired
    private SystemAccountMapper systemAccountMapper;
    @Autowired
    private SystemAccountFlowMapper systemAccountFlowMapper;

    @Override
    public void update(SystemAccount account) {

        int ret = this.systemAccountMapper.updateByPrimaryKey(account);
        if(ret<0){throw new RuntimeException("乐观锁失败：SystemAccount");}
    }

    @Override
    public void initAccount() {
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        if(current == null){
            current  = new SystemAccount();
            this.systemAccountMapper.insert(current);
        }

    }

    @Override
    public void chargeBorrowFee(BigDecimal fee, BidRequest bidRequest) {
        //获取当前系统账户
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        current.setUseableAmount(current.getUseableAmount().add(fee));
        //生成系统账户流水
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setAccountId(current.getId());
        flow.setAccountType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_MANAGE_CHARGE);
        flow.setAmount(fee);
        flow.setFreezedAmount(current.getFreezedAmount());
        flow.setUseableAmount(current.getUseableAmount());
        flow.setVdate(new Date());
        flow.setNote("借款："+bidRequest.getTitle()+"成功，收取借款手续费："+fee);
        this.systemAccountFlowMapper.insert(flow);

        this.update(current);

    }

    @Override
    public void chargeWithDrawFee(MoneyWithdraw m) {
        //获取当前系统账户
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        current.setUseableAmount(current.getUseableAmount().add(m.getChargeFee()));
        //生成系统账户流水
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setAccountId(current.getId());
        flow.setAccountType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE);
        flow.setAmount(m.getChargeFee());
        flow.setFreezedAmount(current.getFreezedAmount());
        flow.setUseableAmount(current.getUseableAmount());
        flow.setVdate(new Date());
        flow.setNote(m.getApplier().getUsername() + "提现："+ m.getAmount() + "成功，收取提现手续费：" + m.getChargeFee());
        this.systemAccountFlowMapper.insert(flow);

        this.update(current);
    }

    @Override
    public void chargeInterestFee(BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
        //获取当前系统账户
        SystemAccount current = this.systemAccountMapper.selectCurrent();
        //修改系统账户余额
        current.setUseableAmount(current.getUseableAmount().add(interestChargeFee));
        //生成系统账户流水
        SystemAccountFlow flow = new SystemAccountFlow();
        flow.setAccountId(current.getId());
        flow.setAccountType(BidConst.SYSTEM_ACCOUNT_ACTIONTYPE_INTREST_MANAGE_CHARGE);
        flow.setAmount(interestChargeFee);
        flow.setFreezedAmount(current.getFreezedAmount());
        flow.setUseableAmount(current.getUseableAmount());
        flow.setVdate(new Date());
        flow.setNote("项目："+psd.getBidRequestId() + "回款成功，系统账户收取利息手续费：" + interestChargeFee);
        this.systemAccountFlowMapper.insert(flow);

        this.update(current);
    }
}
