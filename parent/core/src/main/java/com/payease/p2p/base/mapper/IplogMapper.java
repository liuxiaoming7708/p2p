package com.payease.p2p.base.mapper;

import com.payease.p2p.base.domain.Iplog;
import com.payease.p2p.base.query.IplogQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IplogMapper {

    int insert(Iplog record);

    int queryForCount(IplogQueryObject qo);

    List<Iplog> query(IplogQueryObject qo);

}