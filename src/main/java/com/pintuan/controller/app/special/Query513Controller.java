package com.pintuan.controller.app.special;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Img;
import com.pintuan.model.Product;
import com.pintuan.model.Special;
import com.pintuan.model.User;
import com.pintuan.service.ImageService;
import com.pintuan.service.ProductService;
import com.pintuan.service.SpecialService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询513特色业务
 * 
 * @author zjh 2018-4-26
 */
@ControllerBind(controllerKey = "/pintuan/app/query513")
@Before(CheckUserKeyInterceptor.class)   
public class Query513Controller extends BaseProjectController {
	private SpecialService specialService = new SpecialService();

	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);

		Special special = specialService.find(user.getStr(Fields.USER_ID),"1");
		if(special!=null) {
		  setResp(Fields.SPE_ID, special.getStr(Fields.SPE_ID));
		  setResp(Fields.AMT, special.get(Fields.AMT));
		}
	
		returnJson();
	}

}
