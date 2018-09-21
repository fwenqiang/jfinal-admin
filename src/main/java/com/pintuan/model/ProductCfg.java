package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_product_cfg",key = "pro_cfg_id")
public class ProductCfg extends BaseProjectModel<ProductCfg> {

	private static final long serialVersionUID = 1L;
	public static final ProductCfg dao = new ProductCfg();

}
