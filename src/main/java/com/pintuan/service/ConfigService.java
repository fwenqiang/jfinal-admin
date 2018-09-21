package com.pintuan.service;

import com.pintuan.model.Config;
import com.supyuan.jfinal.base.BaseService;

public class ConfigService extends BaseService {

	public Config findConfig(String cfg_id) {
		return Config.dao.findById(cfg_id);
	}

	/**
	 * 更改红包或者钱包显示与否
	 * @param config
	 */
	public void update(Config config) {
		config.update();
	}
}
