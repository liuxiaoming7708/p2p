package com.payease.p2p.business.service;

import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.business.domain.RechargeOffline;
import com.payease.p2p.business.qo.RechargeOfflineQueryObject;

/**
 * 线下充值Service
 * Created by liuxiaoming on 2017/7/27.
 */
public interface IRechargeOfflineService {
    /**
     * 线下充值单申请
     * @param ro
     */
    void apply(RechargeOffline ro);

    /**
     * 分页查询线下充值记录
     * @param qo
     * @return
     */
    PageResult query(RechargeOfflineQueryObject qo);

    /**
     * 线下充值审核
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);
}
