package com.pintuan.controller.app.user;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.User;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 根据账号获取会员信息
 */
@ControllerBind(controllerKey = "/pintuan/app/getUserByThdId")   
public class GetUserByThdIdController extends BaseProjectController {

	private UserService userService = new UserService();
	
	public void index() throws CoreException {
		String thd_id = isNotNullAndGet(Fields.THD_ID,ErrCode.PAMAS_ERROR).toString();
		
		User user = userService.findByThdId(thd_id);
        if(null==user) {
        	throw new CoreException(ErrCode.USER_UNEXIST);
        }
        setResp(Fields.USER_NAME,user.get(Fields.USER_NAME));
        returnJson();
	}	

}
