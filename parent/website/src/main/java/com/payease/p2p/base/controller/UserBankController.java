package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.business.domain.UserBankinfo;
import com.payease.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户绑定银行卡相关controller
 * Created by liuxiaoming on 2017/8/10.
 */
@Controller
public class UserBankController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserBankinfoService userBankinfoService;

    /**
     * 导向到绑定银行卡页面
     * @param model
     * @return
     */
    @RequestMapping("bankInfo")
    @RequireLogin
    public String bankInfo(Model model){
        Userinfo current = this.userInfoService.getCurrent();
        if(current.getIsBindBank()){
            //用户已经绑定银行卡
            model.addAttribute("bankInfo",this.userBankinfoService.getBankInfoByUser(current.getId()));
            return "bankInfo_result";
        }else{
            //用户未绑定银行卡
            model.addAttribute("userinfo",current);
            return "bankInfo";
        }

    }

    /**
     * 用户绑定银行卡
     * @param userBankinfo
     * @return
     */
    @RequestMapping("bankInfo_save")
    @RequireLogin
    public String bankInfo_save(UserBankinfo userBankinfo){
        this.userBankinfoService.bind(userBankinfo);
        return "redirect:/bankInfo.do";
    }
}
