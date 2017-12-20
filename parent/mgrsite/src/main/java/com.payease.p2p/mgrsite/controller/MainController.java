package com.payease.p2p.mgrsite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liuxiaoming on 2017/6/16.
 */
@Controller
public class MainController {
    /**
     * 后台首页
     *
     * @return
     */
    @RequestMapping("index")
    public String index(Model model) {
        return "main";
    }
}
