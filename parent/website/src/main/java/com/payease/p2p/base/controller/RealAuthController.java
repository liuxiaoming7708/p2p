package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.RealAuth;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.service.IRealAuthService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import com.payease.p2p.base.util.UploadUtil;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

/**
 * 前台 实名认证控制器
 * Created by liuxiaoming on 2017/6/27.
 */
@Controller
public class RealAuthController {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IRealAuthService realAuthService;

    @Autowired
    private ServletContext servletContext;

    /**
     * 用户点击实名认证按钮
     * @param model
     * @return
     */
    @RequireLogin
    @RequestMapping("realAuth")
    public String realAuth(Model model){
        //如果用户没有实名认证并且realAuthId为空，导向到realAuth
        Userinfo current = this.userInfoService.getCurrent();
        if(!current.getIsRealAuth() && current.getRealAuthId() == null){
            return "realAuth";
        }else{
            if(!current.getIsRealAuth()){
                model.addAttribute("auditing",true);
            }else{
                model.addAttribute("realAuth",this.realAuthService.get(current.getRealAuthId()));
            }
            return "realAuth_result";
        }

    }

    /**
     * 提交实名认证审核
     * @param realAuth
     * @return
     */
    @RequestMapping("realAuth_save")
    @ResponseBody
    public JSONResult realAuth_save(RealAuth realAuth){
        this.realAuthService .apply(realAuth);
        return new JSONResult();
    }

    /**
     * 上传图片
     * 注：这里千万不要写 @RequireLogin
     * @param file
     * @return
     */

    @RequestMapping("realAuth_upload")
    @ResponseBody
    public String realAuth_upload(MultipartFile file){
        System.out.println(UserContext.getCurrent());
        String dic = "/upload";
        String fileName = UploadUtil.upload(file,this.servletContext.getRealPath(dic));
        System.out.println(this.servletContext.getRealPath(dic));
        System.out.println(dic+"/"+fileName);
        return dic+"/"+fileName;
    }
}
