package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.ISystemDictionaryService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuxiaoming on 2017/6/15.
 */
@Controller
public class PersonalController {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    /**
     * 个人中心首页
     * @param model
     * @return
     */
    @RequireLogin
    @RequestMapping("personal")
    public String personal(Model model){
        model.addAttribute("account",this.accountService.getCurrent());
        model.addAttribute("userinfo", this.userInfoService.getCurrent());
        return "personal";
    }

    /**
     * 用户绑定手机号
     * @param phoneNumber
     * @param verifyCode
     * @return
     */
    @RequireLogin
    @RequestMapping("bindPhone")
    @ResponseBody
    public JSONResult bindPhone(String phoneNumber,String verifyCode){
        JSONResult jsonResult = new JSONResult();
        try {
            this.userInfoService.bindPhone(phoneNumber,verifyCode);
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 用户绑定邮箱
     * @param uuid
     * @param model
     * @return
     */
    @RequestMapping("bindEmail")
    public String bindEmail(String uuid,Model model){

        try {
            this.userInfoService.bindEmail(uuid);
            model.addAttribute("success",true);
        }catch (Exception e){
            model.addAttribute("success",false);
            model.addAttribute("msg",e.getMessage());
        }
        return "checkmail_result";
    }

    /**
     * 用户基本资料
     * @param model
     * @return
     */
    @RequireLogin
    @RequestMapping("basicInfo")
    public String basicInfo(Model model){
        model.addAttribute("userinfo",this.userInfoService.getCurrent());
        model.addAttribute("educationBackgrounds",this.systemDictionaryService.loadBySn("educationCondition"));
        model.addAttribute("incomeGrades",this.systemDictionaryService.loadBySn("incomeGrande"));
        model.addAttribute("marriages",this.systemDictionaryService.loadBySn("marriage"));
        model.addAttribute("kidCounts",this.systemDictionaryService.loadBySn("kidCount"));
        model.addAttribute("houseConditions",this.systemDictionaryService.loadBySn("houseCondition"));
        return "userinfo";
    }

    /**
     * 保存用户基本资料
     * @param userinfo
     * @return
     */
    @RequireLogin
    @RequestMapping("basicInfo_save")
    @ResponseBody
    public JSONResult basicInfo_save(Userinfo userinfo){
        this.userInfoService.saveBasicInfo(userinfo);
        return new JSONResult();
    }
}
