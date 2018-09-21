package com.pintuan.controller.app.common;

import java.io.File;

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
 * 下载最新版本
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/downLoadLatestVersion")
public class DownLoadLatestVersionController extends BaseProjectController {
	private DictService dictService = new DictService();
	public void index() throws CoreException {
		String app_typ = isNotNullAndGet(Fields.APP_TYP,ErrCode.PAMAS_ERROR).toString();
		Dict dict = dictService.findByKey(app_typ+"_version");
		if(dict!=null){
			File file = new File("/home/root/temp/1.apk");
		     //本地的一张图片
		        if (file.exists()) { //如果文件存在
		            renderFile(file);
		        } else {
		            renderJson();
		        }
		}
        //returnJson();
	}	
}
