package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.query.UserFileQueryObject;
import com.payease.p2p.base.service.IRealAuthService;
import com.payease.p2p.base.service.IUserFileService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.qo.BidRequestQueryObject;
import com.payease.p2p.business.service.IBidRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * (后台)借款相关Controller
 * Created by liuxiaoming on 2017/7/12.
 */
@Controller
public class BidRequestController {

    @Autowired
    private IBidRequestService bidRequestService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IRealAuthService realAuthService;
    @Autowired
    private IUserFileService userFileService;

    /**
     * 满标一审列表
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("bidrequest_audit1_list")
    public String bidrequest_audit1_list(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1);
        model.addAttribute("pageResult",this.bidRequestService.query(qo));
        return "bidrequest/audit1";
    }
    /**
     * 满标二审列表
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("bidrequest_audit2_list")
    public String bidrequest_audit2_list(@ModelAttribute("qo")BidRequestQueryObject qo,Model model){
        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2);
        model.addAttribute("pageResult",this.bidRequestService.query(qo));
        return "bidrequest/audit2";
    }
    /**
     * 满标一审审核
     * @param id
     * @param remark
     * @param state
     * @return
     */
    @RequestMapping("bidrequest_audit1")
    @ResponseBody
    public JSONResult bidrequest_audit1(Long id,String remark,int state){
        JSONResult result = new JSONResult();
        try {
            this.bidRequestService.fullAudit1(id,remark,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }

    /**
     * 满标二审审核
     * @param id
     * @param remark
     * @param state
     * @return
     */
    @RequestMapping("bidrequest_audit2")
    @ResponseBody
    public JSONResult bidrequest_audit2(Long id,String remark,int state){
        JSONResult result = new JSONResult();
        try {
            this.bidRequestService.fullAudit2(id,remark,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }


    /**
     * 发标前审核列表
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("bidrequest_publishaudit_list")
    public String bidrequest_publishaudit_list(@ModelAttribute("qo")BidRequestQueryObject qo, Model model){

        qo.setBidRequestState(BidConst.BIDREQUEST_STATE_PUBLISH_PENDING);
        model.addAttribute("pageResult",this.bidRequestService.query(qo));
        return "bidrequest/publish_audit";
    }

    /**
     * 发标前审核
     * @param id
     * @param remark
     * @param state
     * @return
     */
    @RequestMapping("bidrequest_publishaudit")
    @ResponseBody
    public JSONResult bidrequest_publishaudit(Long id,String remark,int state){
        JSONResult result = new JSONResult();
        try {
            this.bidRequestService.publishAudit(id,remark,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }

    /**
     * 查看标的详细信息
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
        //查询标的审核历史对象
        model.addAttribute("audits",this.bidRequestService.listAuditHistoryByBidRequest(bidRequest.getId()));
        //查询用户实名认证信息
        model.addAttribute("realAuth",this.realAuthService.get(userinfo.getRealAuthId()));
        //列出借款人相关的所有的风控材料（审核通过的）
        UserFileQueryObject qo = new UserFileQueryObject();
        qo.setState(UserFile.STATE_AUDIT);
        qo.setApplierId(userinfo.getId());
        qo.setPageSize(-1);
        model.addAttribute("userFiles",this.userFileService.queryForList(qo));
        return "bidrequest/borrow_info";
    }

}
