package com.pintuan.task;

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.bean.WXResult;
import com.pintuan.service.JnlService;
import com.pintuan.service.OrderService;
import com.pintuan.util.Assert;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.util.task.AsyncTaskExcutor;
import com.supyuan.util.task.Task;

/**
 * 
 * 同步为I订单任务
 * 
 * @author zjh
 *
 */
public class HandleInitOrderTask extends Task {
	private String ord_id;
	private Order order;
	private OrderService orderService = new OrderService();
	private JnlService jnlService = new JnlService();

	public HandleInitOrderTask(String ord_id) {
		this.ord_id = ord_id;
	}
	
	public HandleInitOrderTask(Order order) {
		this.order = order;
		this.ord_id = order.getStr(Fields.ORD_ID);
	}

	@Override
	public void customRun() {
        if(order==null) {
        	order = orderService.findOne(ord_id);
        	Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
        	Assert.isTrue(Constants.ORDER_STATE_INIT.equals(order.getStr(Fields.STATE)), ErrCode.STATE_ERROR);
        }
        Jnl jnl = jnlService.findByOrdId(ord_id);
		if(jnl==null) {
			throw new CoreException("");
		}
		WXResult result = WXRequestUtil.queryOrder(jnl.getStr(Fields.JNL_ID));
		if(result.isSuccess()) {
			/*orderService.updateOrderState(ord_id, result.getState());
			jnlService.updateJnlState(jnl, result.getState());
			order.set(Fields.STATE, result.getState());*/
			if(Constants.ORDER_STATE_SUCCESS.equals(result.getState())) { //成功
				orderService.updateOrderState(ord_id, result.getState());
				jnlService.updateJnlState(jnl, result.getState());
				order.set(Fields.STATE, result.getState());
				AsyncTaskExcutor.exc(new HandleOrderSuccessTask(order.getStr(Fields.USER_ID),order.getStr(Fields.PRO_ID),ord_id));
			}else if(Constants.ORDER_STATE_ABORT.equals(result.getState())) { //失败
				orderService.updateOrderState(ord_id, result.getState());
				jnlService.updateJnlState(jnl, result.getState());
				//order.set(Fields.STATE, result.getState());
			}else {  //初始化订单暂时不理
				/*Date crt_tme = jnl.getDate(Fields.CREATE_TIME);
				if(DateUtils.addSecond(crt_tme, 15*60).after(new Date())){  //超过15分钟订单无效
					orderService.updateOrderState(ord_id, Constants.ORDER_STATE_ABORT);
					jnlService.updateJnlState(jnl, Constants.ORDER_STATE_ABORT);
				}*/
			}
		}else {
			// throw new CoreException("");
			System.out.println("query order error");
		}
		
	}

	

}
