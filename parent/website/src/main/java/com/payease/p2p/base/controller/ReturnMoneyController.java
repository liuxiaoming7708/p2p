package com.payease.p2p.base.controller;

import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.qo.PaymentScheduleQueryObject;
import com.payease.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * (前台)还款相关Controller
 * Created by liuxiaoming on 2017/8/17.
 */
@Controller
public class ReturnMoneyController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IAccountService accountService;

    @RequestMapping("borrowBidReturn_list")
    @RequireLogin
    public String borrowBidReturn_list(@ModelAttribute("qo")PaymentScheduleQueryObject qo,Model model){
        qo.setLogininfoId(UserContext.getCurrent().getId());

        model.addAttribute("pageResult",this.bidRequestService.queryForPaymentSchedule(qo));
        model.addAttribute("account",this.accountService.getCurrent());
        return "returnmoney_list";
    }

    /**
     * 执行还款操作
     * @param id
     * @return
     */
    @RequestMapping("returnMoney")
    @ResponseBody
    public JSONResult returnMoney(Long id){
        JSONResult result = new JSONResult();
        try {
            this.bidRequestService.returnMoney(id);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }

}
