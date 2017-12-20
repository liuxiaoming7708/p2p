package com.payease.p2p.base.service;

import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.VedioAuthQueryObject;

/**
 * 视频认证服务
 * Created by liuxiaoming on 2017/6/29.
 */
public interface IVedioAuthService {
    /**
     * 视频认证分页查询
     * @param qo
     * @return
     */
    PageResult query(VedioAuthQueryObject qo);

    /**
     * 后台 视频认证审核
     * @param loginInfoValue
     * @param remark
     * @param state
     */
    void audit(Long loginInfoValue, String remark, int state);
}
