package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.domain.Iplog;
import com.payease.p2p.base.domain.LoginInfo;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.mapper.IplogMapper;
import com.payease.p2p.base.mapper.LoginInfoMapper;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.ILoginInfoService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.MD5;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登陆服务
 * Created by liuxiaoming on 2017/5/16.
 */
@Service
public class LoginInfoServiceImpl implements ILoginInfoService {

    @Autowired
    private LoginInfoMapper loginInfoMapper;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IplogMapper iplogMapper;

    @Override
    public void register(String username, String password) {
        //判断用户是否存在
        int count = loginInfoMapper.countUserByName(username);
        if(count > 0){
            //如果用户名存在则抛出异常
            throw new RuntimeException("该用户名已存在！");
        }else{
            //若用户不存在则保存用户
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername(username);
            loginInfo.setPassword(MD5.encode(password));
            loginInfo.setState(LoginInfo.STATE_NORMAL);
            loginInfo.setUserType(LoginInfo.USER_NORMAL);
            this.loginInfoMapper.insert(loginInfo);

            //创建对应的账户信息和用户信息
            Account account  = new Account();
            account.setId(loginInfo.getId());
            accountService.add(account);

            Userinfo userinfo = new Userinfo();
            userinfo.setId(loginInfo.getId());
            userInfoService.add(userinfo);
        }

    }

    @Override
    public boolean checkUsername(String username) {
        return this.loginInfoMapper.countUserByName(username) > 0;
    }

    @Override
    public LoginInfo login(String username, String password, String ip, int userType) {
        //创建一个登陆日志对象
        Iplog iplog = new Iplog();
        iplog.setUsername(username);
        iplog.setLoginTime(new Date());
        iplog.setIp(ip);
        iplog.setUserType(userType);
        //查询用户是否存在
        LoginInfo loginInfo = this.loginInfoMapper.login(username, MD5.encode(password),userType);

        if(loginInfo != null){
            //查询不为空 则登陆成功将loginInfo放入session中
            UserContext.putCurrent(loginInfo);
            iplog.setState(Iplog.STATE_SUCCESS);
        }else{
            iplog.setState(Iplog.STATE_FAILED);
        }
        //保存iplog
        this.iplogMapper.insert(iplog);
        //若登陆失败则返回null
        return loginInfo;
    }

    @Override
    public void initAdmin() {
    //查询数据库是否有默认的管理员
         int count = this.loginInfoMapper.countByUserType(LoginInfo.USE_MANAGER);
        //没有则默认创建一个
        if(count<=0){
            LoginInfo admin = new LoginInfo();
            admin.setUsername(BidConst.DEFAULT_ADMIN_NAME);
            admin.setPassword(MD5.encode(BidConst.DEFAULT_ADMIN_PASSWORD));
            admin.setState(LoginInfo.STATE_NORMAL);
            admin.setUserType(LoginInfo.USE_MANAGER);
            this.loginInfoMapper.insert(admin);
        }
    }

    @Override
    public List<Map<String, Object>> autoComplate(String keyword) {
        return this.loginInfoMapper.autoComplate(keyword);
    }

    @Override
    public void logout() {
        UserContext.logOutCurrent();
    }
}
