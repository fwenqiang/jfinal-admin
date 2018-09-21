package com.pintuan.controller.console.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Deposit;
import com.pintuan.model.Invoice;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.DepositService;
import com.pintuan.service.JnlService;
import com.pintuan.service.OrderService;
import com.pintuan.service.UserService;
import com.pintuan.task.HandleOrderSuccessTask;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.pintuan.util.Excel;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;
import com.supyuan.util.task.AsyncTaskExcutor;

/**
 * 发票
 * 
 * 
 * @author zjh 2018-6-14
 */
@ControllerBind(controllerKey = "/pintuan/console/invoice")
public class InvoiceController extends BaseProjectController {

	private OrderService orderService = new OrderService();
	private JnlService jnlService = new JnlService();
	private DepositService depositService = new DepositService();
	private BonusService bonusService = new BonusService();
	private UserService userService = new UserService();

	public void index() throws CoreException {
		queryInvoiceList();
	}

	// 查询列表
	public void queryInvoiceList() throws CoreException {
		int page = getNPage();
		int size = getNSize();
		List<Record> invoiceList = orderService.findInvoiceList( page, size);
		long total = orderService.findSize();
		setResp("invoice_list", DBModelUtils.toMaps(invoiceList,Record.class));
		setResp(Fields.TOTAL, total);
		returnJson();
	}
	
	// 更新发票状态
		public void updateInvoiceState() throws CoreException {
			String inv_id = isNotNullAndGet(Fields.INV_ID, ErrCode.PAMAS_ERROR).toString();
			String state = isNotNullAndGet(Fields.STATE, ErrCode.PAMAS_ERROR).toString();
			Invoice invoice = orderService.findOneInvoice(inv_id);
			Assert.notEmpty(invoice, ErrCode.UN_EXIST);
			invoice.set(Fields.STATE, state);
			invoice.update();
			returnJson();
		}

}
