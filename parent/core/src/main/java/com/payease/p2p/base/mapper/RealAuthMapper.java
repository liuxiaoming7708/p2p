package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.RealAuth;
import com.payease.p2p.base.query.RealAuthQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealAuthMapper {

    int insert(RealAuth record);

    RealAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RealAuth record);

    //分页查询相关
    int queryForCount(RealAuthQueryObject qo);

    List<RealAuth> query(RealAuthQueryObject qo);



}