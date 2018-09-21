package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_product",key = "pro_id")
public class Product extends BaseProjectModel<Product> {

	private static final long serialVersionUID = 1L;
	public static final Product dao = new Product();

}
