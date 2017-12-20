package com.payease.p2p.business.service;

import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.business.domain.PlatformBankInfo;
import com.payease.p2p.business.qo.PlatformBankInfoQueryObject;

import java.util.List;

/**
 * 系统平台账户管理
 * Created by liuxiaoming on 2017/7/26.
 */
public interface IPlatformBankInfoService {
    PageResult query(PlatformBankInfoQueryObject qo);

    /**
     * 添加／编辑
     * @param platformBankInfo
     */
    void saveOrUpdate(PlatformBankInfo platformBankInfo);

    List<PlatformBankInfo> listAll();
}
