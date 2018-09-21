package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_invoice",key = "inv_id")
public class Invoice extends BaseProjectModel<Invoice> {

	private static final long serialVersionUID = 1L;
	public static final Invoice dao = new Invoice();

}
