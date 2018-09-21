package com.pintuan.controller.app.user;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.User;
import com.pintuan.service.CommonService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;

/**
 * 重设密码
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/resetPassword")
public class ResetPasswordController extends BaseProjectController {
	private CommonService commonService = new CommonService();
	private UserService userService = new UserService();
	public void index() throws CoreException {
		String thd_id = isNotNullAndGet(Fields.THD_ID,ErrCode.PAMAS_ERROR).toString();
        String ide_cde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.PAMAS_ERROR).toString();
        String new_pwd = isNotNullAndGet(Fields.NEW_PWD,ErrCode.NEW_PWD_IS_NULL).toString();
        User user = userService.findByThdId(thd_id);
        Assert.notEmpty(user, ErrCode.THD_ID_UN_EXIST);
        commonService.validateIdentifyCode(user.getStr(Fields.PHONE_NO), ide_cde, "2");
        user.set(Fields.PASSWORD, new Md5Utils().getMD5(Base64.decodeAsString(new_pwd)));
        userService.save(user);
        setResp(Fields.USER_IDENTIFY_KEY, user.getStr(Fields.USER_ID));
        returnJson();
	}	
}
