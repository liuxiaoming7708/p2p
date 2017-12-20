package com.payease.p2p.base.service;

import com.payease.p2p.base.domain.RealAuth;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.query.RealAuthQueryObject;

/**
 * 实名认证审核相关服务
 * Created by liuxiaoming on 2017/6/27.
 */
public interface IRealAuthService {

    /**
     * 获取当前用户实名认证相关信息
     * @param id
     * @return
     */
    RealAuth get(Long id);

    /**
     * 提交实名认证审核
     * @param realAuth
     */
    void apply(RealAuth realAuth);

    /**
     * 分页查寻实名认证表
     * @param qo
     * @return
     */
    PageResult query(RealAuthQueryObject qo);

    /**
     * 后台审核实名认证
     * @param id
     * @param remark
     * @param state
     */
    void audit(Long id, String remark, int state);
}
