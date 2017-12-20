package com.payease.p2p.business.service.impl;

import com.payease.p2p.base.domain.Account;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.query.PageResult;
import com.payease.p2p.base.service.IAccountService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.BitStatesUtils;
import com.payease.p2p.base.util.UserContext;
import com.payease.p2p.business.domain.MoneyWithdraw;
import com.payease.p2p.business.domain.UserBankinfo;
import com.payease.p2p.business.mapper.MoneyWithdrawMapper;
import com.payease.p2p.business.qo.MoneyWithdrawQueryObject;
import com.payease.p2p.business.service.IAccountFlowService;
import com.payease.p2p.business.service.IMoneyWithdrawService;
import com.payease.p2p.business.service.ISystemAccountService;
import com.payease.p2p.business.service.IUserBankinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxiaoming on 2017/8/10.
 */
@Service
public class MoneyWithdrawServiceImpl implements IMoneyWithdrawService{

    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IUserBankinfoService userBankinfoService;
    @Autowired
    private IAccountFlowService accountFlowService;
    @Autowired
    private ISystemAccountService systemAccountService;

    @Autowired
    private MoneyWithdrawMapper moneyWithdrawMapper;

    @Override
    public void apply(BigDecimal moneyAmount) {
        //得到当前用户信心 当前账户信息
        Userinfo current = this.userInfoService.getCurrent();
        Account account = this.accountService.getCurrent();
        //判断用户已经绑定银行卡 没有提现在审核流程中 提现金额不超过当前可用余额 提现金额超过最小提现金额
        if(current.getIsBindBank()
                && !current.getHasBidRequestProcess()
                && moneyAmount.compareTo(account.getUseableAmount())<=0
                && moneyAmount.compareTo(BidConst.MIN_WITHDRAW_AMOUNT)>=0){
            UserBankinfo bi = this.userBankinfoService.getBankInfoByUser(current.getId());
            //提现申请对象创建
            MoneyWithdraw m = new MoneyWithdraw();
            m.setAccountName(bi.getAccountName());
            m.setAccountNumber(bi.getAccountNumber());
            m.setAmount(moneyAmount);
            m.setApplier(UserContext.getCurrent());
            m.setApplyTime(new Date());
            m.setBankName(bi.getBankName());
            m.setChargeFee(BidConst.MONEY_WITHDRAW_CHARGEFEE);
            m.setForkName(bi.getForkName());
            m.setState(MoneyWithdraw.STATE_NORMAL);
            this.moneyWithdrawMapper.insert(m);

            //用户可用余额减少 冻结金额增加 生成提现生成流水
            account.setUseableAmount(account.getUseableAmount().subtract(moneyAmount));
            account.setFreezedAmount(account.getFreezedAmount().add(moneyAmount));
            this.accountFlowService.withDrawApplyFlow(account,m);

            //给用户添加状态码
            current.addState(BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS);
            this.userInfoService.update(current);

            this.accountService.update(account);
        }
    }

    @Override
    public PageResult query(MoneyWithdrawQueryObject qo) {
        int count = this.moneyWithdrawMapper.queryForCount(qo);
        if(count>0){
            List<MoneyWithdraw> list = this.moneyWithdrawMapper.query(qo);
            return new PageResult(list,count,qo.getCurrentPage(),qo.getPageSize());
        }
        return PageResult.empty(qo.getPageSize());
    }

    @Override
    public void audit(Long id, String remark, int state) {
        //通过ID 得到提现申请 判断状态
        MoneyWithdraw m = this.moneyWithdrawMapper.selectByPrimaryKey(id);
        if(m!=null && m.getState()==MoneyWithdraw.STATE_NORMAL){
            //设置相关属性
            m.setAuditor(UserContext.getCurrent());
            m.setAuditTime(new Date());
            m.setRemark(remark);
            m.setState(state);

            Userinfo userinfo = this.userInfoService.get(m.getApplier().getId());
            Account account = this.accountService.get(userinfo.getId());
            if(state == MoneyWithdraw.STATE_AUDIT) {
                //审核通过
                //1. 减少冻结金额 生成一条提现流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount().subtract(m.getChargeFee())));
                this.accountFlowService.withDrawSuccessFlow(account,m);
                //2. 减少冻结金额 生成一条支付提现手续费流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getChargeFee()));

                this.accountFlowService.withDrawChargeFeeFlow(account,m);
                //3. 系统账户提现手续费
                this.systemAccountService.chargeWithDrawFee(m);
            }else {
                //审核失败
                //1. 减少冻结金额 增加可用余额 生成取消提现流水
                account.setFreezedAmount(account.getFreezedAmount().subtract(m.getAmount()));
                account.setUseableAmount(account.getUseableAmount().add(m.getAmount()));
                this.accountFlowService.cancalWithDrawFlow(account,m);
            }
            //4. 取消状态码
            userinfo.removeState(BitStatesUtils.OP_HAS_MONEYWITHDRAW_PROCESS);
            this.userInfoService.update(userinfo);
            this.accountService.update(account);
            this.moneyWithdrawMapper.updateByPrimaryKey(m);
        }
    }


}
