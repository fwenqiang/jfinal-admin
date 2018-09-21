package com.pintuan.controller.app.deposit;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;

/**
 *  验证提现密码
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/checkDepositPassword")
@Before(CheckUserKeyInterceptor.class)    
public class CheckDepositPasswordController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String pwd = isNotNullAndGet(Fields.PASSWORD,ErrCode.PAMAS_ERROR).toString();
		Assert.isTrue(new Md5Utils().getMD5(Base64.decodeAsString(pwd)).equals(user.getStr(Fields.REMARK )), ErrCode.PASSWORD_ERROR);
        returnJson();
	}	
	
}
