package com.payease.p2p.base.query;

import com.payease.p2p.base.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 基本的审核查询对象
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
public class AuditQueryObject extends QueryObject {
	protected int state = -1;
	protected Date beginDate;
	protected Date endDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate == null ? null : DateUtil.endOfDay(endDate);
	}
}
