package com.pintuan.controller.app.common;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.Dict;
import com.pintuan.model.IdentifyCode;
import com.pintuan.service.CommonService;
import com.pintuan.service.DictService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询最新版本
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryLatestVersion")
public class QueryLatestVersionController extends BaseProjectController {
	private DictService dictService = new DictService();
	public void index() throws CoreException {
		String app_typ = isNotNullAndGet(Fields.APP_TYP,ErrCode.PAMAS_ERROR).toString();
		Dict dict = dictService.findByKey(app_typ+"_version");
		if(dict!=null){
			setRespMap(dict.getValMap());
		}
        returnJson();
	}	
}
