package com.pintuan.controller.app.user;

import java.util.Date;

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

/**
 * 用户签到
 * 
 * @author zjh 2018-4-26
 */
@ControllerBind(controllerKey = "/pintuan/app/userSignIn")
@Before(CheckUserKeyInterceptor.class)    
public class UserSignInController extends BaseProjectController {
	
	private UserService userService = new UserService();
	
	public void index() throws CoreException {
		String usr_ide_key = getData(Fields.USER_IDENTIFY_KEY);
        User user = User.dao.findById(usr_ide_key);
        Assert.notEmpty(user, ErrCode.USER_UNEXIST);
        Date signTime = user.getDate(Fields.SIG_TME);
        if(signTime==null) {
        	int poi_scr = user.get(Fields.POI_SCR)==null?0:user.getInt(Fields.POI_SCR);
        	user.set(Fields.POI_SCR, poi_scr+10);
        	user.set(Fields.SIG_TME, new Date());
        	userService.save(user);
        	setResp(Fields.SIG_RES, "1");
        	returnJson();
        	return;
        }
        Date now = new Date();
        if(signTime.getDay()==now.getDay()&&signTime.getDate()==now.getDate()) {
        	throw new CoreException(ErrCode.TODAY_ALREADY_SIGN_IN);
        }else {
        	int poi_scr = user.get(Fields.POI_SCR)==null?0:user.getInt(Fields.POI_SCR);
        	user.set(Fields.POI_SCR, poi_scr+10);
        	user.set(Fields.SIG_TME, new Date());
        	userService.save(user);
        	setResp(Fields.SIG_RES, "1");
        }
        returnJson();
	}	
	
}
