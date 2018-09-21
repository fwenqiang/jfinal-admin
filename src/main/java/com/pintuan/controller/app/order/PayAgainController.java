package com.pintuan.controller.app.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.common.WXConstants;
import com.pintuan.common.WeixinFields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Invoice;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.User;
import com.pintuan.service.JnlService;
import com.pintuan.service.OrderService;
import com.pintuan.service.ProductService;
import com.pintuan.service.SchedulingService;
import com.pintuan.task.HandleInitOrderTask;
import com.pintuan.util.Assert;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * 再次支付下单
 * 
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/payAgain")
@Before(CheckUserKeyInterceptor.class)
public class PayAgainController extends BaseProjectController {

	private JnlService jnlService = new JnlService();
	private OrderService orderService = new OrderService();
	private SchedulingService schedulingService = new SchedulingService();
	private ProductService productService = new ProductService();

	public void index() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		User user = (User) getAttribute(Fields.ATTR_USER_ENTITY);
		String pay_chl = isNotNullAndGet(Fields.PAY_CHL, ErrCode.PAY_CHL_IS_NULL).toString();
		String req_chl = isNotNullAndGet(Fields.REQ_CHL, ErrCode.REQ_CHL_IS_NULL).toString();
		Order order = orderService.findOne(ord_id);
		Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
		if (!Constants.ORDER_STATE_INIT.equals(order.getStr(Fields.STATE))) {
			throw new CoreException(ErrCode.ORDER_STATE_NOT_INIT);
		}
		checkInitOrder(order);
		Product product = productService.findById(order.getStr(Fields.PRO_ID));
		Assert.notEmpty(product, ErrCode.PRO_NOT_EXIST);
		// 判断下单资格
		checkCondition(user.getStr(Fields.USER_ID), product);

		Order newOrder = new Order();
		//BeanUtils.copy(order, newOrder);  
		order.copy(newOrder);
		// 订单作废
		abortOrder(order);
		newOrder.set(Fields.ORD_ID, UuidUtils.randomNumStr(15));
		newOrder.set(Fields.STATE, Constants.ORDER_STATE_INIT);
		copyInvoice(ord_id, newOrder.getStr(Fields.ORD_ID)); 
		if (StrUtils.isNotEmpty(getData(Fields.REC_NME)))
			newOrder.set(Fields.REC_NME, getData(Fields.REC_NME));
		if (StrUtils.isNotEmpty(getData(Fields.REC_PHO)))
			newOrder.set(Fields.REC_PHO, getData(Fields.REC_PHO));
		if (StrUtils.isNotEmpty(getData(Fields.REC_ADR)))
			newOrder.set(Fields.REC_ADR, getData(Fields.REC_ADR));
		orderService.add(newOrder);

		if (Constants.PAY_CHL_WX.equals(pay_chl)) { // 微信支付
			dealWXPay(newOrder, pay_chl, req_chl, user);
		} else if (Constants.PAY_CHL_ALI.equals(pay_chl)) { // 阿里支付

		} else if (Constants.PAY_CHL_UNIPAY.equals(pay_chl)) { // 银联支付

		} else {
			throw new CoreException(ErrCode.PAY_CHL_ERROR);
		}

		setResp(Fields.ORD_ID, newOrder.get(Fields.ORD_ID));
		returnJson();
	}
	
	private void copyInvoice(String ord_id,String new_ord_id) {
		Invoice invoice = orderService.findInvoiceByOrdId(ord_id);
		if(invoice!=null) {
			Invoice newInvoice = new Invoice();
			invoice.copy(newInvoice);
			newInvoice.set(Fields.ORD_ID, new_ord_id);
			newInvoice.set(Fields.CREATE_TIME, new Date());
			newInvoice.update();
		}
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

	// 订单作废
	private void abortOrder(Order order) {
		Jnl jnl = jnlService.findByOrdId(order.getStr(Fields.ORD_ID));
		Assert.notEmpty(jnl, ErrCode.JNL_UN_EXIST);
		orderService.updateOrderState(order.getStr(Fields.ORD_ID), Constants.ORDER_STATE_ABORT);
		jnlService.updateJnlState(jnl, Constants.ORDER_STATE_ABORT);
	}

	// 时间戳
	private String getTimestamp() {
		String t = System.currentTimeMillis() + "";
		return t.substring(0, 10);
	}

	// 微信支付
	private void dealWXPay(Order order, String pay_chl, String req_chl, User user) {
		String body = order.getStr(Fields.PRO_NME);
		BigDecimal tol_fee = order.getBigDecimal(Fields.PRO_AMT);//new BigDecimal("0.01");//
		//tol_fee = new BigDecimal("0.01");
		Jnl jnl = jnlService.initWXJnl(order.getStr(Fields.ORD_ID), tol_fee, pay_chl, req_chl, user);
		Map<String, String> res = WXRequestUtil.SendPayment(body, jnl.getStr(Fields.JNL_ID), tol_fee,
				order.getStr(Fields.ORD_ID));
		if (WXConstants.REP_SUCCESS.equals(res.get(Fields.RETURN_CODE))) {
			System.out.println("交易成功：" + res);
			String timestamp = getTimestamp();
			String noncestr = WXRequestUtil.NonceStr();
			setResp(WeixinFields.APPID, res.get(WeixinFields.APPID));
			setResp(WeixinFields.PARTNERID, res.get(WeixinFields.MCH_ID));
			setResp(WeixinFields.PREPAYID, res.get("prepay_id"));
			setResp(WeixinFields.NONCESTR, noncestr);
			setResp(WeixinFields.TIMESTAMP, timestamp);
			setResp(WeixinFields.SIGN, WXRequestUtil.getAPPSign(res.get("prepay_id"), noncestr, timestamp));
			orderService.updateOrderState(order.getStr(Fields.ORD_ID), Constants.ORDER_STATE_INIT);
			jnlService.updateJnlState(jnl, Constants.JNL_STATE_INIT);
		} else {
			System.out.println("交易失败:" + res);
			jnlService.updateJnlState(jnl, Constants.JNL_STATE_ABORT);
			throw new CoreException(ErrCode.WX_UNIFIED_FAIL);
		}
	}

	// 同步为I的订单
	private void checkInitOrder(Order order) {
		SyncTaskExcutor.exc(new HandleInitOrderTask(order));
		order = orderService.findOne(order.getStr(Fields.ORD_ID));
		Assert.isTrue(Constants.ORDER_STATE_INIT.equals(order.getStr(Fields.STATE)), ErrCode.ORDER_STATE_NOT_INIT);
	}

}
