package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_user_pay_info",key = "pay_id")
public class UserPayInfo extends BaseProjectModel<UserPayInfo> {

	private static final long serialVersionUID = 1L;
	public static final UserPayInfo dao = new UserPayInfo();

}
