package com.pintuan.controller.app.page;

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.User;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;

/**
 *用户注册
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/page/userRegister")
public class UserRegisterController extends BaseProjectController {
	private static final String path = "/pages/pintuan/app_";
	private UserService userService = new UserService();
	public void index() throws CoreException {
            //重定向
	    String p_id = getPara("p_id");
	    String usr_num = "吾家优品";
	    String tit_url = Constants.DEFAULT_USER_TITLE_URL;
	    User user = userService.find(p_id);
	    
	    if(user!=null) {
	    	usr_num = user.getStr(Fields.USER_NAME);
	    	tit_url = StrUtils.isEmpty(user.getStr(Fields.TIT_URL))?tit_url:user.getStr(Fields.TIT_URL);
	    }else {
	    	p_id="";
	    }
	    setAttr("p_id", p_id);
	    setAttr(Fields.USER_NAME,usr_num);
	    setAttr(Fields.TIT_URL,tit_url);
        render(path + "packet_register.html");
            
	}	
	
	
}
