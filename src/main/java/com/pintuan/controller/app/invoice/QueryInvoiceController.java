package com.pintuan.controller.app.invoice;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Invoice;
import com.pintuan.service.OrderService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询发票
 * 
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/queryInvoice")
@Before(CheckUserKeyInterceptor.class)
public class QueryInvoiceController extends BaseProjectController {

	private OrderService orderService = new OrderService();

	public void index() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		
		Invoice invoice = orderService.findInvoiceByOrdId(ord_id);
		if(invoice!=null) {
		   setRespMap(invoice.getAttrs());
		}
		returnJson();
	}

}
