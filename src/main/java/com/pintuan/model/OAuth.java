package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;
/**
 * 第三方认证表
 * **/
@ModelBind(table = "pt_oauth",key = "openid")
public class OAuth extends BaseProjectModel<OAuth> {

	private static final long serialVersionUID = 1L;
	public static final OAuth dao = new OAuth();

}
