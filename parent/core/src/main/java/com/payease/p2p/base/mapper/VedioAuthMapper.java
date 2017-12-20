package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.VedioAuth;
import com.payease.p2p.base.query.VedioAuthQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VedioAuthMapper {

    int insert(VedioAuth record);

    VedioAuth selectByPrimaryKey(Long id);

    int queryForCount(VedioAuthQueryObject qo);

    List<VedioAuth> query(VedioAuthQueryObject qo);
}