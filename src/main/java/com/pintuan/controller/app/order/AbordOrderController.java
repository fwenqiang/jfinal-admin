package com.pintuan.controller.app.order;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Order;
import com.pintuan.service.OrderService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 *取消订单
 *                                      
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/abordOrder")
@Before(CheckUserKeyInterceptor.class)    
public class AbordOrderController extends BaseProjectController {
	
	private OrderService orderService = new OrderService();
	
	public void index() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		Order order = orderService.findOne(ord_id);
		Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
		orderService.abordOrder(order);
        returnJson();
	}
	
	
	
	
}
