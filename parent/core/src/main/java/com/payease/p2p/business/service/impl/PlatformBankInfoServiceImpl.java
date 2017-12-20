package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.business.domain.PlatformBankInfo;
import com.payease.p2p.business.mapper.PlatformBankInfoMapper;
import com.payease.p2p.business.qo.PlatformBankInfoQueryObject;
import com.payease.p2p.business.service.IPlatformBankInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxiaoming on 2017/7/26.
 */
@Service
public class PlatformBankInfoServiceImpl implements IPlatformBankInfoService{
    @Autowired
    private PlatformBankInfoMapper platformBankInfoMapper;
    @Override
    public PageResult query(PlatformBankInfoQueryObject qo) {
        int count = this.platformBankInfoMapper.queryForCount(qo);
        if(count>0){
            List<PlatformBankInfo> list = this.platformBankInfoMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());

        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void saveOrUpdate(PlatformBankInfo platformBankInfo) {
        if(platformBankInfo.getId()!=null){
            this.platformBankInfoMapper.updateByPrimaryKey(platformBankInfo);
        }else{
            this.platformBankInfoMapper.insert(platformBankInfo);
        }
    }

    @Override
    public List<PlatformBankInfo> listAll() {
        return this.platformBankInfoMapper.selectAll();
    }

}
