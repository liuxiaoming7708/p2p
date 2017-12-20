package com.payease.p2p.business.qo;

import com.payease.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 借款查询对象
 * Created by liuxiaoming on 2017/7/12.
 */
@Getter
@Setter
public class BidRequestQueryObject extends QueryObject{

    /**
     * 借款状态
     */
    private int bidRequestState = -1;

    /**
     * 多个借款状态
     */
    private int[] states;

    private String orderBy;//按照那个列排
    private String orderType;//按照什么顺序排
}
