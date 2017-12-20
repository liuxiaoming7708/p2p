package com.payease.p2p.business.service;

import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.domain.MoneyWithdraw;
import com.payease.p2p.business.domain.PaymentScheduleDetail;
import com.payease.p2p.business.domain.SystemAccount;

import java.math.BigDecimal;

/**
 * 系统账户服务
 * Created by liuxiaoming on 2017/8/4.
 */
public interface ISystemAccountService {

    void update(SystemAccount account);

    /**
     * 检查并初始化系统账户
     */
    void initAccount();

    /**
     * 系统账户收取管理费
     * @param fee
     * @param bidRequest
     */
    void chargeBorrowFee(BigDecimal fee, BidRequest bidRequest);

    /**
     * 系统账户收取提现手续费
     * @param m
     */
    void chargeWithDrawFee(MoneyWithdraw m);

    /**
     * 系统账户收取利息管理费
     * @param interestChargeFee
     * @param psd
     */
    void chargeInterestFee(BigDecimal interestChargeFee, PaymentScheduleDetail psd);
}
