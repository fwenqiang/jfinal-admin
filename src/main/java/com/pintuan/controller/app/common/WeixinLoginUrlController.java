package com.pintuan.controller.app.common;

import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;

/**
 * before微信公众授权登录
 * 
 * @author zjh 2018-4-24
 */
@ControllerBind(controllerKey = "/pintuan/app/weixinloginurl")
public class WeixinLoginUrlController extends BaseProjectController {

	private static final String path = "/pages/pintuan/weixin_";

	public void index() {
		String state = getPara("state");
		if(state==null||StrUtils.empty(state)) {
			state = "STATE";
		}
		setAttr("state", state);
		render(path + "login.html");
	}

}
