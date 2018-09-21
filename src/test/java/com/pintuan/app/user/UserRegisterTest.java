package com.pintuan.app.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.service.CommonService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 用户注册
 * 
 * @author zjh 2018-4-6
 */
public class UserRegisterTest{
	
	public static void main(String[] args) throws CoreException {
		System.out.println("sdf");
		String value = "{\"pho_no\":\"18825180355\",\"ide_cde_typ\":\"1\"}";
		try {
			System.out.println(URLEncoder.encode(value, "utf-8"));
			//%7B%22pho_no%22%3A%2218825180355%22%2C%22pwd%22%3A%22MTIzNDU2%22%2C%22ide_cde%22%3A%2223%22%7D
			
			//%7B%22pho_no%22%3A%2218825180355%22%2C%22ide_cde_typ%22%3A%221%22%7D
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}	
}
