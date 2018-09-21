package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_user_grid",key = "usr_id")
public class UserGrid extends BaseProjectModel<UserGrid> {

	private static final long serialVersionUID = 1L;
	public static final UserGrid dao = new UserGrid();

}
