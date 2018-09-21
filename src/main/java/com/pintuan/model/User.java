package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_user",key = "usr_id")
public class User extends BaseProjectModel<User> {

	private static final long serialVersionUID = 1L;
	public static final User dao = new User();

}
