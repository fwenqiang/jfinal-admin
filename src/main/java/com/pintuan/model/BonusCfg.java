package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_bonus_cfg",key = "bon_cfg_id")
public class BonusCfg extends BaseProjectModel<BonusCfg> {

	private static final long serialVersionUID = 1L;
	public static final BonusCfg dao = new BonusCfg();

}
