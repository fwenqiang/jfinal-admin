package com.pintuan.controller.console.business;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
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
import com.supyuan.util.task.AsyncTaskExcutor;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * 订单
 * 
 * 未测试
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/console/order")
public class OrderController extends BaseProjectController {

	private OrderService orderService = new OrderService();
	private JnlService jnlService = new JnlService();
	private BonusService bonusService = new BonusService();
	private UserService userService = new UserService();

	public void index() throws CoreException {
		queryOrderList();
	}

	// 查询订单列表
	public void queryOrderList() throws CoreException {
		String ord_id = getData(Fields.ORD_ID);
		String usr_nme = getData(Fields.USER_NAME);
		String pro_nme = getData(Fields.PRO_NME);
		int page = getNPage();
		int size = getNSize();
		List<Order> orderList = orderService.find(ord_id, usr_nme, pro_nme, page, size);
		long total = orderService.findSize(ord_id, usr_nme, pro_nme);
		setResp(Fields.ORDER_LIST, DBModelUtils.toMaps(orderList));
		setResp(Fields.TOTAL, total);
		returnJson();
	}

	public void deleteOrder() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		Order order = orderService.findOne(ord_id);
		Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
		orderService.deleteOrder(ord_id);
		jnlService.deleteJnlByOrdId(ord_id);
		returnJson();
	}

	// 更新订单状态
	public void updateOrderState() throws CoreException {
		String ord_id = isNotNullAndGet(Fields.ORD_ID, ErrCode.ORD_ID_IS_NULL).toString();
		String state = isNotNullAndGet(Fields.STATE, ErrCode.PAMAS_ERROR).toString();
		Order order = orderService.findOne(ord_id);
		Assert.notEmpty(order, ErrCode.ORDER_NOT_EXIST);
		order.set(Fields.STATE, state);
		order.update();
		returnJson();
	}

	public void deleteAllUser() throws CoreException {
		String sql = "delete from pt_user where usr_id!='c3f3bd17a8f649e29a583c695fa6c4fc'";
		Db.update(sql);
		returnJson();
	}

	public void deleteAllOrder() throws CoreException {
		String sql = "delete  FROM sys_log";
		Db.update(sql);
		sql = "delete from pt_address";
		Db.update(sql);
		sql = "delete from pt_amt_record";
		Db.update(sql);
		sql = "delete from pt_bank_card";
		Db.update(sql);
		sql = "delete from pt_invoice";
		Db.update(sql);
		sql = "delete from pt_deposit";
		Db.update(sql);
		sql = "delete from pt_red_packet";
		Db.update(sql);
		sql = "delete from pt_user_pay_info";
		Db.update(sql);
		 sql = "delete from pt_jnl";
		Db.update(sql);
		 sql = "delete from pt_order";
		Db.update(sql);
		 sql = "delete from pt_product_cfg";
		Db.update(sql);
		 sql = "delete from pt_scheduling";
		Db.update(sql);
		sql = "delete from pt_identify_code";
		Db.update(sql);
		sql = "delete from pt_user_grid";
		Db.update(sql);
		sql = "delete from pt_bonus_cfg where usr_id!='c3f3bd17a8f649e29a583c695fa6c4fc'";
		Db.update(sql);
		sql = "delete from pt_bonus";
		Db.update(sql);
		sql = "delete from pt_red_packet";
		Db.update(sql);
		sql = "update `pt_product` set buy_cnt_sum=0";
		Db.update(sql);
		
		returnJson();
	}
	
	public void importUser() throws CoreException {
		String fileName = getData("fileName");
		Assert.notEmpty(fileName, ErrCode.DONT_UPLOAD_FILE);
		List<List<String >> result = new ArrayList<List<String>>();
	  		  List<List<String>> userList = Excel.readExcel(fileName,1);
	  		  int count = 0;
			  for(List<String> user:userList) {
				if(count==0) {
					count++;
					continue;
				}
				User model = new User();
				String usr_id = WXRequestUtil.MD5(user.get(5));
				if(StrUtils.isNotEmpty(user.get(3))) {
				   User parent = userService.findByThdId(user.get(3));
				   if(parent==null) {
					   String p_id = WXRequestUtil.MD5(user.get(3));
					   model.set(Fields.P_ID, p_id);
				   }else {
				   model.set(Fields.P_ID, parent.get(Fields.USER_ID));
				   }
			    }
				model.set(Fields.USER_ID, usr_id);
				model.set(Fields.PHONE_NO, user.get(6));
				model.set(Fields.USER_NAME, user.get(4));
				model.set(Fields.STATE, Constants.ACCESS_STATE);
				model.set(Fields.USER_TYPE, Constants.USER_TYPE_COMMON);
				model.set(Fields.CREATE_TIME, new Date());
				model.set(Fields.POI_SCR, 0);
				model.set(Fields.THD_ID, user.get(5));
				model.set(Fields.PASSWORD, new Md5Utils().getMD5(Base64.decodeAsString("RTEwQURDMzk0OUJBNTlBQkJFNTZFMDU3RjIwRjg4M0U=")));
				model.set(Fields.TIT_URL, Constants.DEFAULT_USER_TITLE_URL);
				try {
				Assert.isEmpty(userService.findByThdId(user.get(5)), ErrCode.THD_ID_IS_EXIST);
				model.save();
				 // 添加钱包和基金
		        BonusCfg  myPaket = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_1);
		        BonusCfg  bossFund = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_2);
		        BonusCfg  helpFund = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_3);
		        if(StrUtils.isNotEmpty(user.get(7))) {
		        	Product product = Product.dao.findFirstByWhere("where amt_typ=? and state='1' order by buy_cnt_sum ", "1990");
		               Order order = orderService.add(usr_id, product);
		               Jnl jnl = jnlService.initWXJnl( order.getStr(Fields.ORD_ID), new BigDecimal("1990"), "10", "1", model);
		               orderService.updateOrderState(order.getStr(Fields.ORD_ID), "E");
						jnlService.updateJnlState(jnl, "E");
					SyncTaskExcutor.exc(new HandleOrderSuccessTask(usr_id,order.getStr(Fields.PRO_ID),order.getStr(Fields.ORD_ID)));
					if(StrUtils.isNotEmpty(user.get(9))) {
						Product product1 = Product.dao.findFirstByWhere("where amt_typ=? and state='1' order by buy_cnt_sum ", "800");
			               Order order1 = orderService.add(usr_id, product1);
			               Jnl jnl1 = jnlService.initWXJnl( order1.getStr(Fields.ORD_ID), new BigDecimal("800"), "10", "1", model);
			               orderService.updateOrderState(order1.getStr(Fields.ORD_ID), "E");
							jnlService.updateJnlState(jnl1, "E");
						SyncTaskExcutor.exc(new HandleOrderSuccessTask(usr_id,order1.getStr(Fields.PRO_ID),order1.getStr(Fields.ORD_ID)));
					}
						
		        }
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("error="+user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
					result.add(user);
				}
			  }
	  	  setResp("user", result);
	  	  returnJson();
	}

	public void importOrder() throws CoreException {
		String fileName = getData("fileName");
		Assert.notEmpty(fileName, ErrCode.DONT_UPLOAD_FILE);
		List<List<String >> result = new ArrayList<List<String>>();
	  		  List<List<String>> userList = Excel.readExcel(fileName,1);
	  		  int count = 0;
			  for(List<String> user:userList) {
				if(count==0) {
					count++;
					continue;
				}
				try {
				String usr_id = WXRequestUtil.MD5(user.get(5));
				User u = new User();
				u.set(Fields.USER_ID, usr_id);
				u.set(Fields.USER_NAME, user.get(4));
               Product product = Product.dao.findFirstByWhere("where amt_typ=? and state='1' order by buy_cnt_sum ", "1990");
               Order order = orderService.add(usr_id, product);
               Jnl jnl = jnlService.initWXJnl( order.getStr(Fields.ORD_ID), new BigDecimal("1990"), "10", "1", u);
               orderService.updateOrderState(order.getStr(Fields.ORD_ID), "E");
				jnlService.updateJnlState(jnl, "E");
					AsyncTaskExcutor.exc(new HandleOrderSuccessTask(usr_id,order.getStr(Fields.PRO_ID),order.getStr(Fields.ORD_ID)));
				
				
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("error="+user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
					result.add(user);
				}
				count++;
			  }
	  	  setResp("order", result);
	  	  returnJson();
	}

	public void downloadOrderList() throws IOException {
		try {
			String path = "/home/root/images/product/";
			String fileName = "orderList.xls";
			writeExcel();
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
	
	private void writeExcel() {
		int page = 0;
		int size = 200;
		String path = "/home/root/images/product/";
		String fileName = "orderList.xls";
		Excel excel = new Excel(path, fileName);
		excel.createSheet(Constants.SHEET1);
		createExcelTitle(excel); 
		while(true) {
		  List<Record> orderList = orderService.findExcelList( page, size);
		  if(orderList==null||orderList.isEmpty()) {break;}
		  int begin = page*size;
		  int end = begin+orderList.size();
		  int row = begin+1;
		  for (int i = begin; i < end; i++) {
				excel.createRow(Constants.SHEET1, row);
			    createExcelItem(excel,row,orderList.get(i-begin)); 
			    row++;
			}
		  page++;
		}
		excel.writeExcel();
	}
	
	private void createExcelTitle(Excel excel) {
		excel.createRow(Constants.SHEET1, 0);
		excel.createCell(Constants.SHEET1, 0, 0, "序号");
		excel.createCell(Constants.SHEET1, 0, 1, "订单号");
		excel.createCell(Constants.SHEET1, 0, 2, "用户名");
		excel.createCell(Constants.SHEET1, 0, 3, "手机号");
		excel.createCell(Constants.SHEET1, 0, 4, "商品名称");
		excel.createCell(Constants.SHEET1, 0, 5, "商品价格(元)");
		excel.createCell(Constants.SHEET1, 0, 6, "购买时间");
		excel.createCell(Constants.SHEET1, 0, 7, "收货地址");
		excel.createCell(Constants.SHEET1, 0, 8, "订单状态");
	}
	
	private void createExcelItem(Excel excel,int row,Record record) {
		excel.createCell(Constants.SHEET1, row, 0, row+"");
		excel.createCell(Constants.SHEET1, row, 1, record.getStr("ord_id"));
		excel.createCell(Constants.SHEET1, row, 2, record.getStr("usr_nme"));
		excel.createCell(Constants.SHEET1, row, 3, record.getStr("pho_no"));
		excel.createCell(Constants.SHEET1, row, 4, record.getStr("pro_nme"));
		excel.createCell(Constants.SHEET1, row, 5, record.get("pro_amt")+"");
		excel.createCell(Constants.SHEET1, row, 6, record.get("crt_tme")+"");
		excel.createCell(Constants.SHEET1, row, 7, record.getStr("rec_adr"));
		excel.createCell(Constants.SHEET1, row, 8, record.getStr("cn_state"));
	}

}
