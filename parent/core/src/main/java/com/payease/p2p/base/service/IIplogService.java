package com.payease.p2p.base.service;

import com.payease.p2p.base.query.IplogQueryObject;
import com.payease.p2p.base.query.PageResult;

/**
 * 登陆日志服务
 * Created by liuxiaoming on 2017/6/15.
 */
public interface IIplogService {
    /**
     * 高级查询 + 分页
     * @param qo
     * @return
     */
    PageResult query(IplogQueryObject qo);
}
