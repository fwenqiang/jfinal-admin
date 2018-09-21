package com.pintuan.controller.app.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BankCard;
import com.pintuan.service.UserService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *  查询银行卡列表
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryBankCardList")
@Before(CheckUserKeyInterceptor.class)    
public class QueryBankCardListController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		List<BankCard> bankCardList = userService.findBankCardList(usr_ide_key);
		setResp(Fields.BANK_CARD_LIST,DBModelUtils.toMaps(bankCardList));
        returnJson();
	}	
	
}
