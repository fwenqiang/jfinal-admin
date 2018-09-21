package com.pintuan.controller.app.user;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.User;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;

/**
 * 用户删除，测试用
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/updateUserInfo")
public class UpdateUserInfoController extends BaseProjectController {
	private UserService userService = new UserService();
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String usr_nme = getData(Fields.USER_NAME);
		String tit_url = getData(Fields.TIT_URL);
		User user = new User();
		user.set(Fields.USER_ID, usr_ide_key);
		if(StrUtils.isNotEmpty(usr_nme)) {
			user.set(Fields.USER_NAME, usr_nme);
		}
		if(StrUtils.isNotEmpty(tit_url)) {
			user.set(Fields.TIT_URL, tit_url);
		}
        userService.save(user);        
        returnJson();
	}	
}
