package com.pintuan.controller.app.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.pintuan.util.Assert;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.IdentifyCode;
import com.pintuan.model.User;
import com.pintuan.service.CommonService;
import com.pintuan.service.UserService;
import com.pintuan.util.AliSmsUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 获取验证码
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/sendIdentifyCode")
public class SendIdentifyCodeController extends BaseProjectController {
	private CommonService commonService = new CommonService();
	private UserService userService = new UserService();
	public static Log log = LogFactory.getLog(SendIdentifyCodeController.class);
	public void index() throws CoreException {
		String phoneNo = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PHONE_NO_IS_NULL).toString();
        String ideCdeTyp = isNotNullAndGet(Fields.IDENTIFY_CODE_TYPE,ErrCode.IDENTIFY_CODE_TYPE_IS_NULL).toString();
        checkPama(phoneNo,ideCdeTyp);
        IdentifyCode ideCde = commonService.createIdentifyCode(phoneNo, ideCdeTyp);  
        AliSmsUtil.sendSms(ideCde.getStr(Fields.PHONE_NO),ideCde.getStr(Fields.IDENTIFY_CODE),ideCdeTyp);
        setResp(Fields.IDENTIFY_ID, ideCde.get(Fields.IDENTIFY_ID));
        returnJson();
	}
	
	//校验参数
	private void checkPama(String phoneNo,String ideCdeTyp) {
		if (Constants.IDE_TYP_REGISTER.equals(ideCdeTyp)) {  //注册：一个手机号可以注册多个账号
			// User user = userService.findFirstUserByPhone(phoneNo);  
			// Assert.isEmpty(user,ErrCode.PHONE_EXIST);
		}else if (Constants.IDE_TYP_UPDATE_PHONE.equals(ideCdeTyp)) { 
			// User user = userService.findFirstUserByPhone(phoneNo);  
			// Assert.isEmpty(user,ErrCode.PHONE_EXIST);
		}else if(Constants.IDE_TYP_RESETPWD.equals(ideCdeTyp)) {
			User user = userService.findFirstUserByPhone(phoneNo);
			Assert.notEmpty(user,ErrCode.USER_UNEXIST);
		}else {
			throw new CoreException(ErrCode.PAMAS_ERROR);
		}
	}
}
