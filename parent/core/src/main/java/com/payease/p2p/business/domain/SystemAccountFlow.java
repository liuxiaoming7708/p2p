package com.payease.p2p.business.domain;

import com.payease.p2p.base.domain.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统账户账户流水
 * 
 * @author Administrator
 * 
 */
@Getter
@Setter
public class SystemAccountFlow extends BaseDomain {

	private Long accountId;// 对应的系统账户的id
	private BigDecimal amount;// 流水相关金额
	private int accountType;// 系统账户流水类型
	private Date vdate;// 流水创建时间
	private String note;
	private BigDecimal useableAmount;// 流水产生之后系统账户的可用余额;
	private BigDecimal freezedAmount;// 流水产生之后系统账户的冻结余额;
}
