package com.pintuan.controller.app.user;

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
 * 修改密码
 * 
 * @author zjh 2018-5-12
 */
@ControllerBind(controllerKey = "/pintuan/app/updatePassword")
@Before(CheckUserKeyInterceptor.class)  
public class UpdatePasswordController extends BaseProjectController {
	private UserService userService = new UserService();
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		Assert.notEmpty(user, ErrCode.USER_UNEXIST);
        String newPwd = isNotNullAndGet(Fields.NEW_PWD,ErrCode.NEW_PWD_IS_NULL).toString();
        String oldPwd = isNotNullAndGet(Fields.OLD_PWD,ErrCode.OLD_PWD_IS_NULL).toString();
        String pwd = user.get(Fields.PASSWORD);
        Assert.isTrue(pwd.equals(new Md5Utils().getMD5(Base64.decodeAsString(oldPwd))), ErrCode.PASSWORD_ERROR);
        user.set(Fields.PASSWORD, new Md5Utils().getMD5(Base64.decodeAsString(newPwd)));
        userService.save(user);
        returnJson();
	}	
}
