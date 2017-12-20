package com.payease.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户风控材料查询对象
 * Created by liuxiaoming on 2017/7/10.
 */
@Getter
@Setter
public class UserFileQueryObject extends AuditQueryObject {

    private Long applierId;

}
