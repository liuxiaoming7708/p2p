package com.payease.p2p.business.mapper;

import com.payease.p2p.business.domain.Bid;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BidMapper {

    int insert(Bid record);

    Bid selectByPrimaryKey(Long id);

    /**
     * 把属于某个借款的所有投标对象的状态修改
     * @param bidRequestId
     * @param state
     */
    void updateBidsState(@Param("bidRequestId")Long bidRequestId, @Param("state")int state);
}