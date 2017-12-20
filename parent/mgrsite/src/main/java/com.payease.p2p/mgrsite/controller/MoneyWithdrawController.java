package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.business.qo.MoneyWithdrawQueryObject;
import com.payease.p2p.business.service.IMoneyWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * （后台）提现审核
 * Created by liuxiaoming on 2017/8/15.
 */
@Controller
public class MoneyWithdrawController {

    @Autowired
    private IMoneyWithdrawService moneyWithdrawService;

    @RequestMapping("moneyWithdraw")
    public String moneyWithdraw(@ModelAttribute("qo")MoneyWithdrawQueryObject qo, Model model){
        model.addAttribute("pageResult",this.moneyWithdrawService.query(qo));
        return "moneywithdraw/list";
    }

    /**
     * 提现审核
     * @return
     */
    @RequestMapping("moneyWithdraw_audit")
    @ResponseBody
    public JSONResult moneyWithdraw_audit(Long id,String remark,int state){
        JSONResult json = new JSONResult();
        try{
            this.moneyWithdrawService.audit(id,remark,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            json.setSuccess(false);
            json.setMsg(re.getMessage());
        }
        return json;

    }
}
