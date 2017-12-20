package com.payease.p2p.base.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 风控材料
 * Created by liuxiaoming on 2017/7/3.
 */
@Getter
@Setter
public class UserFile extends BaseAuditDomain{

    private String image;
    private int score;
    private SystemDictionaryItem fileType;

    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("applier", this.applier.getUsername());
        json.put("fileType", this.fileType.getTitle());
        json.put("image", image);
        return JSONObject.toJSONString(json);
    }
}
