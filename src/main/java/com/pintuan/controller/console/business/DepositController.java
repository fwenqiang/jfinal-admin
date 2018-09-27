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
 * 提现
 * 
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/console/deposit")
public class DepositController extends BaseProjectController {

	private OrderService orderService = new OrderService();
	private JnlService jnlService = new JnlService();
	private DepositService depositService = new DepositService();
	private BonusService bonusService = new BonusService();
	private UserService userService = new UserService();

	public void index() throws CoreException {
		queryDepositList();
	}

	// 查询列表
	public void queryDepositList() throws CoreException {
		String usr_nme = StrUtils.isEmpty(getData(Fields.USER_NAME)) ? "" : getData(Fields.USER_NAME);
		String crt_time = getData(Fields.CREATE_TIME);

		String state = getData(Fields.STATE);
		int page = getNPage();
		int size = getNSize();
		List<Record> depositList = depositService.findDepositList(usr_nme, state, crt_time, page, size);
		long total = depositService.findSize(usr_nme, crt_time, state);
		setResp(Fields.DEPOSIT_LIST, DBModelUtils.toMaps(depositList, Record.class));
		setResp(Fields.TOTAL, total);
		returnJson();
	}

	// 提现审核
	public void depositShenhe() throws CoreException {
		String dep_id = isNotNullAndGet(Fields.DEP_ID, ErrCode.PAMAS_ERROR).toString();
		String state = getData(Fields.STATE);
		Deposit deposit = depositService.findById(dep_id);
		Assert.notEmpty(deposit, ErrCode.UN_EXIST);
		Assert.isTrue("1".equals(deposit.get(Fields.STATE)), ErrCode.STATE_ERROR);
		deposit.set(Fields.STATE, state);
		deposit.set(Fields.UPDATE_TIME, new Date());
		// deposit.set(Fields.TAR_AMT,
		// getTarAmt(deposit.getBigDecimal(Fields.AMT)));
		deposit.update();
		if (state.equals("2")) {
			addAmtRecord(deposit.getStr(Fields.USER_ID), deposit.getBigDecimal(Fields.AMT));
		}
		returnJson();
	}

