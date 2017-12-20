package com.payease.p2p.base.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * 视频认证查询对象
 * Created by liuxiaoming on 2017/6/29.
 */
@Getter
@Setter
public class VedioAuthQueryObject extends AuditQueryObject{

    private String keyword;

    public String getKeyword(){
        return StringUtils.hasLength(keyword)?keyword:null;
    }
}
