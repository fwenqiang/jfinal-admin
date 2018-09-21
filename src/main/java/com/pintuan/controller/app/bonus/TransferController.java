package com.pintuan.controller.app.bonus;

import java.math.BigDecimal;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 好友相互转款
 * 
 */
@ControllerBind(controllerKey = "/pintuan/app/transfer")
@Before(CheckUserKeyInterceptor.class)
public class TransferController extends BaseProjectController {
	private BonusService bonusService = new BonusService();
	private UserService userService = new UserService();


	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY, ErrCode.PAMAS_ERROR).toString();
		String fee = isNotNullAndGet(Fields.FEE, ErrCode.PAMAS_ERROR).toString();
		String thd_id = isNotNullAndGet(Fields.THD_ID, ErrCode.PAMAS_ERROR).toString();
		
		User fuser = (User) getAttribute(Fields.ATTR_USER_ENTITY);
		String usr_id = fuser.getStr(Fields.USER_ID);
		
		//String usr_id = isNotNullAndGet(Fields.USER_ID, ErrCode.PAMAS_ERROR).toString();
		
		User user = userService.findByThdId(thd_id);
        if(null==user) {
        	throw new CoreException(ErrCode.USER_UNEXIST);
        }
        
        String to_usr_id = user.get(Fields.USER_ID);
		BonusCfg bonusCfg = bonusService.findBonusCfg(usr_id, Constants.BONUS_CFG_TYP_1);
		if (bonusCfg == null) {
			bonusCfg = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_1);
		}
		
		BigDecimal amt = new BigDecimal(fee);
		if(amt.compareTo(bonusCfg.getBigDecimal(Fields.TOL_AMT))>0) {
			throw new CoreException(ErrCode.TRANSFER_AMT_ERR);
		}
		
		BonusCfg toBonusCfg = bonusService.findBonusCfg(to_usr_id, Constants.BONUS_CFG_TYP_1);
		if (toBonusCfg == null) {
			toBonusCfg = bonusService.addMyPaket(to_usr_id, Constants.BONUS_CFG_TYP_1);
		}
		
		
		bonusCfg.set(Fields.TOL_AMT, bonusCfg.getBigDecimal(Fields.TOL_AMT).subtract(amt));
		bonusCfg.update();
		userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "07", "好友互转", amt, "2", "好友互转",to_usr_id);

		toBonusCfg.set(Fields.TOL_AMT, toBonusCfg.getBigDecimal(Fields.TOL_AMT).add(amt));
		toBonusCfg.update();
		userService.addAmtRecord(to_usr_id, toBonusCfg.getStr(Fields.BON_CFG_ID), "07", "好友互转", amt, "1", "好友互转",usr_id);
		
		returnJson();
	}
	
	
}
