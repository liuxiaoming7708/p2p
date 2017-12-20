package com.payease.p2p.mgrsite.controller;

import com.payease.p2p.base.domain.SystemDictionary;
import com.payease.p2p.base.domain.SystemDictionaryItem;
import com.payease.p2p.base.query.SystemDictionaryQueryObject;
import com.payease.p2p.base.service.ISystemDictionaryService;
import com.payease.p2p.base.util.JSONResult;
import com.payease.p2p.base.util.RequireLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 数据字典控制器
 * Created by liuxiaoming on 2017/6/26.
 */
@Controller
public class SystemDictionaryController {
    @Autowired
    private ISystemDictionaryService systemDictionaryService;


    /**
     * 数据字典分类列表
     * @param qo
     * @param model
     * @return
     */
    @RequireLogin
    @RequestMapping("systemDictionary_list")
    public String SystemDictionary_List(
            @ModelAttribute("qo")SystemDictionaryQueryObject qo, Model model){
        model.addAttribute("pageResult",this.systemDictionaryService.queryDics(qo));
        return "systemdic/systemDictionary_list";
    }

    /**
     * 添加／修改数据字典
     * @param sd
     * @return
     */
    @RequestMapping("systemDictionary_update")
    @ResponseBody
    public JSONResult systemDictionary_update(SystemDictionary sd){
            this.systemDictionaryService.saveOrUpdateDic(sd);
            return new JSONResult();
    }

    /**
     * 查询数据字典明细表
     * @param qo
     * @param model
     * @return
     */
    @RequestMapping("systemDictionaryItem_list")
    public String systemDictionaryItem_list(
            @ModelAttribute("qo")SystemDictionaryQueryObject qo,Model model){
        model.addAttribute("pageResult",this.systemDictionaryService.queryItems(qo));
        model.addAttribute("systemDictionaryGroups",this.systemDictionaryService.listDics());
        return "systemdic/systemDictionaryItem_list";
    }

    /**
     * 添加／修改数据字典明细
     * @param item
     * @return
     */
    @RequestMapping("systemDictionaryItem_update")
    @ResponseBody
    public JSONResult systemDictionaryItem_update(SystemDictionaryItem item){
        System.out.println(item.toString());
        this.systemDictionaryService.saveOrUpdateItem(item);
        return new JSONResult();
    }

}
