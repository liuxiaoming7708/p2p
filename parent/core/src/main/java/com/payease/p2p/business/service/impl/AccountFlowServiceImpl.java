package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.business.domain.*;
import com.payease.p2p.business.mapper.AccountFlowMapper;
import com.payease.p2p.business.service.IAccountFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liuxiaoming on 2017/7/28.
 */
@Service
public class AccountFlowServiceImpl implements IAccountFlowService {
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    @Override
    public void rechargeFlow(Account account, RechargeOffline ro) {

        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE);
        flow.setAmount(ro.getAmount());
        flow.setNote("线下充值成功，充值金额为："+ro.getAmount());

        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void bidFlow(Account account, Bid bid) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_FREEZED);
        flow.setAmount(bid.getAvailableAmount());
        flow.setNote("投标" + bid.getBidRequestTitle() + "借款，投标冻结金额：" + bid.getAvailableAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void cancalBidFlow(Account account, Bid bid) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_UNFREEZED);
        flow.setAmount(bid.getAvailableAmount());
        flow.setNote("投标" + bid.getBidRequestTitle() + "借款失败，取消投标冻结金额：" + bid.getAvailableAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void borrowSuccessFlow(Account account, BidRequest bidRequest) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL);
        flow.setAmount(bidRequest.getBidRequestAmount());
        flow.setNote("借款" + bidRequest.getTitle() + "借款，收到借款：" + bidRequest.getBidRequestAmount());
        this.accountFlowMapper.insert(flow);

    }

    @Override
    public void borrowChargeFee(BigDecimal borrowChargeFee, Account account, BidRequest bidRequest) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CHARGE);
        flow.setAmount(borrowChargeFee);
        flow.setNote("借款" + bidRequest.getTitle() + "成功，支付借款手续费：" + borrowChargeFee);
        this.accountFlowMapper.insert(flow);

    }

    @Override
    public void bidSuccessFlow(Account account, Bid bid) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL);
        flow.setAmount(bid.getAvailableAmount());
        flow.setNote("借款" + bid.getBidRequestTitle() + "成功，成功投标金额：" + bid.getAvailableAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void withDrawApplyFlow(Account account, MoneyWithdraw m) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED);
        flow.setAmount(m.getAmount());
        flow.setNote("提现申请，冻结金额：" + m.getAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void withDrawSuccessFlow(Account account, MoneyWithdraw m) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW);
        flow.setAmount(m.getAmount().subtract(m.getChargeFee()));
        flow.setNote("提现成功，取消冻结金额：" + flow.getAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void withDrawChargeFeeFlow(Account account, MoneyWithdraw m) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE);
        flow.setAmount(m.getChargeFee());
        flow.setNote("提现成功，支付手续费：" + m.getChargeFee());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void cancalWithDrawFlow(Account account, MoneyWithdraw m) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED);
        flow.setAmount( m.getAmount());
        flow.setNote("提现取消，取消冻结流水：" + m.getAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void returnMoneyFlow(Account account, PaymentSchedule ps) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_RETURN_MONEY);
        flow.setAmount( ps.getTotalAmount());
        flow.setNote("借款：" + ps.getBidRequestTitle()+ "第"+ps.getMonthIndex()+ "期成功还款"+ ps.getTotalAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void receiveMoneyFlow(Account account, PaymentScheduleDetail psd) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_CALLBACK_MONEY);
        flow.setAmount( psd.getTotalAmount());
        flow.setNote("借款:"+psd.getBidRequestId()+"第"+psd.getMonthIndex()+ "期成功还款"+ psd.getTotalAmount());
        this.accountFlowMapper.insert(flow);
    }

    @Override
    public void chargeInterestFee(Account account, BigDecimal interestChargeFee, PaymentScheduleDetail psd) {
        AccountFlow flow = baseFlow(account);
        flow.setAccountActionType(BidConst.ACCOUNT_ACTIONTYPE_INTEREST_SHARE);
        flow.setAmount(interestChargeFee);
        flow.setNote("借款:"+psd.getBidRequestId()+"第"+psd.getMonthIndex()+ "期成功支付利息管理费"+ interestChargeFee);
        this.accountFlowMapper.insert(flow);
    }

    private AccountFlow baseFlow(Account account){
        AccountFlow flow = new AccountFlow();
        flow.setAccountId(account.getId());
        flow.setFreezedAmount(account.getFreezedAmount());
        flow.setUseableAmount(account.getUseableAmount());
        flow.setVdate(new Date());
        return flow;
    }
}
