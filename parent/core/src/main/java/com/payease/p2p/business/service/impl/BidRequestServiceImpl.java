package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.*;
import com.payease.p2p.business.mapper.*;
import com.payease.p2p.business.qo.BidRequestQueryObject;
import com.payease.p2p.business.qo.PaymentScheduleQueryObject;
import com.payease.p2p.business.service.IAccountFlowService;
import com.payease.p2p.business.service.IBidRequestService;
import com.payease.p2p.business.service.ISystemAccountService;
import com.payease.p2p.business.util.CalculatetUtil;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by liuxiaoming on 2017/7/12.
 */
@Service
public class BidRequestServiceImpl implements IBidRequestService {
    
    @Autowired
    private BidRequestMapper bidRequestMapper;
    @Autowired
    private BidMapper bidMapper;
    @Autowired
    private BidRequestAuditHistoryMapper bidRequestAuditHistoryMapper;
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;
    @Autowired
    private PaymentScheduleDetailMapper paymentScheduleDetailMapper;

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;
    
    @Override
    public void update(BidRequest bidRequest) {
        int ret = this.bidRequestMapper.updateByPrimaryKey(bidRequest);
        if(ret<=0){
            throw new RuntimeException("乐观锁失败：BidRequest："+bidRequest.getId());
        }
    }

