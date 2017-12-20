package com.payease.p2p.business.domain;

import com.payease.p2p.base.domain.BaseDomain;
import com.payease.p2p.base.util.BidConst;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 平台账户
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
public class SystemAccount extends BaseDomain {
	private int version;// 版本
	private BigDecimal useableAmount = BidConst.ZERO;// 平台账户剩余金额
	private BigDecimal freezedAmount = BidConst.ZERO;// 平台账户冻结金额
}
