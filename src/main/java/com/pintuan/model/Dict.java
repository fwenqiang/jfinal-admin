package com.pintuan.model;

import java.util.Map;

import com.alibaba.druid.support.json.JSONUtils;
import com.pintuan.common.Fields;
import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

/**
 * 字典表
 * @author Administrator
 *
 */
@ModelBind(table = "pt_dict",key = "dic_id")
public class Dict extends BaseProjectModel<Dict> {

	private static final long serialVersionUID = 1L;
	public static final Dict dao = new Dict();
	
	public Map<String,Object> getValMap(){
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> parMap = (Map<String, Object>) JSONUtils.parse(this.getStr(Fields.DIC_VAL));
			return parMap;
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
	public void setValMap(Map<String, Object> parMap){
		String jsonStr = JSONUtils.toJSONString(parMap);
		this.set(Fields.DIC_VAL, jsonStr);
	}

}
