package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.UserGrid;
import com.pintuan.service.AddressService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *  查询用户排位
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryUserGrid")
@Before(CheckUserKeyInterceptor.class)    
public class QueryUserGridController extends BaseProjectController {
	private UserService userService = new UserService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		
		UserGrid userGrid = userService.findUserGrid(usr_ide_key);    
		if(userGrid==null) {
			userGrid = userService.addUserGrid(usr_ide_key);
		}
		setRespMap(userGrid.getAttrs());
        returnJson();
	}	
	
}
