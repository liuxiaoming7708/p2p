package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.query.UserFileQueryObject;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.IRealAuthService;
import com.payease.p2p.base.service.IUserFileService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * (前台)借款相关
 * Created by liuxiaoming on 2017/6/23.
 */
@Controller
public class BorrowController {

    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;

    /**
     * 跳转到借款首页
     */
    @RequestMapping("borrow")
    public String borrowIndex(Model model){
        //判断
        if(UserContext.getCurrent()!=null){
                model.addAttribute("account",this.accountService.getCurrent());
                model.addAttribute("userinfo",this.userInfoService.getCurrent());
                model.addAttribute("creditBorrowScore", BidConst.BASE_BORROW_SCORE);
            return "borrow";
        }else {
            //没有登陆直接导向静态页面
            return "redirect:borrow.html";
        }
    }

    /**
     * 跳转到申请借款页面
     * @param model
     * @return
     */
    @RequestMapping("borrowInfo")
    public String borrowInfo(Model model){
        Userinfo userinfo = this.userInfoService.getCurrent();
        if(this.bidRequestService.canApply(userinfo)){
            if(!userinfo.getHasBidRequestProcess()){
                //如果用户没有借款在审核流程中，添加属性到model，跳转到 borrow_apply 页面
                model.addAttribute("minBidRequestAmount",BidConst.SMALLEST_BIDREQUEST_AMOUNT);
                model.addAttribute("minBidAmount",BidConst.SMALLEST_BID_AMOUNT);
                model.addAttribute("account",this.accountService.getCurrent());
                return "borrow_apply";
            }else{
                //如果有，返回到 borrow_apply_result 页面
                return "borrow_apply_result";
            }

        }else{

            //否则返回到 /borrow.do
            return "redirect:/borrow.do";
        }
    }

    /**
     * 申请借款流程
     * @param bidRequest
     * @return
     */
    @RequestMapping("borrow_apply")
    public String borrow_apply(BidRequest bidRequest){
        this.bidRequestService.apply(bidRequest);
        return "redirect:/borrowInfo.do";
    }

    /**
     * 查看借款详细信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("borrow_info")
    public String borrow_info(Long id,Model model){
        BidRequest bidRequest = this.bidRequestService.get(id);
        Userinfo userinfo = this.userInfoService.get(bidRequest.getCreateUser().getId());

        model.addAttribute("bidRequest",bidRequest);
        model.addAttribute("userInfo",userinfo);

        //查询用户实名认证信息
        model.addAttribute("realAuth",this.realAuthService.get(userinfo.getRealAuthId()));
        //列出借款人相关的所有的风控材料（审核通过的）
        UserFileQueryObject qo = new UserFileQueryObject();
        qo.setState(UserFile.STATE_AUDIT);
        qo.setApplierId(userinfo.getId());
        qo.setPageSize(-1);
        model.addAttribute("userFiles",this.userFileService.queryForList(qo));
        model.addAttribute("self",false);

        if(UserContext.getCurrent()!=null){
            if(UserContext.getCurrent().getId() == bidRequest.getCreateUser().getId()){
                model.addAttribute("self",true);
            }else{
                model.addAttribute("account",this.accountService.getCurrent());
            }
        }
        return "borrow_info";
    }

    /**
     * 执行一次投标
     * @param bidRequestId
     * @param amount
     * @return
     */
    @RequestMapping("borrow_bid")
    @ResponseBody
    public JSONResult borrow_bid(Long bidRequestId, BigDecimal amount){
        this.bidRequestService.bid(bidRequestId,amount);
        return new JSONResult();
    }

}
