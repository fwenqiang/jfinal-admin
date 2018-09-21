package com.pintuan.controller.app.invoice;

import java.math.BigDecimal;
import java.util.Date;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Invoice;
import com.pintuan.model.Order;
import com.pintuan.service.OrderService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 * 新增发票
 * 
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/addInvoice")
@Before(CheckUserKeyInterceptor.class)
public class AddInvoiceController extends BaseProjectController {

	private OrderService orderService = new OrderService();

	public void index() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		String inv_typ= isNotNullAndGet(Fields.INV_TYP, ErrCode.PAMAS_ERROR).toString();
		String rec_pho= isNotNullAndGet(Fields.REC_PHO, ErrCode.PAMAS_ERROR).toString();
		String inv_tit= isNotNullAndGet(Fields.INV_TIT, ErrCode.PAMAS_ERROR).toString();
		String per_no= getData(Fields.PER_NO);
		String inv_det= isNotNullAndGet(Fields.INV_DET, ErrCode.PAMAS_ERROR).toString();
		Invoice invoice = orderService.findInvoiceByOrdId(ord_id);
		Order order = orderService.findOne(ord_id);
		Assert.isEmpty(invoice, ErrCode.INVOICE_EXIST);
		Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
		BigDecimal amt = order.getBigDecimal(Fields.PRO_AMT);
		checkType(inv_typ); 
		Invoice inv = new Invoice();
		inv.set(Fields.INV_ID,UuidUtils.getUUID2());
		inv.set(Fields.INV_TYP,inv_typ);
		inv.set(Fields.REC_PHO,rec_pho);
		inv.set(Fields.REC_EML,getData(Fields.REC_EML));
		inv.set(Fields.INV_TIT,inv_tit);
		inv.set(Fields.PER_NO,per_no);
		inv.set(Fields.INV_DET,inv_det);
		inv.set(Fields.AMT,amt);
		inv.set(Fields.CREATE_TIME,new Date());
		inv.set(Fields.ORD_ID,ord_id);
		inv.save();
		setResp(Fields.INV_ID, inv.getStr(Fields.INV_ID));
		returnJson();
	}

	private void checkType(String inv_typ) {
		if(inv_typ.equals("1")||inv_typ.equals("2")) {
			return ;
		}
		throw new CoreException(ErrCode.PAMAS_ERROR);
	}
	

}
