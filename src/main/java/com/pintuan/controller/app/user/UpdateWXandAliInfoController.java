package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.UserPayInfo;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 *  更改微信和阿里账号信息
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/updateWXandAliInfo")
@Before(CheckUserKeyInterceptor.class)    
public class UpdateWXandAliInfoController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		UserPayInfo userPayInfo = userService.findUserPayInfo(usr_ide_key);
		boolean flag = false;
		if(userPayInfo==null) {
			userPayInfo = new UserPayInfo();
			userPayInfo.set(Fields.PAY_ID, UuidUtils.getUUID2());
			userPayInfo.set(Fields.USER_ID, usr_ide_key);
			flag = true;
		}
		userPayInfo.set(Fields.WX_ACU, getData(Fields.WX_ACU));
		userPayInfo.set(Fields.WX_PWD, getData(Fields.WX_PWD));
		userPayInfo.set(Fields.WX_NME, getData(Fields.WX_NME));
		userPayInfo.set(Fields.ALI_ACU, getData(Fields.ALI_ACU));
		userPayInfo.set(Fields.ALI_PWD, getData(Fields.ALI_PWD));
		userPayInfo.set(Fields.ALI_NME, getData(Fields.ALI_NME));
		if(flag) {
			userService.addUserPayInfo(userPayInfo);
		}else {
			userService.updateUserPayInfo(userPayInfo);
		}
        returnJson();
	}	
	
}
