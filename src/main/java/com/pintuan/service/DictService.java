package com.pintuan.service;

import java.util.Date;

import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.Dict;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.extend.UuidUtils;

/**字典**/
public class DictService extends BaseService {

	/**
	 * 添加
	 * 
	 */
	public Dict add(String key,String val) {
		Dict dict = new Dict();
		dict.set(Fields.DIC_ID, UuidUtils.getUUID2());
		dict.set(Fields.DIC_KEY, key);
		dict.set(Fields.DIC_VAL, val);
		dict.set(Fields.STATE, Constants.ACCESS_STATE);
		dict.set(Fields.CREATE_TIME, new Date());
		dict.save();
		return dict;
	}
	
	public void update(Dict dict) {
		dict.update();
	}
	
	public Dict findByKey(String key) {
		return Dict.dao.findFirstByWhere("where dic_key=? and state=?",key,Constants.ACCESS_STATE);
	}
	
}
