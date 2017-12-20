package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.BidRequest;
import com.payease.p2p.business.qo.BidRequestQueryObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRequestMapper {

    int insert(BidRequest record);

    BidRequest selectByPrimaryKey(Long id);

    int updateByPrimaryKey(BidRequest record);

    int queryForCount(BidRequestQueryObject qo);

    List<BidRequest> query(BidRequestQueryObject qo);
}