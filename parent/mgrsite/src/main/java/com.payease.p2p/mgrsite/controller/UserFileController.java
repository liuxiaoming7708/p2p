package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.query.UserFileQueryObject;
import com.payease.p2p.base.service.IUserFileService;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  （后台）风控材料审核Controller
 * Created by liuxiaoming on 2017/7/10.
 */
@Controller
public class UserFileController {

    @Autowired
    private IUserFileService userFileService;

    /**
     * 风控材料列表查询（分页）
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("userFileAuth")
    public String userFileAuthList(@ModelAttribute("qo") UserFileQueryObject qo, Model model){

        model.addAttribute("pageResult",this.userFileService.query(qo));
        return "userFileAuth/list";
    }

    @RequestMapping("userFile_audit")
    @ResponseBody
    public JSONResult userFile_audit(Long id,String remark,int score,int state){
            JSONResult result = new JSONResult();
        try {
            this.userFileService.audit(id,remark,score,state);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }
}
