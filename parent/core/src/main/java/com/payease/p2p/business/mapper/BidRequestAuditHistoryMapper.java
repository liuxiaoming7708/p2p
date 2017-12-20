package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.BidRequestAuditHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRequestAuditHistoryMapper {

    int insert(BidRequestAuditHistory record);

    BidRequestAuditHistory selectByPrimaryKey(Long id);


    /**
     * 查询一个借款所有相关审核对象
     * @param id
     * @return
     */
    List<BidRequestAuditHistory> listAuditHistoryByBidRequest(Long id);
}