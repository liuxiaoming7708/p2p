package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.SystemDictionary;
import com.payease.p2p.base.domain.SystemDictionaryItem;
import com.payease.p2p.base.mapper.SystemDictionaryItemMapper;
import com.payease.p2p.base.mapper.SystemDictionaryMapper;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.SystemDictionaryQueryObject;
import com.payease.p2p.base.service.ISystemDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxiaoming on 2017/6/26.
 */
@Service
public class SystemDictionaryServiceImpl implements ISystemDictionaryService {

    @Autowired
    private SystemDictionaryMapper systemDictionaryMapper;
    @Autowired
    private SystemDictionaryItemMapper systemDictionaryItemMapper;

    @Override
    public PageResult queryDics(SystemDictionaryQueryObject qo) {
        int count = this.systemDictionaryMapper.queryForCount(qo);
        if(count > 0){
            List<SystemDictionary> list = this.systemDictionaryMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void saveOrUpdateDic(SystemDictionary sd) {
        if(sd.getId()!=null){
            this.systemDictionaryMapper.updateByPrimaryKey(sd);
        }else{
            this.systemDictionaryMapper.insert(sd);
        }
    }

    @Override
    public PageResult queryItems(SystemDictionaryQueryObject qo) {
        int count = this.systemDictionaryItemMapper.queryForCount(qo);
        if(count > 0){
            List<SystemDictionaryItem> list = this.systemDictionaryItemMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void saveOrUpdateItem(SystemDictionaryItem item) {
        if(item.getId()!=null){
            this.systemDictionaryItemMapper.updateByPrimaryKey(item);
        }else{
            this.systemDictionaryItemMapper.insert(item);
        }
    }

    @Override
    public List<SystemDictionary> listDics() {
        return this.systemDictionaryMapper.selectAll();
    }

    @Override
    public List<SystemDictionaryItem> loadBySn(String sn) {

        return this.systemDictionaryItemMapper.selectByParentSn(sn);
    }
}
