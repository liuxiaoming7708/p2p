package com.payease.p2p.base.controller;

import com.payease.p2p.base.query.IplogQueryObject;
import com.payease.p2p.base.service.IIplogService;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查看登陆日志
 * Created by liuxiaoming on 2017/6/15.
 */
@Controller
public class IplogController {
    @Autowired
    private IIplogService iplogService;

    @RequestMapping("ipLog")
    public String ipLogList(@ModelAttribute("qo") IplogQueryObject qo, Model model){
        qo.setUsername(UserContext.getCurrent().getUsername());
        model.addAttribute("pageResult",this.iplogService.query(qo));
        return "iplog_list";
    }
}
