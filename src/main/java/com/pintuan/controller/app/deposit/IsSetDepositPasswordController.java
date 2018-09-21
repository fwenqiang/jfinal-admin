package com.pintuan.controller.app.deposit;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;

/**
 * 查询是否已设置提现密码
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/isSetDepositPassword")
@Before(CheckUserKeyInterceptor.class)    
public class IsSetDepositPasswordController extends BaseProjectController {
	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String remark = user.getStr(Fields.REMARK);
		if(StrUtils.isEmpty(remark)) {
			setResp(Fields.IS_SET, "0");
		}else {
			setResp(Fields.IS_SET, "1");
		}
        returnJson();
	}	
	
}
