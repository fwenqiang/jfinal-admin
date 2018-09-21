package com.pintuan.controller.app.order;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.service.OrderService;
import com.pintuan.task.HandleInitOrderTask;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * 查询订单详情
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/queryOrderDetail")
@Before(CheckUserKeyInterceptor.class)  
public class QueryOrderDetailController extends BaseProjectController {
	private OrderService orderService = new OrderService();

	public void index() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();

		Record order = orderService.findOrderDetail(ord_id);
		Assert.notEmpty(order,ErrCode.ORDER_NOT_EXIST);
		
		String state = order.getStr(Fields.STATE);
		if(Constants.ORDER_STATE_INIT.equals(state)) {  //同步订单状态
			SyncTaskExcutor.exc(new HandleInitOrderTask(order.getStr(Fields.ORD_ID)));
			order = orderService.findOrderDetail(ord_id);
		}
		
		setRespMap(order.getColumns());
		returnJson();
	}
}