    @Override
    public boolean canApply(Userinfo userinfo) {
        //判断用户 实名认证、视频认证、填写基本资料、风控材料分数
        return userinfo.getIsRealAuth()
                && userinfo.getIsVedioAuth()
                && userinfo.getIsBasicInfo()
                && userinfo.getAuthScore() >= BidConst.BASE_BORROW_SCORE;
    }
    @Override
    public void apply(BidRequest br) {
        //得到当前用户；
        Userinfo current = this.userInfoService.getCurrent();
        Account account = this.accountService.getCurrent();
        //判断：
        if(this.canApply(current) // 1.可以申请借款
            && !current.getHasBidRequestProcess() // 2.没有借款在审核流程当中
            && br.getBidRequestAmount().compareTo(BidConst.SMALLEST_BIDREQUEST_AMOUNT)>=0
            && br.getBidRequestAmount().compareTo(account.getRemainBorrowLimit())<=0 // 3.系统最小借款金额 <= 借款金额 <= 剩余信用额度
            && br.getCurrentRate().compareTo(BidConst.SMALLEST_CURRENT_RATE)>=0
            && br.getCurrentRate().compareTo(BidConst.MAX_CURRENT_RATE)<=0 // 4.系统最小年化利率 <= 年化利率 <= 最高年化利率
            && br.getMinBidAmount().compareTo(BidConst.SMALLEST_BID_AMOUNT)>=0 // 5.系统最小投标 <= 最小投标
                ){ //判断成功
                   //1.创建一个 BidRequest 设置属性 保存
            BidRequest bidRequest = new BidRequest();
            bidRequest.setBidRequestAmount(br.getBidRequestAmount());
            bidRequest.setCurrentRate(br.getCurrentRate());
            bidRequest.setDescription(br.getDescription());
            bidRequest.setDisableDays(br.getDisableDays());
            bidRequest.setMinBidAmount(br.getMinBidAmount());
            bidRequest.setReturnType(br.getReturnType());
            bidRequest.setMonthes2Return(br.getMonthes2Return());
            bidRequest.setTitle(br.getTitle());

            bidRequest.setBidRequestType(BidConst.BIDREQUEST_TYPE_NORMAL);//借款类型
            bidRequest.setApplyTime(new Date());//申请时间
            bidRequest.setCreateUser(UserContext.getCurrent());//申请人
            bidRequest.setTotalRewardAmount(CalculatetUtil.calTotalInterest(
                    bidRequest.getReturnType(),
                    bidRequest.getBidRequestAmount(),
                    bidRequest.getCurrentRate(),
                    bidRequest.getMonthes2Return()));//计算总利息
            bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);//标的状态
            this.bidRequestMapper.insert(bidRequest);

                   //2.给用户添加一个状态码
            current.addState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            this.userInfoService.update(current);

        }






    }

    @Override
    public PageResult query(BidRequestQueryObject qo) {
        int count = this.bidRequestMapper.queryForCount(qo);
        if(count>0){
            List<BidRequest> list = this.bidRequestMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public List<BidRequest> queryForList(BidRequestQueryObject qo) {
        return this.bidRequestMapper.query(qo);
    }

    @Override
    public void publishAudit(Long id, String remark, int state) {
        //得到借款对象，判断处于发标前审核
        BidRequest br = this.bidRequestMapper.selectByPrimaryKey(id);
        if(null != br && br.getBidRequestState() == BidConst.BIDREQUEST_STATE_PUBLISH_PENDING){
            //创建一个审核历史对象、设置相关参数
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            history.setApplier(br.getCreateUser());
            history.setApplyTime(br.getApplyTime());
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            history.setAuditType(BidRequestAuditHistory.PUBLISH_AUDIT);
            history.setRemark(remark);
            history.setState(state);
            history.setBidRequestId(br.getId());
            this.bidRequestAuditHistoryMapper.insert(history);
            if(state == BidRequestAuditHistory.STATE_AUDIT){
                //如果审核通过
                //1.修改借款状态
                br.setBidRequestState(BidConst.BIDREQUEST_STATE_BIDDING);
                //2.修改借款信息
                br.setPublishTime(new Date());
                br.setDisableDate(DateUtils.addDays(br.getPublishTime(),br.getDisableDays()));

            }else {
                //如果审核失败
                //1.修改借款状态
                br.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE);
                //2.移除状态码
                Userinfo borrowUser = this.userInfoService.get(br.getCreateUser().getId());
                borrowUser.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                this.userInfoService.update(borrowUser);

            }
            br.setNote(remark);//风控意见
            this.update(br);
        }



    }

    @Override
    public BidRequest get(Long id) {
        return this.bidRequestMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BidRequestAuditHistory> listAuditHistoryByBidRequest(Long id) {
        return this.bidRequestAuditHistoryMapper.listAuditHistoryByBidRequest(id);
    }

    @Override
    public void bid(Long bidRequestId, BigDecimal amount) {
        //得到当前借款对象
        BidRequest bidRequest = this.get(bidRequestId);
        //得到当前账户
        Account currentAccount = this.accountService.getCurrent();
        //判断本次投标是否有效
        if(bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_BIDDING //  1.借款处于招标状态
                &&!currentAccount.getId().equals(bidRequest.getCreateUser().getId()) //  2.当前用户不是借款人
                &&amount.compareTo(bidRequest.getMinBidAmount())>=0 //  3.投标金额 >= 最小投标金额
                &&amount.compareTo(bidRequest.getRemainAmount())<=0 //  4.投标金额 <= 标的剩余金额
                &&amount.compareTo(currentAccount.getUseableAmount())<=0 //  5.投标金额 <= 账户余额
                ){
                //执行投标
                //  1.创建一个投标对象并设置相关属性
                Bid bid = new Bid();
                bid.setActualRate(bidRequest.getCurrentRate());
                bid.setAvailableAmount(amount);
                bid.setBidRequestId(bidRequestId);
                bid.setBidRequestState(bidRequest.getBidRequestState());
                bid.setBidRequestTitle(bidRequest.getTitle());
                bid.setBidTime(new Date());
                bid.setBidUser(UserContext.getCurrent());
                this.bidMapper.insert(bid);
                //  2.修改借款中的一些属性
                bidRequest.setBidCount(bidRequest.getBidCount()+1);
                bidRequest.setCurrentSum(bidRequest.getCurrentSum().add(amount));
                //  3.减少账户可用余额，增加冻结金额
                currentAccount.setUseableAmount(currentAccount.getUseableAmount().subtract(amount));
                currentAccount.setFreezedAmount(currentAccount.getFreezedAmount().add(amount));
                this.accountService.update(currentAccount);
                //  4.生成一条投标流水
                this.accountFlowService.bidFlow(currentAccount,bid);
                //  5.判断标是否投满，如果投满，标进入一审状态
                if(bidRequest.getBidRequestAmount().equals(bidRequest.getCurrentSum())){
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                    //当借款状态发生变化的时候，要同时修改所有投标的状态
                    this.bidMapper.updateBidsState(bidRequestId,BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
                }
                this.update(bidRequest);

                }






    }

    @Override
    public void fullAudit1(Long id, String remark, int state) {
        //得到账户，判断借款状态
        BidRequest bidRequest = this.get(id);
        if(bidRequest!=null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1){
            //创建一个审核对象并设置相关属性，保存
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            history.setApplier(bidRequest.getCreateUser());
            history.setAuditTime(new Date());
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            history.setAuditType(BidRequestAuditHistory.FULL_AUDIT_1);
            history.setBidRequestId(id);
            history.setRemark(remark);
            history.setState(state);
            //判断审核状态
            if(state == BidRequestAuditHistory.STATE_AUDIT) {
                //审核通过
                // 1. 修改标的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
                // 2. 修改投标状态
                this.bidMapper.updateBidsState(id,BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
            }else {
                //审核失败
                // 1. 修改标的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
                // 2. 修改投标状态
                this.bidMapper.updateBidsState(id,BidConst.BIDREQUEST_STATE_REJECTED);
                // 3. 遍历投标，针对该标的每一个投标人，增加账户余额，减少冻结金额，创建一条取消投标流水
                returnMoney(bidRequest);
                // 4. 对于借款人，移除借款状态码
                Userinfo borrowUser = this.userInfoService.get(bidRequest.getCreateUser().getId());
                borrowUser.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
                this.userInfoService.update(borrowUser);
            }

            this.update(bidRequest);
        }


    }

    @Override
    public void fullAudit2(Long id, String remark, int state) {
        //得到借款，判断对象
        BidRequest bidRequest = this.get(id);
        if(bidRequest != null && bidRequest.getBidRequestState() == BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2) {
            //创建一个历史审核对象，设置值，保存
            BidRequestAuditHistory history = new BidRequestAuditHistory();
            history.setApplier(bidRequest.getCreateUser());
            history.setApplyTime(new Date());
            history.setAuditor(UserContext.getCurrent());
            history.setAuditTime(new Date());
            history.setAuditType(BidRequestAuditHistory.FULL_AUDIT_2);
            history.setBidRequestId(id);
            history.setRemark(remark);
            history.setState(state);

            this.bidRequestAuditHistoryMapper.insert(history);

            if(state == BidRequestAuditHistory.STATE_AUDIT) {
                //审核通过
                // 1. 从借款对象的角度思考
                //**1.1 修改借款的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_PAYING_BACK);
                //**1.2 修改投标的状态
                this.bidMapper.updateBidsState(id,BidConst.BIDREQUEST_STATE_PAYING_BACK);
                // 2. 从借款人角度思考
                //**2.1 借款人账户余额增加，生成一条账户流水
                Account borrowAccount = this.accountService.get(bidRequest.getCreateUser().getId());
                borrowAccount.setUseableAmount(borrowAccount.getUseableAmount().add(bidRequest.getBidRequestAmount()));
                this.accountFlowService.borrowSuccessFlow(borrowAccount,bidRequest);
                //**2.2 去掉借款状态码(见外面)
                //**2.3 剩余信用额度减少
                borrowAccount.setRemainBorrowLimit(borrowAccount.getRemainBorrowLimit().subtract(bidRequest.getBidRequestAmount()));
                //**2.4 增加 unReturnAmount(待还总额)
                borrowAccount.setUnReturnAmount(borrowAccount.getUnReturnAmount()
                        .add(bidRequest.getBidRequestAmount())
                        .add(bidRequest.getTotalRewardAmount()));
                //**2.5 支付借款手续费，生成手续费流水
                BigDecimal borrowChargeFee = CalculatetUtil.calAccountManagementCharge(bidRequest.getBidRequestAmount());
                borrowAccount.setUseableAmount(borrowAccount.getUseableAmount().subtract(borrowChargeFee));
                this.accountFlowService.borrowChargeFee(borrowChargeFee,borrowAccount,bidRequest);
                //**2.6 平台收取手续费（两个对象）
                this.systemAccountService.chargeBorrowFee(borrowChargeFee,bidRequest);
                this.accountService.update(borrowAccount);
                // 3. 从投资人角度思考
                //**3.1 遍历投标
                Map<Long,Account> updates = new HashMap<>();
                for(Bid bid:bidRequest.getBids()) {
                    //**3.2 减少账户的冻结金额，增加投标成功的流水
                    Long bidAccountId = bid.getBidUser().getId();
                    Account bidAccount = updates.get(bidAccountId);
                    if(bidAccount == null){
                        bidAccount = this.accountService.get(bidAccountId);
                        updates.put(bidAccountId,bidAccount);
                    }
                    bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
                    this.accountFlowService.bidSuccessFlow(bidAccount,bid);


                }
                // 4. 考虑满标二审对还款流程有什么交代的
                //**4.1 生成针对这个借款的还款信息和回款信息
                List<PaymentSchedule> pss = this.createPaymentSchedule(bidRequest);
                System.out.println("还款信息：总共"+pss.size()+"条！");
                //遍历还款对象 遍历回款对象
                //**3.3 增加代收利息和代收本金
                for(PaymentSchedule ps:pss){
                    for (PaymentScheduleDetail psd:ps.getPaymentScheduleDetails()){
                        Account bidAccount = updates.get(psd.getToLogininfoId());
                        //代收利息
                        bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().add(psd.getInterest()));
                        //代收本金
                        bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal());
                    }
                }
                for(Account account:updates.values()){
                    this.accountService.update(account);
                }

            }else {
                //审核失败
                // 1. 修改标的状态
                bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_REJECTED);
                // 2. 修改投标状态
                this.bidMapper.updateBidsState(id,BidConst.BIDREQUEST_STATE_REJECTED);
                // 3. 遍历投标，针对该标的每一个投标人，增加账户余额，减少冻结金额，创建一条取消投标流水
                returnMoney(bidRequest);
            }
            // 4. 对于借款人，移除借款状态码
            Userinfo borrowUser = this.userInfoService.get(bidRequest.getCreateUser().getId());
            borrowUser.removeState(BitStatesUtils.OP_HAS_BIDREQUEST_PROCESS);
            this.userInfoService.update(borrowUser);

            this.update(bidRequest);
        }
    }

    @Override
    public PageResult queryForPaymentSchedule(PaymentScheduleQueryObject qo) {
        int count = this.paymentScheduleMapper.queryForCount(qo);
        if(count>0){
            List<PaymentSchedule> list = this.paymentScheduleMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void returnMoney(Long id) {
        //得到还款对象,判断
        PaymentSchedule ps = this.paymentScheduleMapper.selectByPrimaryKey(id);
        // 1. 处于代还款对象 还款人是否是当前用户
        if(ps.getState() == BidConst.PAYMENT_STATE_NORMAL
                && ps.getBorrowUser().getId().equals(UserContext.getCurrent().getId())){
        // 2. 账户余额 >= 还款金额
            Account returnAccount = this.accountService.get(ps.getBorrowUser().getId());
            if(returnAccount.getUseableAmount().compareTo(ps.getTotalAmount()) >=0){
            //执行还款
            // 0. 对于还款对象，修改状态
                ps.setState(BidConst.PAYMENT_STATE_DONE);
                ps.setPayDate(new Date());
                this.paymentScheduleMapper.updateByPrimaryKey(ps);

            // 1. 对于借款人（还款人）
            // **1.1 可用余额减少，生成还款流水
                returnAccount.setUseableAmount(returnAccount.getUseableAmount().subtract(ps.getTotalAmount()));
                this.accountFlowService.returnMoneyFlow(returnAccount,ps);
            // **1.2 待还余额减少
                returnAccount.setUnReturnAmount(returnAccount.getUnReturnAmount().subtract(ps.getTotalAmount()));
            // **1.3 剩余信用额度增加
                returnAccount.setRemainBorrowLimit(returnAccount.getRemainBorrowLimit().add(ps.getPrincipal()));

            // 2. 对于投资人（收款人）
                Map<Long,Account> updates = new HashMap<>();
                for (PaymentScheduleDetail psd:ps.getPaymentScheduleDetails()){
                // **2.1 遍历还款明细对象
                    Long bidAccountId = psd.getToLogininfoId();
                    Account bidAccount = updates.get(bidAccountId);
                    if(bidAccount == null){
                        System.out.println(this.accountService.get(bidAccountId).toString());
                        bidAccount = this.accountService.get(bidAccountId);
                        updates.put(bidAccountId,bidAccount);
                    }
                // **2.2 得到投资人账户 增加账户可用余额 生成成功收款流水
                    bidAccount.setUseableAmount(bidAccount.getUseableAmount().add(psd.getTotalAmount()));
                    this.accountFlowService.receiveMoneyFlow(bidAccount,psd);
                //  **2.3 减少待收本金和待收利息
                    bidAccount.setUnReceivePrincipal(bidAccount.getUnReceivePrincipal().subtract(psd.getPrincipal()));
                    bidAccount.setUnReceiveInterest(bidAccount.getUnReceiveInterest().subtract(psd.getInterest()));
                // **2.4 支付利息管理费 生成支付利息管理费流水
                    BigDecimal interestChargeFee = CalculatetUtil.calInterestManagerCharge(psd.getInterest());
                    bidAccount.setUseableAmount(bidAccount.getUseableAmount().subtract(interestChargeFee));
                    this.accountFlowService.chargeInterestFee(bidAccount,interestChargeFee,psd);
                    // **2.5 系统账户收取利息管理费
                    this.systemAccountService.chargeInterestFee(interestChargeFee,psd);

                    //修改每一期回款对象
                    psd.setPayDate(new Date());
                    this.paymentScheduleDetailMapper.updateByPrimaryKey(psd);
                }
                for(Account account:updates.values()){
                    this.accountService.update(account);
                }
            // 3. 对于借款（还款人） 如果当前还的是最后一期 标的借款变成已完成状态 修改投标信息
                PaymentScheduleQueryObject qo = new PaymentScheduleQueryObject();
                qo.setBidRequestId(ps.getBidRequestId());
                qo.setPageSize(-1);
                List<PaymentSchedule> pss =this.paymentScheduleMapper.query(qo);
                //遍历查询是否还有未还款的还款计划
                boolean unReturn = false;
                for(PaymentSchedule temp : pss){
                    if(temp.getState() == BidConst.PAYMENT_STATE_NORMAL || temp.getState() == BidConst.PAYMENT_STATE_OVERDUE){
                        unReturn = true;
                        break;
                    }
                }
                if(!unReturn){
                    //说明最后一期还款
                    BidRequest bidRequest = this.get(ps.getBidRequestId());
                    bidRequest.setBidRequestState(BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                    this.update(bidRequest);
                    this.bidMapper.updateBidsState(bidRequest.getId(),BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK);
                }

                this.accountService.update(returnAccount);
            }
        }

    }

    /**
     * 创建针对这个借款的还款信息和回款明细
     * @param bidRequest
     */
    private List<PaymentSchedule> createPaymentSchedule(BidRequest bidRequest) {

        List<PaymentSchedule> ret = new ArrayList<>();
        //用于累加本金
        BigDecimal totalPrincipal = BidConst.ZERO;
        //用于累加利息
        BigDecimal totalInterest = BidConst.ZERO;

        for (int i=0;i < bidRequest.getMonthes2Return();i++){
            //每一次都是一期还款对象
            PaymentSchedule ps = new PaymentSchedule();
            ps.setBidRequestId(bidRequest.getId());
            ps.setBidRequestTitle(bidRequest.getTitle());
            ps.setBidRequestType(bidRequest.getBidRequestType());
            ps.setBorrowUser(bidRequest.getCreateUser());
            ps.setDeadLine(DateUtils.addMonths(bidRequest.getPublishTime(),i+1));
            ps.setMonthIndex(i+1);
            ps.setReturnType(bidRequest.getReturnType());
            ps.setState(BidConst.PAYMENT_STATE_NORMAL);
            if(i<bidRequest.getMonthes2Return()-1) {
                ps.setInterest(CalculatetUtil.calMonthlyInterest(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
                        bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                ps.setTotalAmount(CalculatetUtil.calMonthToReturnMoney(bidRequest.getReturnType(), bidRequest.getBidRequestAmount(),
                        bidRequest.getCurrentRate(), i + 1, bidRequest.getMonthes2Return()));
                ps.setPrincipal(ps.getTotalAmount().subtract(ps.getInterest()));
            //累加 本金和利息
                totalPrincipal = totalPrincipal.add(ps.getPrincipal());
                totalInterest = totalInterest.add(ps.getInterest());
            }else{
                //最后一期
                ps.setInterest(bidRequest.getTotalRewardAmount().subtract(totalInterest));
                ps.setPrincipal(bidRequest.getBidRequestAmount().subtract(totalPrincipal));
                ps.setTotalAmount(ps.getInterest().add(ps.getPrincipal()));
            }
            this.paymentScheduleMapper.insert(ps);

            this.createPaymentScheduleDetail(ps,bidRequest);
            ret.add(ps);
        }
        return ret;
    }

    /**
     * 创建针对每一期的还款的回款对象
     * @param ps
     * @param bidRequest
     */
    private void createPaymentScheduleDetail(PaymentSchedule ps, BidRequest bidRequest) {

        //用于累加本金
        BigDecimal totalPrincipal = BidConst.ZERO;
        //用于累加总金额
        BigDecimal totalAmount = BidConst.ZERO;
        for (int i=0;i<bidRequest.getBids().size();i++){
            Bid bid = bidRequest.getBids().get(i);
            //针对每一个投标创建一个回款对象
            PaymentScheduleDetail psd = new PaymentScheduleDetail();
            psd.setBidAmount(bid.getAvailableAmount());
            psd.setBidId(bid.getId());
            psd.setBidRequestId(bidRequest.getId());
            psd.setDeadline(ps.getDeadLine());
            psd.setFromLogininfo(bidRequest.getCreateUser());
            psd.setMonthIndex(ps.getMonthIndex());
            psd.setPaymentScheduleId(ps.getId());
            psd.setReturnType(bidRequest.getReturnType());
            psd.setToLogininfoId(bid.getBidUser().getId());
            if(i<bidRequest.getBids().size()-1) {
                //回款本金 = 投标金额/借款金额*本期还款本金
                psd.setPrincipal(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP)
                        .multiply(ps.getPrincipal()).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                //回款利息 = 投标金额/借款金额*本期还款利息
                psd.setInterest(bid.getAvailableAmount().divide(bidRequest.getBidRequestAmount(), BidConst.CAL_SCALE, RoundingMode.HALF_UP)
                        .multiply(ps.getInterest()).setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP));
                psd.setTotalAmount(psd.getPrincipal().add(psd.getInterest()));
            }else{
                //最后一个回款明细
                psd.setPrincipal(ps.getPrincipal().subtract(totalPrincipal));
                psd.setTotalAmount(ps.getTotalAmount().subtract(totalAmount));
                psd.setInterest(psd.getTotalAmount().subtract(psd.getPrincipal()));
            }
            this.paymentScheduleDetailMapper.insert(psd);
            ps.getPaymentScheduleDetails().add(psd);
        }
    }

    /**
     * 借款失败 退款
     * @param bidRequest
     */
    private void returnMoney(BidRequest bidRequest){
        Map<Long,Account> updates = new HashMap<>();
        for(Bid bid:bidRequest.getBids()){
            Long bidAccountId = bid.getBidUser().getId();
            Account bidAccount = updates.get(bidAccountId);
            if(bidAccount==null){
                bidAccount =  this.accountService.get(bidAccountId);
                updates.put(bidAccountId,bidAccount);
            }
            bidAccount.setUseableAmount(bidAccount.getUseableAmount().add(bid.getAvailableAmount()));
            bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().subtract(bid.getAvailableAmount()));
            this.accountFlowService.cancalBidFlow(bidAccount,bid);
        }
        //统一修改投标人账户
        for (Account bidAccount:updates.values()){
            this.accountService.update(bidAccount);
        }
    }

}
