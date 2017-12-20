package com.payease.p2p.business.qo;

import lombok.Getter;
import lombok.Setter;

import com.payease.p2p.base.query.AuditQueryObject;

/**
 * 还款对象查询对象
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
public class PaymentScheduleQueryObject extends AuditQueryObject {

	private Long logininfoId;// 查看指定用户的还款

	private Long bidRequestId;// 所属的借款

}
