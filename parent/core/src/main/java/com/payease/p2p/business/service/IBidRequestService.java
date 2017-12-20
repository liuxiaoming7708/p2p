package com.payease.p2p.business.service;

import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.domain.BidRequestAuditHistory;
import com.payease.p2p.business.qo.BidRequestQueryObject;
import com.payease.p2p.business.qo.PaymentScheduleQueryObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * 借款服务
 * Created by liuxiaoming on 2017/7/12.
 */
public interface IBidRequestService {

    /**
     * 乐观锁控制
     * @param bidRequest
     */
    void update(BidRequest bidRequest);

    /**
     * 用户是否能够申请借款
     * @param userinfo
     * @return
     */
    boolean canApply(Userinfo userinfo);

    /**
     * 申请借款
     * @param br
     */
    void apply(BidRequest br);

    /**
     * 条件查询标的列表（分页）
     * @param qo
     * @return
     */
    PageResult query(BidRequestQueryObject qo);

    List<BidRequest> queryForList(BidRequestQueryObject qo);
    /**
     * 发标前审核
     * @param id
     * @param remark
     * @param state
     */
    void publishAudit(Long id, String remark, int state);

    /**
     * 根据ID查询 bidRequest对象
     * @param id
     * @return
     */
    BidRequest get(Long id);

    /**
     * 查询此标的审核历史对象
     * @param id
     * @return
     */
    List<BidRequestAuditHistory> listAuditHistoryByBidRequest(Long id);

    /**
     * 执行一次投标
     * @param bidRequestId
     * @param amount
     */
    void bid(Long bidRequestId, BigDecimal amount);

    /**
     * 满标一审审核
     * @param id
     * @param remark
     * @param state
     */
    void fullAudit1(Long id, String remark, int state);

    /**
     * 满标二审审核
     * @param id
     * @param remark
     * @param state
     */
    void fullAudit2(Long id, String remark, int state);

    /**
     * 还款列表
     * @param qo
     * @return
     */
    PageResult queryForPaymentSchedule(PaymentScheduleQueryObject qo);

    /**
     * 执行还款操作
     * @param id
     */
    void returnMoney(Long id);
}
