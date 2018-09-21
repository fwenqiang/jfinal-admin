package com.pintuan.controller.app.page;

import com.pintuan.base.CoreException;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *徒弟的树
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/myChildTree")
public class MyChildTreeController extends BaseProjectController {
	private static final String path = "/pages/pintuan/app_";
	
	public void index() throws CoreException {
            //重定向
		    String usr_key_id = getPara("usr_key_id");
		    setAttr("usr_key_id", usr_key_id);
            render(path + "myChildTree.html");
            
	}	
	
	
}
