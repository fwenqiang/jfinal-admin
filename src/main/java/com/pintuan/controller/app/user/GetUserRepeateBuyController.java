package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Order;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.OrderService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 判断用户是否可以复购
 */
@ControllerBind(controllerKey = "/pintuan/app/getUserRepeateBuy")
@Before(CheckUserKeyInterceptor.class)    
public class GetUserRepeateBuyController extends BaseProjectController {

	private BonusService bonusService = new BonusService();
	private OrderService orderService = new OrderService();
	
	public void index() throws CoreException {
		User user = (User) getAttribute(Fields.ATTR_USER_ENTITY);
		
		int flag = 0;
		
		Order order = orderService.findByUserId(user.getStr(Fields.USER_ID), "1990");
		if(order!=null){
			long bounsCount = bonusService.findBonusSum(user.getStr(Fields.USER_ID));
			if(bounsCount>=7){
				flag = 1;
			}
		}
		
        setResp("flag",flag);
        returnJson();
	}	

}
