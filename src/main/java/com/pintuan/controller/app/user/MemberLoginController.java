package com.pintuan.controller.app.user;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 会员登录
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/memberLogin")
public class MemberLoginController extends BaseProjectController {
	private UserService userService = new UserService();

	public void index() throws CoreException {
		String thd_id = isNotNullAndGet(Fields.THD_ID,ErrCode.PAMAS_ERROR).toString();
        String pwd = isNotNullAndGet(Fields.PASSWORD,ErrCode.PASSWORD_IS_NULL).toString();
        Record user = userService.findUserByThdIdAndPwd(thd_id, pwd);
        if(null==user) {
        	throw new CoreException(ErrCode.LOGIN_ERROR);
        }
        setResp(Fields.USER_IDENTIFY_KEY,user.get(Fields.USER_ID));
        returnJson();
	}	
	
}
