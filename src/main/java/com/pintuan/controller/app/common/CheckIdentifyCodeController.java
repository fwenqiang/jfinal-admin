package com.pintuan.controller.app.common;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.IdentifyCode;
import com.pintuan.service.CommonService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 验证验证码
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/checkIdentifyCode")
public class CheckIdentifyCodeController extends BaseProjectController {
	private CommonService commonService = new CommonService();
	public void index() throws CoreException {
		String phoneNo = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PHONE_NO_IS_NULL).toString();
        String ideCde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.IDENTIFY_CODE_IS_NULL).toString();
        String ideCdeTyp = isNotNullAndGet(Fields.IDENTIFY_CODE_TYPE,ErrCode.IDENTIFY_CODE_TYPE_IS_NULL).toString();
        IdentifyCode identifyCode = commonService.validateIdentifyCode(phoneNo, ideCde,ideCdeTyp);        
        setResp(Fields.IDENTIFY_ID, identifyCode.get(Fields.IDENTIFY_ID));
        returnJson();
	}	
}
