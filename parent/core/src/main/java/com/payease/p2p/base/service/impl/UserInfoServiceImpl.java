package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.MailVerify;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.mapper.MailVerifyMapper;
import com.payease.p2p.base.mapper.UserinfoMapper;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.service.IVerifyCodeService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.DateUtil;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liuxiaoming on 2017/6/15.
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService{

    @Autowired
    private UserinfoMapper userinfoMapper;
    @Autowired
    private IVerifyCodeService verifyCodeService;
    @Autowired
    private MailVerifyMapper mailVerifyMapper;

    @Override
    public void update(Userinfo userinfo) {
        int ret = this.userinfoMapper.updateByPrimaryKey(userinfo);
        if(ret<=0){
            throw new RuntimeException("乐观锁失败：UserInfo："+userinfo.getId());
        }
    }

    @Override
    public void add(Userinfo userinfo) {
        this.userinfoMapper.insert(userinfo);
    }

    @Override
    public Userinfo getCurrent() {
        return this.userinfoMapper.selectByPrimaryKey(UserContext.getCurrent().getId());
    }

    @Override
    public Userinfo get(Long id) {
        return this.userinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public void bindPhone(String phoneNumber, String verifyCode) {
        //1. 做验证码的校验
        if(this.verifyCodeService.validate(phoneNumber,verifyCode)){
            //2. 如果校验成功 绑定手机和状态码
            Userinfo current = this.getCurrent();
            if(!current.getIsBindPhone()) {
                current.addState(BitStatesUtils.OP_BIND_PHONE);
                current.setPhoneNumber(phoneNumber);
                this.update(current);
            }else{
                throw new RuntimeException("绑定失败，该用户已经绑定过手机，无需再次绑定，请使用修改手机号码功能进行号码的更新！");
            }
        }else{
            throw new RuntimeException("验证码校验失败！");
        }

    }

    @Override
    public void bindEmail(String uuid) {
        //根据uuid查询MailVerify对象
        MailVerify mailVerify = this.mailVerifyMapper.selectByUUID(uuid);
        if(mailVerify!=null
                && DateUtil.getSecondsBetween(mailVerify.getSendTime(),new Date())<= BidConst.VERIFYEMAIL_VAILDATE_DAY*24*3600){
            //如果有,在有效期之内
            //得到用户，如果用户没有绑定邮箱
            Userinfo userinfo = this.userinfoMapper.selectByPrimaryKey(mailVerify.getLogininfoId());
            if(!userinfo.getIsBindEmail()){
                //添加邮箱状态码，设置email属性
                userinfo.addState(BitStatesUtils.OP_BIND_EMAIL);
                userinfo.setEmail(mailVerify.getEmail());
                this.update(userinfo);
                return;
            }
        }
        throw new RuntimeException("验证邮箱地址错误！");

    }

    @Override
    public void saveBasicInfo(Userinfo userinfo) {
        //获取当前用户，给当前用户设置相关参数
        Userinfo current = this.getCurrent();
        current.setEducationBackground(userinfo.getEducationBackground());
        current.setHouseCondition(userinfo.getHouseCondition());
        current.setIncomeGrade(userinfo.getIncomeGrade());
        current.setKidCount(userinfo.getKidCount());
        current.setMarriage(userinfo.getMarriage());
        //如果当前用户没有填写基本资料的状态，绑定一个
        if(!current.getIsBasicInfo()){
            current.addState(BitStatesUtils.OP_BASIC_INFO);
        }
        this.update(current);
    }
}
