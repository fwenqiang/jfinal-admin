package com.pintuan.controller.app.order;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Order;
import com.pintuan.model.User;
import com.pintuan.service.OrderService;
import com.pintuan.task.HandleInitOrderTask;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * 查询订单列表
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/queryOrderList")
@Before(CheckUserKeyInterceptor.class)  
public class QueryOrderListController extends BaseProjectController {
	private OrderService orderService = new OrderService();

	public void index() throws CoreException {
		String state = getData(Fields.STATE);
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		int page = getNPage();
		int size = getNSize();
		checkInitOrder(user.getStr(Fields.USER_ID));
		List<Record> orderList = orderService.findOrderList(user.getStr(Fields.USER_ID), state, page, size);
		
		setResp(Fields.ORDER_LIST, DBModelUtils.toMaps(orderList,Record.class));
		returnJson();
	}
	
	//查询为I的订单
		private void checkInitOrder(String usr_id) {
			List<Order> initOrderList = orderService.findUserOrderByState(usr_id,Constants.ORDER_STATE_INIT); 
			if(DBModelUtils.isEmpty(initOrderList)) {
				return ;
			}
			for(Order order:initOrderList) {
				SyncTaskExcutor.exc(new HandleInitOrderTask(order));
			}
		}

}
