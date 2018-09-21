package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_scheduling",key = "sdl_id")
public class Scheduling extends BaseProjectModel<Scheduling> {

	private static final long serialVersionUID = 1L;
	public static final Scheduling dao = new Scheduling();

}
