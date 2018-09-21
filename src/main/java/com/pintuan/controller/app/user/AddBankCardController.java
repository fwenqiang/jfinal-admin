package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BankCard;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 *  添加银行卡
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/addBankCard")
@Before(CheckUserKeyInterceptor.class)    
public class AddBankCardController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		BankCard bankCard = new BankCard();
		bankCard.set(Fields.CAR_ID, UuidUtils.getUUID2());
		bankCard.set(Fields.USER_ID, usr_ide_key);
		bankCard.set(Fields.BAK_TYP, getData(Fields.BAK_TYP));
		bankCard.set(Fields.USER_NAME, getData(Fields.USER_NAME));
		bankCard.set(Fields.CAR_NO, getData(Fields.CAR_NO));
		bankCard.set(Fields.BAK_NME, getData(Fields.BAK_NME));
		userService.addBankCard(bankCard);
		setResp(Fields.CAR_ID, bankCard.getStr(Fields.CAR_ID));
        returnJson();
	}	
	
}
