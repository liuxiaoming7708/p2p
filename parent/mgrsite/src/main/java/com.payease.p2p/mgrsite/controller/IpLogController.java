package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.query.IplogQueryObject;
import com.payease.p2p.base.service.IIplogService;
import com.payease.p2p.base.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台登陆日志查询
 * Created by liuxiaoming on 2017/6/16.
 */
@Controller
public class IpLogController {
    @Autowired
    private IIplogService iplogService;
    @RequireLogin
    @RequestMapping("ipLog")
    public String ipLogList(@ModelAttribute("qo")IplogQueryObject qo,Model model){

        model.addAttribute("pageResult",this.iplogService.query(qo));
        return "ipLog/list";
    }
}
