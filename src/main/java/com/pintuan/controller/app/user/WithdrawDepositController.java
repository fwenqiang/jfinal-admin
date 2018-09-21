package com.pintuan.controller.app.user;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Deposit;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 *  用户提现
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/withdrawDeposit")
@Before(CheckUserKeyInterceptor.class)    
public class WithdrawDepositController extends BaseProjectController {
	private UserService userService = new UserService(); 
	private BonusService bonusService = new BonusService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String amt = isNotNullAndGet(Fields.AMT,ErrCode.PAMAS_ERROR).toString();
		Deposit deposit = userService.findDeposit(usr_ide_key,Constants.DEPOSIT_STATE_1);
		Assert.isEmpty(deposit, ErrCode.DEPOSIT_DEALING);
		BonusCfg bonusCfg = bonusService.findBonusCfg(usr_ide_key, Constants.BONUS_CFG_TYP_1);
		Assert.notEmpty(bonusCfg, ErrCode.CANNOT_WITHDROW_DEPOSIT);
		BigDecimal fee = bonusCfg.getBigDecimal(Fields.TOL_AMT);
		Assert.isTrue(fee.compareTo(new BigDecimal(amt))!=-1, ErrCode.DEPOSIT_AMT_ERROR);
		deposit = new Deposit();
		deposit.set(Fields.DEP_ID, UuidUtils.getUUID2());
		deposit.set(Fields.USER_ID, usr_ide_key);
		deposit.set(Fields.CREATE_TIME, new Date());
		deposit.set(Fields.AMT, new BigDecimal(amt));
		deposit.set(Fields.TAR_AMT, getTarAmt(new BigDecimal(amt)));
		deposit.set(Fields.STATE, Constants.DEPOSIT_STATE_1);
		deposit.set(Fields.PASSWORD, getData(Fields.PASSWORD));
		deposit.set(Fields.USER_NAME, getData(Fields.USER_NAME));
		deposit.set(Fields.ACUNT, getData(Fields.ACUNT));
		deposit.set(Fields.DEP_TYP, getData(Fields.DEP_TYP));
		deposit.set(Fields.BAK_TYP, getData(Fields.BAK_TYP));
		deposit.set(Fields.BAK_NME, getData(Fields.BAK_NME));
		userService.addDeposit(deposit);
        returnJson();
	}	
	
	private BigDecimal getTarAmt(BigDecimal amt) {
		if(amt.compareTo(BigDecimal.ZERO)<=0) {
			return BigDecimal.ZERO;
		}
		BigDecimal fee_5 = new BigDecimal("0.05");
		BigDecimal fee_3 = new BigDecimal("0.03");
		if(amt.compareTo(new BigDecimal("3500"))<=0) {
			BigDecimal fee = fee_5.multiply(amt);
			return fee;
		}else {
			BigDecimal fee = fee_5.multiply(amt);
			BigDecimal fee1 = fee_3.multiply(amt.subtract(new BigDecimal("3500")));
			return fee.add(fee1);
		}
	}
	
	/*public static void main(String[] args) {
		WithdrawDepositController w = new WithdrawDepositController();
		System.out.println(w.getTarAmt(new BigDecimal("0")));
		System.out.println(w.getTarAmt(new BigDecimal("1000")));
		System.out.println(w.getTarAmt(new BigDecimal("13600")));
	}*/
	
}
