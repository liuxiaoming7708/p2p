package com.payease.p2p.business.service;

import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.business.qo.MoneyWithdrawQueryObject;

import java.math.BigDecimal;

/**
 * 提现相关业务
 * Created by liuxiaoming on 2017/8/10.
 */
public interface IMoneyWithdrawService {
    /**
     * 提现申请
     * @param moneyAmount
     */
    void apply(BigDecimal moneyAmount);

    PageResult query(MoneyWithdrawQueryObject qo);

    /**
     * 提现审核
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);
}
