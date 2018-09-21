package com.pintuan.controller.app.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.service.UserService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 通过手机号查询用户列表
 * 
 * @author zjh 2018-5-16
 */
@ControllerBind(controllerKey = "/pintuan/app/queryUserListByPhoneNo")
public class QueryUserListByPhoneNoController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String pho_no = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PAMAS_ERROR).toString();
		List<User> userList = userService.findUserListByPhone(pho_no);  
		setResp(Fields.USR_LIST, DBModelUtils.toMaps(userList));
        returnJson();
	}	
	
}
