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
 * 更新用户地址
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/updateUserAddress")
@Before(CheckUserKeyInterceptor.class)    
public class UpdateUserAddressController extends BaseProjectController {
	private AddressService addressService = new AddressService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String adr_id = isNotNullAndGet(Fields.ADR_ID,ErrCode.PAMAS_ERROR).toString();
		String rec_nme = getData(Fields.REC_NME);
		String rec_pho = getData(Fields.REC_PHO);
		String rec_adr = getData(Fields.REC_ADR);
		String is_def = getData(Fields.IS_DEF);
		if("1".equals(is_def)) {
			addressService.updateDefaultSet(usr_ide_key);
		}
        addressService.update(adr_id, rec_nme, rec_pho, rec_adr, is_def);        
        returnJson();
	}	
	
}