	private void addAmtRecord(String usr_id, BigDecimal amt) {
		BonusCfg bonusCfg = bonusService.findBonusCfg(usr_id, Constants.BONUS_CFG_TYP_1);
		bonusCfg.set(Fields.TOL_AMT, bonusCfg.getBigDecimal(Fields.TOL_AMT).subtract(amt));
		bonusCfg.update();
		BigDecimal fee_5 = new BigDecimal("0.05");
		BigDecimal fee_3 = new BigDecimal("0.03");
		BigDecimal fee = fee_5.multiply(amt);// 手续费
		if (amt.compareTo(new BigDecimal("3500")) > 0) {
			BigDecimal fee1 = fee_3.multiply(amt.subtract(new BigDecimal("3500")));
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "07", "税收", fee1, "2", "税收",usr_id);
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "04", "提现手续费", fee, "2", "提现手续费",usr_id);
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "01", "提现",
					amt.subtract(fee).subtract(fee1), "2", "提现",usr_id);
		} else if (amt.compareTo(BigDecimal.ZERO) <= 0) {
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "04", "提现手续费", BigDecimal.ZERO, "2",
					"提现手续费",usr_id);
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "01", "提现", BigDecimal.ZERO, "2",
					"提现",usr_id);
		} else {
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "04", "提现手续费", fee, "2", "提现手续费",usr_id);
			userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "01", "提现", amt.subtract(fee), "2",
					"提现",usr_id);
		}
	}

	/*
	 * private BigDecimal getTarAmt(BigDecimal amt) {
	 * if(amt.compareTo(BigDecimal.ZERO)<=0) { return BigDecimal.ZERO; }
	 * BigDecimal fee_5 = new BigDecimal("0.05"); BigDecimal fee_3 = new
	 * BigDecimal("0.03"); if(amt.compareTo(new BigDecimal("3500"))<=0) {
	 * BigDecimal fee = fee_5.multiply(amt); return fee; }else { BigDecimal fee
	 * = fee_5.multiply(amt); BigDecimal fee1 = fee_3.multiply(amt.subtract(new
	 * BigDecimal("3500"))); return fee.add(fee1); } }
	 */

	public void downloadDepositList() throws IOException {
		try {
			String path = "/home/root/images/product/";
			String fileName = "depositList.xls";
			writeExcel("1");
			File file = new File(path, fileName);
			// 本地的一张图片
			if (file.exists()) { // 如果文件存在
				renderFile(file);
			} else {
				renderFile(new File(path, "error.xls"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadAuditDepositList() throws IOException {
		try {
			String path = "/home/root/images/product/";
			String fileName = "depositAuditList.xls";
			writeExcel("2");
			File file = new File(path, fileName);
			// 本地的一张图片
			if (file.exists()) { // 如果文件存在
				renderFile(file);
			} else {
				renderFile(new File(path, "error.xls"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeExcel(String state) {
		int page = 0;
		int size = 200;
		String path = "/home/root/images/product/";
		String fileName = "depositList.xls";
		if(!"1".equals(state)){
			fileName = "depositAuditList.xls";
		}
		Excel excel = new Excel(path, fileName);
		excel.createSheet(Constants.SHEET1);
		createExcelTitle(excel); 
		while(true) {
		  List<Record> depositList = depositService.findDepositList(null, state, null, page, size);
		  if(depositList==null||depositList.isEmpty()) {break;}
		  int begin = page*size;
		  int end = begin+depositList.size();
		  int row = begin+1;
		  for (int i = begin; i < end; i++) {
				excel.createRow(Constants.SHEET1, row);
			    createExcelItem(excel,row,depositList.get(i-begin)); 
			    row++;
			}
		  page++;
		}
		excel.writeExcel();
	}
	
	private void createExcelTitle(Excel excel) {
		excel.createRow(Constants.SHEET1, 0);
		excel.createCell(Constants.SHEET1, 0, 0, "序号");
		excel.createCell(Constants.SHEET1, 0, 1, "用户名");
		excel.createCell(Constants.SHEET1, 0, 2, "手机号码");
		excel.createCell(Constants.SHEET1, 0, 3, "提现金额(元)");
		excel.createCell(Constants.SHEET1, 0, 4, "手续费&个税(元)");
		excel.createCell(Constants.SHEET1, 0, 5, "实际提现金额(元)");
		excel.createCell(Constants.SHEET1, 0, 6, "提现类型");
		excel.createCell(Constants.SHEET1, 0, 7, "提现账号");
		excel.createCell(Constants.SHEET1, 0, 8, "账号用户名");
		excel.createCell(Constants.SHEET1, 0, 9, "银行类型");
		excel.createCell(Constants.SHEET1, 0, 10, "开户分行");
		excel.createCell(Constants.SHEET1, 0, 11, "申请时间");
	}
	
	private void createExcelItem(Excel excel,int row,Record record) {
		Double actMoney = record.getDouble("amt")-record.getDouble("tar_amt");
		excel.createCell(Constants.SHEET1, row, 0, row+"");
		excel.createCell(Constants.SHEET1, row, 1, record.getStr("name"));
		excel.createCell(Constants.SHEET1, row, 2, record.getStr("pho_no"));
		excel.createCell(Constants.SHEET1, row, 3, record.get("amt")+"");
		excel.createCell(Constants.SHEET1, row, 4, record.get("tar_amt")+"");
		excel.createCell(Constants.SHEET1, row, 5, actMoney+"");
		excel.createCell(Constants.SHEET1, row, 6, record.getStr("cn_dep_typ"));
		excel.createCell(Constants.SHEET1, row, 7, record.getStr("acunt"));
		excel.createCell(Constants.SHEET1, row, 8, record.getStr("usr_nme")+"");
		excel.createCell(Constants.SHEET1, row, 9, record.getStr("bak_typ"));
		excel.createCell(Constants.SHEET1, row, 10, record.getStr("bak_nme"));
		excel.createCell(Constants.SHEET1, row, 11, record.get("crt_tme")+"");
	}
}
