package com.payease.p2p.business.qo;

import com.payease.p2p.base.query.QueryObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 线下充值查询
 * 
 * @author Administrator
 * 
 */
@Getter
@Setter
public class RechargeOfflineQueryObject extends QueryObject {

	private Long applierId;
	private long bankInfoId = -1;// 按照开户行查询
	private String tradeCode;
	private Date beginDate;
	private Date endDate;
	private int state = -1;
	public String getTradeCode() {
		return StringUtils.hasLength(tradeCode) ? tradeCode : null;
	}
}
