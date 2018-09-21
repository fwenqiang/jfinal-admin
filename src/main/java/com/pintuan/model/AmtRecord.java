package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;
/**
 * 收支记录表
 * **/
@ModelBind(table = "pt_amt_record",key = "red_id")
public class AmtRecord extends BaseProjectModel<AmtRecord> {

	private static final long serialVersionUID = 1L;
	public static final AmtRecord dao = new AmtRecord();

}
