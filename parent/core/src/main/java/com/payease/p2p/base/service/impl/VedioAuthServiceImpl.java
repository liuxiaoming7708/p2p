package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.LoginInfo;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.domain.VedioAuth;
import com.payease.p2p.base.event.VedioAuthSuccessEvent;
import com.payease.p2p.base.mapper.VedioAuthMapper;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.VedioAuthQueryObject;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.service.IVedioAuthService;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaoming on 2017/6/29.
 */
@Service
public class VedioAuthServiceImpl implements IVedioAuthService{

    @Autowired
    private VedioAuthMapper vedioAuthMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public PageResult query(VedioAuthQueryObject qo) {
        int count = this.vedioAuthMapper.queryForCount(qo);
        if(count>0){
            List<VedioAuth> list = this.vedioAuthMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void audit(Long loginInfoValue, String remark, int state) {
        //得到审核的用户
        Userinfo applier = this.userInfoService.get(loginInfoValue);
        //判断该用户没有视频审核通过
        if(null != applier && !applier.getIsVedioAuth()){
            //设置好对象属性
            VedioAuth vedioAuth = new VedioAuth();
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setId(loginInfoValue);

            vedioAuth.setApplier(loginInfo);
            vedioAuth.setApplyTime(new Date());
            vedioAuth.setAuditor(UserContext.getCurrent());
            vedioAuth.setAuditTime(new Date());
            vedioAuth.setRemark(remark);
            vedioAuth.setState(state);
            //如果视频审核通过添加状态码
            if(state == VedioAuth.STATE_AUDIT){
                applier.addState(BitStatesUtils.OP_VEDIO_AUTH);
                this.userInfoService.update(applier);

                VedioAuthSuccessEvent event = new VedioAuthSuccessEvent(this,vedioAuth);
                this.publisher.publishEvent(event);

            }
            this.vedioAuthMapper.insert(vedioAuth);
        }
    }
}
