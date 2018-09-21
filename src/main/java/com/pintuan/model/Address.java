package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;
/**
 * 地址表
 * @author Administrator
 *
 */
@ModelBind(table = "pt_address",key = "adr_id")
public class Address extends BaseProjectModel<Address> {

	private static final long serialVersionUID = 1L;
	public static final Address dao = new Address();

}
