package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_jnl",key = "jnl_id")
public class Jnl extends BaseProjectModel<Jnl> {

	private static final long serialVersionUID = 1L;
	public static final Jnl dao = new Jnl();

}
