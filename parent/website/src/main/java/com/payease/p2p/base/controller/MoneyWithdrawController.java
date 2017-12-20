package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.business.service.IMoneyWithdrawService;
import com.payease.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 用户提现相关业务（前台）
 * Created by liuxiaoming on 2017/8/10.
 */
@Controller
public class MoneyWithdrawController {

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IUserBankinfoService userBankinfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IMoneyWithdrawService moneyWithdrawService;

    /**
     * 提现申请页面
     * @param model
     * @return
     */
    @RequestMapping("moneyWithdraw")
    @RequireLogin
    public String moneyWithdraw(Model model){
        //得到当前用户
        Userinfo current = this.userInfoService.getCurrent();
        model.addAttribute("haveProcessing",current.getHasWithdrawProcess());
        model.addAttribute("bankInfo",this.userBankinfoService.getBankInfoByUser(current.getId()));
        model.addAttribute("account",this.accountService.getCurrent());
        return "moneyWithdraw_apply";
    }

    /**
     * 提现申请
     */
    @RequestMapping("moneyWithdraw_apply")
    @ResponseBody
    public JSONResult moneyWithdraw_apply(BigDecimal amount){
        JSONResult json = new JSONResult();
        try{
            if(amount==null){
                json.setSuccess(false);
                json.setMsg("金额不能为null！");
            }else{
                this.moneyWithdrawService.apply(amount);
            }

        }catch (RuntimeException re){
            re.printStackTrace();
            json.setSuccess(false);
            json.setMsg(re.getMessage());
        }
        return json;
    }
}
