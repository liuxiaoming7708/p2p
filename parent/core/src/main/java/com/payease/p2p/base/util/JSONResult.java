package com.payease.p2p.base.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一使用ajax请求返回对象
 */
@Setter
@Getter
public class JSONResult {
	private boolean success = true;
	private String msg;

	public JSONResult() {
		super();
	}

	public JSONResult(String msg) {
		super();
		this.msg = msg;
	}

	public JSONResult(Boolean success, String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}


}
