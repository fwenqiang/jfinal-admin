package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_bank_card",key = "car_id")
public class BankCard extends BaseProjectModel<BankCard> {

	private static final long serialVersionUID = 1L;
	public static final BankCard dao = new BankCard();

}
