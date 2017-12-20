package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.RechargeOffline;
import com.payease.p2p.business.mapper.RechargeOfflineMapper;
import com.payease.p2p.business.qo.RechargeOfflineQueryObject;
import com.payease.p2p.business.service.IAccountFlowService;
import com.payease.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaoming on 2017/7/27.
 */
@Service
public class RechargeOfflineServiceImpl implements IRechargeOfflineService{
    @Autowired
    private RechargeOfflineMapper rechargeOfflineMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IAccountFlowService accountFlowService;

    @Override
    public void apply(RechargeOffline ro) {
        ro.setApplier(UserContext.getCurrent());
        ro.setApplyTime(new Date());
        ro.setState(RechargeOffline.STATE_NORMAL);
        this.rechargeOfflineMapper.insert(ro);


    }

    @Override
    public PageResult query(RechargeOfflineQueryObject qo) {
        int count = this.rechargeOfflineMapper.queryForCount(qo);
        if(count>0){
            List<RechargeOffline> list = this.rechargeOfflineMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //查询出线下充值单判断状态
        RechargeOffline ro = this.rechargeOfflineMapper.selectByPrimaryKey(id);
        if(ro!=null && ro.getState()==RechargeOffline.STATE_NORMAL){
            //设置相关属性
            ro.setAuditor(UserContext.getCurrent());
            ro.setAuditTime(new Date());
            ro.setRemark(remark);
            ro.setState(state);
            //审核通过
            if(state == RechargeOffline.STATE_AUDIT){
                //得到申请人账户信息，可用余额增加
                Account applierAccount = accountService.get(ro.getApplier().getId());
                applierAccount.setUseableAmount(applierAccount.getUseableAmount().add(ro.getAmount()));
                //添加一条充值流水信息
                this.accountFlowService.rechargeFlow(applierAccount,ro);
                //修改账户信息
                this.accountService.update(applierAccount);
            }

            this.rechargeOfflineMapper.updateByPrimaryKey(ro);
        }
    }
}
