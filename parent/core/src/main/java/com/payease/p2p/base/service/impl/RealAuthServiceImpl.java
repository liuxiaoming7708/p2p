package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.RealAuth;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.event.RealAuthSuccessEvent;
import com.payease.p2p.base.mapper.RealAuthMapper;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.RealAuthQueryObject;
import com.payease.p2p.base.service.*;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaoming on 2017/6/27.
 */
@Service
public class RealAuthServiceImpl implements IRealAuthService {

    @Autowired
    private RealAuthMapper realAuthMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ApplicationContext publisher;

    @Override
    public RealAuth get(Long id) {
        return this.realAuthMapper.selectByPrimaryKey(id);
    }

    @Override
    public void apply(RealAuth realAuth) {
        //判断当前用户能够申请
        Userinfo current = this.userInfoService.getCurrent();
        if(!current.getIsRealAuth() && current.getRealAuthId() ==null){
            //保存一个realAuth
            RealAuth ra = new RealAuth();
            ra.setRealName(realAuth.getRealName());
            ra.setSex(realAuth.getSex());
            ra.setIdNumber(realAuth.getIdNumber());
            ra.setBornDate(realAuth.getBornDate());
            ra.setAddress(realAuth.getAddress());
            ra.setImage1(realAuth.getImage1());
            ra.setImage2(realAuth.getImage2());

            ra.setApplier(UserContext.getCurrent());
            ra.setApplyTime(new Date());
            ra.setState(RealAuth.STATE_NORMAL);
            this.realAuthMapper.insert(ra);

            current.setRealAuthId(ra.getId());
            this.userInfoService.update(current);
        }
    }

    @Override
    public PageResult query(RealAuthQueryObject qo) {
        int count = this.realAuthMapper.queryForCount(qo);
        if(count>0){
            List<RealAuth> list = this.realAuthMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //查询realauth
        RealAuth ra = this.realAuthMapper.selectByPrimaryKey(id);
        if(ra!=null && ra.getState() == RealAuth.STATE_NORMAL){
            //查询申请人，申请人没有实名认证，并且realAuth处于待审核状态
            //设置相关属性
            Userinfo applier = this.userInfoService.get(ra.getApplier().getId());//查询申请人信息
            ra.setAuditor(UserContext.getCurrent());//审核人信息
            ra.setRemark(remark);//标记
            ra.setAuditTime(new Date());//审核日期
            ra.setState(state);//状态
            if(!applier.getIsRealAuth()){//申请人为未认证状态
                if(state == RealAuth.STATE_REJECT){
                    //审核失败
                    //1.把申请人的realAuthId设置为null
                    applier.setRealAuthId(null);
                }else if(state == RealAuth.STATE_AUDIT){
                    //审核成功
                    //1.把申请人的状态码添加
                    applier.addState(BitStatesUtils.OP_REAL_AUTH);
                    //2.设置申请人的实名认证相关信息
                    applier.setRealName(ra.getRealName());
                    applier.setIdNumber(ra.getIdNumber());
                   //创建并发布消息
                    RealAuthSuccessEvent event = new RealAuthSuccessEvent(this,ra);
                    this.publisher.publishEvent(event);
                }
                this.userInfoService.update(applier);//更新申请人信息
            }
            this.realAuthMapper.updateByPrimaryKey(ra);//更新实名认证信息
        }

    }
}
