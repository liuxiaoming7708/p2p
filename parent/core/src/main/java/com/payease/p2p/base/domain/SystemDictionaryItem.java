package com.payease.p2p.base.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典明细
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
@ToString
public class SystemDictionaryItem extends BaseDomain {

	private Long parentId;
	private String title;
	private int sequence;
	private String tvalue;
	
	/**
	 * 返回当前的json字符串
	 * @return
	 */
	public String getJsonString(){
		Map<String,Object> json=new HashMap<>();
		json.put("id",id);
		json.put("title", title);
		json.put("sequence", sequence);
		json.put("parentId", parentId);
		return JSONObject.toJSONString(json);
	}
}
