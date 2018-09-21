package com.pintuan.controller.app.bonus;

import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Deposit;
import com.pintuan.service.DepositService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询提现列表
 * 
 * @author zjh 2018-5-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryDepositList")
@Before(CheckUserKeyInterceptor.class)    
public class QueryDepositListController extends BaseProjectController {
	private DepositService depositService = new DepositService();
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		
		List<Deposit> depositList = depositService.findDepositList(usr_ide_key);
        setResp(Fields.DEPOSIT_LIST,DBModelUtils.toMaps(depositList));
        returnJson();
	}	
	
	
	
}
