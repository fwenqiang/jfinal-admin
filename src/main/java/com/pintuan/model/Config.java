package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

/**
 * 地址表
 * @author Administrator
 *
 */
@ModelBind(table = "pt_config",key = "cfg_id")
public class Config extends BaseProjectModel<Config> {

	private static final long serialVersionUID = 1L;
	public static final Config dao = new Config();

}
