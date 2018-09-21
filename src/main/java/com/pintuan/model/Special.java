package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_special",key = "spe_id")
public class Special extends BaseProjectModel<Special> {

	private static final long serialVersionUID = 1L;
	public static final Special dao = new Special();

}
