package com.pintuan.controller.app.user;

//import org.springframework.util.Assert;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.service.CommonService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *  修改手机号
 * 
 * @author zjh 2018-6-9
 */
@ControllerBind(controllerKey = "/pintuan/app/updatePhoneNo")
@Before(CheckUserKeyInterceptor.class)    
public class UpdatePhoneNoController extends BaseProjectController {
	private CommonService commonService = new CommonService(); 
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String pho_no = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PAMAS_ERROR).toString();
		String new_pho_no = isNotNullAndGet(Fields.NEW_PHO_NO,ErrCode.PAMAS_ERROR).toString();
		String ide_cde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.PAMAS_ERROR).toString();
		commonService.validateIdentifyCode(new_pho_no, ide_cde, Constants.IDE_TYP_UPDATE_PHONE);
		Assert.isTrue(pho_no.equals(user.getStr(Fields.PHONE_NO)),ErrCode.PHO_NO_ERROR);
		user.set(Fields.PHONE_NO, new_pho_no);
		user.update();
        returnJson();
	}	
	
}
