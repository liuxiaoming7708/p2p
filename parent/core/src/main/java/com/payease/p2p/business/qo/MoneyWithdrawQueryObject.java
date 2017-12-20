package com.payease.p2p.business.qo;

import lombok.Getter;
import lombok.Setter;

import com.payease.p2p.base.query.AuditQueryObject;

/**
 * 提现申请查询对象
 * 
 * @author Administrator
 * 
 */
@Getter
@Setter
public class MoneyWithdrawQueryObject extends AuditQueryObject {

	private Long applierId;
}
