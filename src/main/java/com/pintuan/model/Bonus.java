package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_bonus",key = "bon_id")
public class Bonus extends BaseProjectModel<Bonus> {

	private static final long serialVersionUID = 1L;
	public static final Bonus dao = new Bonus();

}
