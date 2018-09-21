package com.pintuan.controller.app.user;

import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Address;
import com.pintuan.model.User;
import com.pintuan.service.AddressService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询用户地址列表
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryUserAddressList")
@Before(CheckUserKeyInterceptor.class)    
public class QueryUserAddressListController extends BaseProjectController {
	private AddressService addressService = new AddressService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String is_def = getData(Fields.IS_DEF);
        List<Address> addressList = addressService.findByUsrId(usr_ide_key,is_def);
        setResp(Fields.ADDRESS_LIST,DBModelUtils.toMaps(addressList));
        returnJson();
	}	
	
}
