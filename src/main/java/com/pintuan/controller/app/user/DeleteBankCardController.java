package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *  删除银行卡
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/deleteBankCard")
@Before(CheckUserKeyInterceptor.class)    
public class DeleteBankCardController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		// String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String car_id = isNotNullAndGet(Fields.CAR_ID,ErrCode.PAMAS_ERROR).toString();
		userService.deleteBankCard(car_id);
        returnJson();
	}	
	
}
