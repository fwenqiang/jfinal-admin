package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_deposit",key = "dep_id")
public class Deposit extends BaseProjectModel<Deposit> {

	private static final long serialVersionUID = 1L;
	public static final Deposit dao = new Deposit();

}
