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
 * 添加用户地址
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/addUserAddress")
@Before(CheckUserKeyInterceptor.class)    
public class AddUserAddressController extends BaseProjectController {
	private AddressService addressService = new AddressService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String rec_nme = isNotNullAndGet(Fields.REC_NME,ErrCode.PAMAS_ERROR).toString();
		String rec_pho = isNotNullAndGet(Fields.REC_PHO,ErrCode.PAMAS_ERROR).toString();
		String rec_adr = isNotNullAndGet(Fields.REC_ADR,ErrCode.PAMAS_ERROR).toString();
		String is_def = isNotNullAndGet(Fields.IS_DEF,ErrCode.PAMAS_ERROR).toString();
		if("1".equals(is_def)) {
			addressService.updateDefaultSet(usr_ide_key);
		}
        addressService.add(usr_ide_key, rec_nme, rec_pho, rec_adr, is_def);        
        returnJson();
	}	
	
}
