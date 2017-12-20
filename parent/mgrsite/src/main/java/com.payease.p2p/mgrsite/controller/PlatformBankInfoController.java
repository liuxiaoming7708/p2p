package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.business.domain.PlatformBankInfo;
import com.payease.p2p.business.qo.PlatformBankInfoQueryObject;
import com.payease.p2p.business.service.IPlatformBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系统平台账户管理
 * Created by liuxiaoming on 2017/7/26.
 */
@Controller
public class PlatformBankInfoController {

    @Autowired
    private IPlatformBankInfoService platformBankInfoService;

    @RequestMapping("companyBank_list")
    public String companyBank_list(@ModelAttribute("qo")PlatformBankInfoQueryObject qo, Model model){
        model.addAttribute("pageResult", this.platformBankInfoService.query(qo));
        return "platformbankinfo/list";
    }

    /**
     * 添加／编辑
     * @param platformBankInfo
     * @return
     */
    @RequestMapping("companyBank_update")
    @ResponseBody
    public JSONResult companyBank_update(PlatformBankInfo platformBankInfo){
        this.platformBankInfoService.saveOrUpdate(platformBankInfo);
        return new JSONResult();
    }
}
