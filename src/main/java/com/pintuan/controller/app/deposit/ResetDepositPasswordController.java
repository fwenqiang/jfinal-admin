package com.pintuan.controller.app.deposit;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.service.CommonService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;

/**
 *  修改提现密码
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/resetDepositPassword")
@Before(CheckUserKeyInterceptor.class)    
public class ResetDepositPasswordController extends BaseProjectController {
	private UserService userService = new UserService(); 
	private CommonService commonService = new CommonService(); 
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String ide_cde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.PAMAS_ERROR).toString();
		String pwd = isNotNullAndGet(Fields.NEW_PASSWORD,ErrCode.PAMAS_ERROR).toString();
		commonService.validateIdentifyCode(user.getStr(Fields.PHONE_NO), ide_cde, "2");
	    user.set(Fields.REMARK, new Md5Utils().getMD5(Base64.decodeAsString(pwd)));
	    userService.save(user);
        returnJson();
	}		
	
}
