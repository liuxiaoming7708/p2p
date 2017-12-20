package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.query.RealAuthQueryObject;
import com.payease.p2p.base.service.IRealAuthService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  后台 实名认证审核
 * Created by liuxiaoming on 2017/6/28.
 */
@Controller
public class RealAuthController {

    @Autowired
    private IRealAuthService realAuthService;

    /**
     * 分页查询实名认证列表
     * @param model
     * @return
     */
    @RequestMapping("realAuth")
    public String realAuth(@ModelAttribute("qo")RealAuthQueryObject qo, Model model){
        model.addAttribute("pageResult",this.realAuthService.query(qo));
        return "realAuth/list";
    }

    @RequestMapping("realAuth_audit")
    @ResponseBody
    public JSONResult realAuth_audit(Long id,String remark,int state){
        this.realAuthService.audit(id,remark,state);
        return new JSONResult();
    }
}
