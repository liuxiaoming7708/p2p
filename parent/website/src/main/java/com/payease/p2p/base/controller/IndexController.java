package com.payease.p2p.base.controller;

import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.business.qo.BidRequestQueryObject;
import com.payease.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 * Created by liuxiaoming on 2017/7/22.
 */
@Controller
public class IndexController {

    @Autowired
    private IBidRequestService bidRequestService;

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        BidRequestQueryObject qo = new BidRequestQueryObject();
        qo.setStates(new int[]{BidConst.BIDREQUEST_STATE_BIDDING,
                BidConst.BIDREQUEST_STATE_PAYING_BACK,
                BidConst.BIDREQUEST_STATE_COMPLETE_PAY_BACK});
        qo.setOrderBy("bidRequestState");
        qo.setOrderType("ASC");
        qo.setPageSize(5);
        model.addAttribute("bidRequests",this.bidRequestService.queryForList(qo));
        return "main";
    }

    /**
     * 投资列表的外面框框
     * @return
     */
    @RequestMapping("invest")
    public String invest(){

        return "invest";
    }

    /**
     * 投资列表的明细
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("invest_list")
    public String invest_list(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        model.addAttribute("pageResult",this.bidRequestService.query(qo));
        return "invest_list";
    }
}
