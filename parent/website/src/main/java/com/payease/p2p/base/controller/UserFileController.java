package com.payease.p2p.base.controller;

import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.service.ISystemDictionaryService;
import com.payease.p2p.base.service.IUserFileService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.UploadUtil;
import com.payease.p2p.base.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户风控材料controller
 * Created by liuxiaoming on 2017/7/5.
 */
@Controller
public class UserFileController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private IUserFileService userFileService;
    @Autowired
    private ISystemDictionaryService systemDictionaryService;

    /**
     * 点击风控材料认证
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("userFile")
    public String userFile(Model model, HttpServletRequest request){

//        Cookie[] cookie = request.getCookies();
//        System.out.println(UserContext.getCurrent());
        List<UserFile> userFiles = this.userFileService.listUserFilesByHasSelectType(false);
        if(userFiles.size()>0){
            model.addAttribute("userFiles",userFiles);
            model.addAttribute("fileTypes",this.systemDictionaryService.loadBySn("fileTypes"));
            return "userFiles_commit";
        }else{
            model.addAttribute("sessionid", request.getSession().getId());
            model.addAttribute("userFiles",this.userFileService.listUserFilesByHasSelectType(true));
            return "userFiles";
        }


    }
    /**
     * 点击上传风控材料
     * @param file
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("userFile_apply")
    @ResponseBody
    public String userFile_apply(MultipartFile file, HttpSession session, HttpServletRequest request){
        Cookie[] cookie = request.getCookies();
        System.out.println(UserContext.getCurrent());
        Object   aa=session.getAttribute("logininfo");
        String path = this.servletContext.getRealPath("/upload/");
        String fileName = UploadUtil.upload(file,path);
        fileName ="/upload/"+fileName;
        this.userFileService.apply(fileName);
        return fileName;
    }

    /**
     * 给用户资料选择风控材料的分类
     * @param id
     * @param fileType
     * @return
     */
    @RequestMapping("userFile_selectType")
    @ResponseBody
    public JSONResult userFile_selectType(Long[] id,Long[] fileType){
        JSONResult result = new JSONResult();
        try {
            this.userFileService.selectTypes(id,fileType);
        }catch (RuntimeException re){
            re.printStackTrace();
            result.setSuccess(false);
            result.setMsg(re.getMessage());
        }
        return result;
    }
}
