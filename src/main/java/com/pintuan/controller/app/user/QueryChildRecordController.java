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
 * 查询收徒记录
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryChildRecord")
@Before(CheckUserKeyInterceptor.class)    
public class QueryChildRecordController extends BaseProjectController {
	private UserService userService = new UserService();
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		long childCount = userService.findChildrenUserCount(usr_ide_key);
		long grandChildCount = userService.findGrandChildUserCount(usr_ide_key);
		setResp(Fields.CHD_NUM, childCount);
		setResp(Fields.GRD_CHD_NUM, grandChildCount);
        returnJson();
	}	
	
}
