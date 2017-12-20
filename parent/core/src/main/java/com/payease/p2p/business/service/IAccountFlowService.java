package com.payease.p2p.business.service;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.business.domain.*;

import java.math.BigDecimal;

/**
 * 处理账户流水信息
 *
 * Created by liuxiaoming on 2017/7/28.
 */
public interface IAccountFlowService {
    /**
     * 线下充值生成账户流水
     * @param account
     * @param ro
     */
    void rechargeFlow(Account account, RechargeOffline ro);

    /**
     * 投标流水
     * @param account
     * @param bid
     */
    void bidFlow(Account account, Bid bid);

    /**
     * 投标失败流水
     * @param account
     * @param bid
     */
    void cancalBidFlow(Account account, Bid bid);

    /**
     * 成功借款的流水
     * @param account
     * @param bidRequest
     */
    void borrowSuccessFlow(Account account, BidRequest bidRequest);

    /**
     * 生成借款手续费流水
     * @param borrowChargeFee
     * @param account
     * @param bidRequest
     */
    void borrowChargeFee(BigDecimal borrowChargeFee, Account account, BidRequest bidRequest);

    /**
     * 成功投标流水
     * @param account
     * @param bid
     */
    void bidSuccessFlow(Account account, Bid bid);

    /**
     * 生成提现申请流水
     * @param account
     * @param m
     */
    void withDrawApplyFlow(Account account, MoneyWithdraw m);

    /**
     * 提现成功 生成提现流水
     * @param account
     * @param m
     */
    void withDrawSuccessFlow(Account account, MoneyWithdraw m);

    /**
     * 支付提现手续费流水
     * @param account
     * @param m
     */
    void withDrawChargeFeeFlow(Account account, MoneyWithdraw m);

    /**
     * 取消提现流水
     * @param account
     * @param m
     */
    void cancalWithDrawFlow(Account account, MoneyWithdraw m);

    /**
     * 生成成功还款流水
     * @param account
     * @param ps
     */
    void returnMoneyFlow(Account account, PaymentSchedule ps);

    /**
     * 投资人成功收款流水
     * @param account
     * @param psd
     */
    void receiveMoneyFlow(Account account, PaymentScheduleDetail psd);

    /**
     * 生成支付利息管理费流水
     * @param account
     * @param interestChargeFee
     * @param psd
     */
    void chargeInterestFee(Account account, BigDecimal interestChargeFee, PaymentScheduleDetail psd);
}
