package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.SystemDictionary;
import com.payease.p2p.base.domain.SystemDictionaryItem;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.SystemDictionaryQueryObject;

import java.util.List;

/**
 * 数据字典相关服务（含数据字典服务）
 * Created by liuxiaoming on 2017/6/26.
 */
public interface ISystemDictionaryService {
    /**
     * 数据字典分类分页查询
     * @param qo
     * @return
     */
    PageResult queryDics(SystemDictionaryQueryObject qo);

    /**
     * 添加或修改数据字典分类
     * @param sd
     */
    void saveOrUpdateDic(SystemDictionary sd);

    /**
     * 数据字典明细分页查询
     * @param qo
     * @return
     */
    PageResult queryItems(SystemDictionaryQueryObject qo);

    /**
     * 添加或者修改数据字典明细
     * @param item
     */
    void saveOrUpdateItem(SystemDictionaryItem item);

    /**
     * 查询全部数据字典分类
     * @return
     */
    List<SystemDictionary> listDics();

    /**
     * 根据数据字典的编号查询明细
     * @param sn
     * @return
     */
    List<SystemDictionaryItem> loadBySn(String sn);
}
