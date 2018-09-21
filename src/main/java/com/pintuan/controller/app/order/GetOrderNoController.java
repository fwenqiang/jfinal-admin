package com.pintuan.controller.app.order;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.service.OrderService;
import com.pintuan.service.ProductService;
import com.pintuan.service.SchedulingService;
import com.pintuan.task.HandleInitOrderTask;
import com.pintuan.task.HandleOrderSuccessTask;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.task.AsyncTaskExcutor;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * 获取订单号
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/getOrderNo")
@Before(CheckUserKeyInterceptor.class)
public class GetOrderNoController extends BaseProjectController {

	private OrderService orderService = new OrderService();
	private ProductService productService = new ProductService();
	private SchedulingService schedulingService = new SchedulingService();

	public void index() throws CoreException {
		String pro_id = isNotNullAndGet(Fields.PRO_ID, ErrCode.PRO_ID_IS_NULL).toString();
		String usr_id = isNotNullAndGet(Fields.USER_IDENTIFY_KEY, ErrCode.USER_UNEXIST).toString();
		Product product = productService.findById(pro_id);
		Assert.notEmpty(product, ErrCode.PRO_NOT_EXIST);
		Order oldOrder = checkInitOrder(usr_id, product);
		if (oldOrder != null) {
			setResp(Fields.ORD_ID, oldOrder.get(Fields.ORD_ID));
			returnJson();
			return;
		}
		checkCondition(usr_id, product); // 判断是否可以下单
		Order order = orderService.add(usr_id, product);
		setResp(Fields.ORD_ID, order.get(Fields.ORD_ID));
		returnJson();
	}

	/**
	 * A系统 条件：有且仅限购入1990 拿完7次段位分红方可进入B系统; B系统 条件：有且仅限购入800
	 * 条件1：分享1人拿到第8段位分红，条件2：分享2人拿到第9段分红，条件3：分享3人拿到第11段分红，条件4：分享4人拿到第14段分红
	 * 
	 * @param usr_id
	 * @param product
	 */
	private void checkCondition(String usr_id, Product product) {
		List<Record> records = orderService.find(usr_id, product); // 同类商品只能下单一次
		if (records != null && records.size() > 0) {
			throw new CoreException(ErrCode.SAME_TYPE_ORDER_DUPLICATE);
		}
		// 有且仅限购入1990 拿完7次段位分红方可进入B系统;
		if ("800".equals(product.getStr(Fields.AMT_TYP))) {
			String amt_typ = "1990";
			Order order1990 = orderService.findByUserId(usr_id, amt_typ);
			Assert.notEmpty(order1990, ErrCode.NEED_1990_FIRST);
			int sum = (int) schedulingService.findUserCanGainBonus(order1990.getInt(Fields.SDL_ID), amt_typ);
			Assert.isTrue(sum >= 7, ErrCode.NEED_HAVE_7_SCHDULING);
		}
	}

	// 查询为I的订单
	private Order checkInitOrder(String usr_id, Product product) {
		Order order = orderService.findByState(usr_id, product.getStr(Fields.AMT_TYP), Constants.ORDER_STATE_BEGIN);
		if (order != null) {
			order.set(Fields.CREATE_TIME, new Date());
			order.set(Fields.PRO_ID, product.get(Fields.PRO_ID));
			order.set(Fields.PRO_NME, product.get(Fields.PRO_NME));
			order.set(Fields.PRO_AMT, product.get(Fields.PRO_AMT));
			order.update();
			return order;
		}
		order = orderService.findByState(usr_id, product.getStr(Fields.AMT_TYP), Constants.ORDER_STATE_INIT);
		if (order != null) {
			SyncTaskExcutor.exc(new HandleInitOrderTask(order));
			order = orderService.findByState(usr_id, product.getStr(Fields.AMT_TYP), Constants.ORDER_STATE_INIT);
			Assert.isEmpty(order, ErrCode.INIT_ORDER_IS_NOT_NULL);
		}
		return null;
	}

}
