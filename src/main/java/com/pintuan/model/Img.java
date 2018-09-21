package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_img",key = "img_id")
public class Img extends BaseProjectModel<Img> {

	private static final long serialVersionUID = 1L;
	public static final Img dao = new Img();

}
