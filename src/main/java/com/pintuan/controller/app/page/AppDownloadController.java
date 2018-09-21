package com.pintuan.controller.app.page;

import com.pintuan.base.CoreException;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *app下载
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/appDownload")
public class AppDownloadController extends BaseProjectController {
	private static final String path = "/pages/pintuan/app_";
	
	public void index() throws CoreException {
            //重定向
            render(path + "download.html");
            
	}	
	
	
}
