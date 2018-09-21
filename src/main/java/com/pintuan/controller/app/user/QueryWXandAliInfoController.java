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

/**
 *  查询微信和阿里账号信息
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryWXandAliInfo")
@Before(CheckUserKeyInterceptor.class)    
public class QueryWXandAliInfoController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		UserPayInfo userPayInfo = userService.findUserPayInfo(usr_ide_key);
		if(userPayInfo!=null) {
		setRespMap(userPayInfo.getAttrs());
		}
        returnJson();
	}	
	
}
