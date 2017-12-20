package com.payease.p2p.base.query;

import lombok.Getter;
import lombok.Setter;

/**
 * 高级查询的基类
 */
@Getter
@Setter
abstract public class QueryObject {
	private Integer currentPage = 1;
	private Integer pageSize = 5;

	public int getStart() {
		return (currentPage - 1) * pageSize;
	}
}
