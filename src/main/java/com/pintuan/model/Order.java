package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_order",key = "ord_id")
public class Order extends BaseProjectModel<Order> {

	private static final long serialVersionUID = 1L;
	public static final Order dao = new Order();

}
