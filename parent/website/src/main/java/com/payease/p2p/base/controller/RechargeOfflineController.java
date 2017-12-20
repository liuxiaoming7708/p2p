package com.payease.p2p.base.controller;

import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.RechargeOffline;
import com.payease.p2p.business.qo.RechargeOfflineQueryObject;
import com.payease.p2p.business.service.IPlatformBankInfoService;
import com.payease.p2p.business.service.IRechargeOfflineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * (前台)线下充值 controller
 * Created by liuxiaoming on 2017/7/27.
 */
@Controller
public class RechargeOfflineController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    @Autowired
    private IRechargeOfflineService rechargeOfflineService;

    /**
     * 导向到线下充值页面
     * @param model
     * @return
     */
    @RequestMapping("recharge")
    public String recharge(Model model){
        model.addAttribute("banks",this.platformBankInfoService.listAll());
        return "recharge";
    }

    /**
     * 线下充值单申请
     * @param ro
     * @return
     */
    @RequestMapping("recharge_save")
    @ResponseBody
    public JSONResult recharge_save(RechargeOffline ro){

        JSONResult jsonResult = new JSONResult();
        try {
            this.rechargeOfflineService.apply(ro);
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    @RequireLogin
    @RequestMapping("recharge_list")
    public String recharge_list(@ModelAttribute("qo")RechargeOfflineQueryObject qo,Model model){
        qo.setApplierId(UserContext.getCurrent().getId());
        model.addAttribute("pageResult",this.rechargeOfflineService.query(qo));
        return "recharge_list";
    }

}
