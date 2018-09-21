package com.pintuan.controller.app.bonus;

import java.math.BigDecimal;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询基金金额
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryFundAmt")
@Before(CheckUserKeyInterceptor.class)    
public class QueryFundAmtController extends BaseProjectController {
	private BonusService bonusService = new BonusService();
	
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String bon_cfg_typ = isNotNullAndGet(Fields.BON_CFG_TYP,ErrCode.PAMAS_ERROR).toString();
		checkType(bon_cfg_typ);
		BonusCfg pakets = bonusService.findBonusCfg(user.getStr(Fields.USER_ID),bon_cfg_typ);
		if(pakets==null) {
			pakets = bonusService.addMyPaket(user.getStr(Fields.USER_ID),bon_cfg_typ);
		}
		BigDecimal tol_amt = bonusService.findBonusCfgSum(bon_cfg_typ);
        setResp("self_amt",pakets.get(Fields.TOL_AMT));
        setResp("tol_amt",tol_amt);
        returnJson();
	}	
	
	private void checkType(String bon_cfg_typ) {
		if(bon_cfg_typ.equals("2")||bon_cfg_typ.equals("3")) {
			return ;
		}
		throw new CoreException(ErrCode.PAMAS_ERROR);
	}
	
	
	
}
