package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.UserFile;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.UserFileQueryObject;

import java.util.List;

/**
 * 用户风控材料Service
 * Created by liuxiaoming on 2017/7/5.
 */
public interface IUserFileService {

    /**
     * 上传一个风控材料图片，创建一个UserFile对象
     * @param fileName
     */
    void apply(String fileName);

    /**
     * 列出当前用户没有选择类型的风控材料
     * @param  hasTypes :true 有选择风控类型分类的
     * @return
     */
    List<UserFile> listUserFilesByHasSelectType(Boolean hasTypes);

    /**
     * 添加当前用户资料风控类型
     * @param ids
     * @param fileTypes
     */
    void selectTypes(Long[] ids, Long[] fileTypes);

    /**
     * 查询风控材料列表（条件查询+分页）
     * @param qo
     * @return
     */
    PageResult query(UserFileQueryObject qo);

    /**
     * 直接查询结果集
     * @param qo
     * @return
     */
    List<UserFile> queryForList(UserFileQueryObject qo);

    /**
     * 审核操作
     * @param id
     * @param remark
     * @param score
     * @param state
     */
    void audit(Long id, String remark, int score, int state);
}
