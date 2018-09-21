package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_identify_code",key = "ide_id")
public class IdentifyCode extends BaseProjectModel<IdentifyCode> {

	private static final long serialVersionUID = 1L;
	public static final IdentifyCode dao = new IdentifyCode();

}
