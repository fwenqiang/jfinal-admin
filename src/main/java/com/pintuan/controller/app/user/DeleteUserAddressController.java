package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.service.AddressService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 删除用户地址
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/deleteUserAddress")
@Before(CheckUserKeyInterceptor.class)    
public class DeleteUserAddressController extends BaseProjectController {
	private AddressService addressService = new AddressService(); 
	public void index() throws CoreException {
		String adr_id = isNotNullAndGet(Fields.ADR_ID,ErrCode.PAMAS_ERROR).toString();
        addressService.delete(adr_id);        
        returnJson();
	}	
	
}
