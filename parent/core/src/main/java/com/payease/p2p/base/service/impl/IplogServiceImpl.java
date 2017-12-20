package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.Iplog;
import com.payease.p2p.base.mapper.IplogMapper;
import com.payease.p2p.base.query.IplogQueryObject;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.service.IIplogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuxiaoming on 2017/6/15.
 */
@Service
public class IplogServiceImpl implements IIplogService{

    @Autowired
    private IplogMapper iplogMapper;
    @Override
    public PageResult query(IplogQueryObject qo) {
        int count = this.iplogMapper.queryForCount(qo);
        if(count>0){
            List<Iplog> list = this.iplogMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
        }
        return PageResult.empty(qo.getPageSize());
    }
}
