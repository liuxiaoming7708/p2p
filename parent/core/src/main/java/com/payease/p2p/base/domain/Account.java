package com.payease.p2p.base.domain;

import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.MD5;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * 用户对应的账户信息
 * 
 * @author Administrator
 * 
 */
@Setter
@Getter
public class Account extends BaseDomain {

	private int version;
	private String tradePassword;
	private BigDecimal useableAmount = BidConst.ZERO;//可用余额
	private BigDecimal freezedAmount = BidConst.ZERO;//冻结金额
	private BigDecimal unReceiveInterest = BidConst.ZERO;//待收利息
	private BigDecimal unReceivePrincipal = BidConst.ZERO;//待收本金
	private BigDecimal unReturnAmount = BidConst.ZERO;//待还总额
	private BigDecimal remainBorrowLimit = BidConst.INIT_BORROW_LIMIT;//剩余信用额度
	private BigDecimal borrowLimit = BidConst.INIT_BORROW_LIMIT;//授信额度

	private String verifyCode;// 做数据校验的

	public String getVerifyCode() {
		return MD5.encode(useableAmount.hashCode() + " "
				+ freezedAmount.hashCode());
	}

	public boolean checkVerifyCode() {
		return MD5.encode(
				useableAmount.hashCode() + " " + freezedAmount.hashCode())
				.equals(verifyCode);
	}

	/**
	 * 计算账户总额
	 * @return
     */
	public BigDecimal getTotalAmount() {
		return useableAmount.add(this.freezedAmount)
				.add(this.unReceivePrincipal);
	}

	@Override
	public String toString() {
		return "Account{" +
				"version=" + version +
				", tradePassword='" + tradePassword + '\'' +
				", useableAmount=" + useableAmount +
				", freezedAmount=" + freezedAmount +
				", unReceiveInterest=" + unReceiveInterest +
				", unReceivePrincipal=" + unReceivePrincipal +
				", unReturnAmount=" + unReturnAmount +
				", remainBorrowLimit=" + remainBorrowLimit +
				", borrowLimit=" + borrowLimit +
				", verifyCode='" + verifyCode + '\'' +
				'}';
	}
}
