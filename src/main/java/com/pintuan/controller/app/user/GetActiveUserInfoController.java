package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;

/**
 * 获得当前用户信息
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/getActiveUserInfo")
@Before(CheckUserKeyInterceptor.class)    
public class GetActiveUserInfoController extends BaseProjectController {
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
        User user = User.dao.findById(usr_ide_key);
        Assert.notEmpty(user, ErrCode.USER_UNEXIST);
        user.remove(Fields.PASSWORD);
        if(StrUtils.isEmpty(user.getStr(Fields.TIT_URL))) {
        	user.set(Fields.TIT_URL, Constants.DEFAULT_USER_TITLE_URL);
        }
        setRespMap(user.getAttrs());
        returnJson();
	}	
	
}
