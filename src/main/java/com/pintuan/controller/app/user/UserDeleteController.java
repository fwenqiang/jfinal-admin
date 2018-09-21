package com.pintuan.controller.app.user;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 用户删除，测试用
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/userDelete")
public class UserDeleteController extends BaseProjectController {
	private UserService userService = new UserService();
	public void index() throws CoreException {
		String phoneNo = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PHONE_NO_IS_NULL).toString();
        userService.delete(phoneNo);        
        returnJson();
	}	
}
