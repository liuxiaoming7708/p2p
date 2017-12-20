package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.query.VedioAuthQueryObject;
import com.payease.p2p.base.service.ILoginInfoService;
import com.payease.p2p.base.service.IVedioAuthService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 视频认证Controller
 * Created by liuxiaoming on 2017/6/30.
 */
@Controller
public class VedioAuthController {

    @Autowired
    private IVedioAuthService vedioAuthService;

    @Autowired
    private ILoginInfoService loginInfoService;

    /**
     * 视频认证列表分页查询
     * @return
     */
    @RequestMapping("vedioAuth")
    public String vedioAuth(@ModelAttribute("qo")VedioAuthQueryObject qo, Model model){

        model.addAttribute("pageResult",this.vedioAuthService.query(qo));
        return "vedioAuth/list";
    }

    /**
     * 添加视频认证审核
     * @param loginInfoValue
     * @param remark
     * @param state
     * @return
     */
    @RequestMapping("vedioAuth_audit")
    @ResponseBody
    public JSONResult vedioAuth_audit(Long loginInfoValue,String remark,int state){
        JSONResult jsonResult = new JSONResult();
        try {
            this.vedioAuthService.audit(loginInfoValue,remark,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            jsonResult.setSuccess(false);
            jsonResult.setMsg(re.getMessage());

        }
        return jsonResult;
    }

    /**
     * 用户的自动补全功能
     * @param keyword
     * @return
     */
    @RequestMapping("vedioAuth_autocomplate")
    @ResponseBody
    public List<Map<String,Object>> vedioAuth_autocomplate(String keyword){
        return this.loginInfoService.autoComplate(keyword);
    }
}
