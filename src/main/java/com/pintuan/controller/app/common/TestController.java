package com.pintuan.controller.app.common;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.Scheduling;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.JnlService;
import com.pintuan.service.OrderService;
import com.pintuan.service.ProductService;
import com.pintuan.service.SchedulingService;
import com.pintuan.service.SpecialService;
import com.pintuan.task.HandleOrderSuccessTask;
import com.pintuan.task.TestSyncTask;
import com.pintuan.task.TestTask;
import com.pintuan.util.Excel;
import com.pintuan.util.SchUtil;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.DateUtils;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;
import com.supyuan.util.task.AsyncTaskExcutor;
import com.supyuan.util.task.SyncTaskExcutor;

/**
 * test
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/test")
public class TestController extends BaseProjectController {
	private static final String path = "/pages/pintuan/weixin_";
	private BonusService bonusService = new BonusService();
	private SpecialService specialService = new SpecialService();
	private ProductService productService = new ProductService();
	private SchedulingService schedulingService = new SchedulingService();
	private OrderService orderService = new OrderService();
	private JnlService jnlService = new JnlService();
	
	//启动定时作业
	private void index() throws CoreException {
		AsyncTaskExcutor.exc(new TestTask("task1",99),0,2000);
		// s.cancel(true);
		System.out.println("********111");
        returnJson();
	}	
	
	//启动定时作业
	private void cancel() throws CoreException {
		ScheduledFuture s = AsyncTaskExcutor.getScheduledFuture("task1");
		System.out.println("*******s="+(s==null));
		if(s!=null) s.cancel(true);
		
        returnJson();
	}
	
	public void result() throws CoreException {
	//重定向
    render(path + "login_result.html");
	}
	
	public void downfile() {//文件下载

        File file = new File("G:\\tools\\PuTTY_0.67.0.0.exe");
     //本地的一张图片
        if (file.exists()) { //如果文件存在
            renderFile(file);
        } else {
            renderJson();
        }
    }
	
	public void test1() {

		bonusService.updatePaket(new BigDecimal("0.5"), "cd0b3a1a856641b2b35765938ed452c6", Constants.BONUS_CFG_TYP_2);
    }
	
	public void test2() {
		String amt_typ = "1990";
		Scheduling scheduling = schedulingService.find(amt_typ);
		if(scheduling==null) {
			scheduling = initScheduling(amt_typ); 
		}else {
		schedulingService.updateState(scheduling.get(Fields.SDL_ID)+"",Constants.SDL_STATE_OVER);  //结束当前排单
		int tur_num = SchUtil.getNextTurNum(scheduling.getInt(Fields.TUR_NUM), scheduling.getInt(Fields.GRID));
		int grid =SchUtil.getNextGrid(scheduling.getInt(Fields.GRID)); 
		BigDecimal amt = SchUtil.getNextAmt(amt_typ);
		int tar_num = SchUtil.getTarNum(tur_num, grid);        
		Scheduling nowSdl = schedulingService.add(grid, tur_num, amt, amt_typ, tar_num);  //创建新排单
		
		productService.updateProCfgState(scheduling.get(Fields.SDL_ID)+"",Constants.SDL_STATE_OVER);  //结束当前商品配置表的项目
		
		//插入三类产品
		Product product1 = productService.find(amt_typ, "沉香");
		productService.addProductCfg(nowSdl,product1);
		Product product2 = productService.find(amt_typ, "健康");
		productService.addProductCfg(nowSdl,product2);
		Product product3 = productService.find(amt_typ, "教育");
		productService.addProductCfg(nowSdl,product3);
		}
    }
	
	/**初始化本次排单**/
	private Scheduling initScheduling(String amt_typ) {
		Scheduling scheduling = schedulingService.find(amt_typ,"2");
		BigDecimal amt =SchUtil.getNextAmt(amt_typ);
		Scheduling nowSdl = null;
		if(scheduling==null) { //第一次
			nowSdl =schedulingService.add(1,1,amt,amt_typ,SchUtil.getTarNum(1, 1));
		}else {
			int tur_num = SchUtil.getNextTurNum(scheduling.getInt(Fields.TUR_NUM), scheduling.getInt(Fields.GRID));
			int grid =SchUtil.getNextGrid(scheduling.getInt(Fields.GRID)); 
			nowSdl= schedulingService.add(tur_num,grid,amt,amt_typ,SchUtil.getTarNum(tur_num, grid));
		}
		//插入三类产品
				Product product1 = productService.find(amt_typ, "沉香");
				productService.addProductCfg(nowSdl,product1);
				Product product2 = productService.find(amt_typ, "健康");
				productService.addProductCfg(nowSdl,product2);
				Product product3 = productService.find(amt_typ, "教育");
				productService.addProductCfg(nowSdl,product3);
		return nowSdl;
	}
	
	/**导入用户**/
	public void importUser() {
		String filePath = "/home/root/db/";//"D:\\temp\\";//
	  	  String fileName=  "test.xls";
	  	  List<List<String >> result = new ArrayList<List<String>>();
	  	  //for(int i=1;i<5;i++) {
	  		  List<List<String>> userList = Excel.readExcel(filePath, fileName,1);
	  		  int count = 0;
			  for(List<String> user:userList) {
				if(count==0) {
					count++;
					continue;
				}
				User model = new User();
				String usr_id = WXRequestUtil.MD5(user.get(5));
				if(StrUtils.isNotEmpty(user.get(3))) {
				   String p_id = WXRequestUtil.MD5(user.get(3));
				   model.set(Fields.P_ID, p_id);
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
				try {
				model.save();
				 // 添加钱包和基金
		        BonusCfg  myPaket = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_1);
		        BonusCfg  bossFund = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_2);
		        BonusCfg  helpFund = bonusService.addMyPaket(usr_id, Constants.BONUS_CFG_TYP_3);
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("error="+user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
					result.add(user);
				}
			  }
	  	  //}
	  	  setResp("user", result);
	  	  returnJson();
	}
	
	/**导入订单**/
	public void importOrder() {
		String filePath = "/home/root/db/";//"D:\\temp\\";//
	  	  String fileName=  "test.xls";
	  	  List<List<String >> result = new ArrayList<List<String>>();
	  	 //for(int i=1;i<5;i++) {
	  		  List<List<String>> userList = Excel.readExcel(filePath, fileName,1);
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
				// if(count>=30) break;
			  }
	  	  //}
	  	  setResp("user", result);
	  	  returnJson();
	}
	
	/**导入订单**/
	public void importOrder2() {
		String filePath = "/home/root/db/";
	  	  String fileName=  "test.xls";
	  	  List<List<String >> result = new ArrayList<List<String>>();
	  	 //for(int i=1;i<5;i++) {
	  		  List<List<String>> userList = Excel.readExcel(filePath, fileName,1);
	  		  int count = 0;
			  for(List<String> user:userList) {
				if(count==0) {
					count++;
					continue;
				}
				if(StrUtils.isEmpty(user.get(9))) continue;
				try {
				String usr_id = WXRequestUtil.MD5(user.get(5));
				User u = new User();
				u.set(Fields.USER_ID, usr_id);
				u.set(Fields.USER_NAME, user.get(4));
                Product product = Product.dao.findFirstByWhere("where amt_typ=? and state='1' order by buy_cnt_sum ", "800");
                Order order = orderService.add(usr_id, product);
                Jnl jnl = jnlService.initWXJnl( order.getStr(Fields.ORD_ID), new BigDecimal("800"), "10", "1", u);
                orderService.updateOrderState(order.getStr(Fields.ORD_ID), "E");
				jnlService.updateJnlState(jnl, "E");
				AsyncTaskExcutor.exc(new HandleOrderSuccessTask(usr_id,order.getStr(Fields.PRO_ID),order.getStr(Fields.ORD_ID)));
				
				
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("error="+user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
					result.add(user);
				}
				count++;
				// if(count>=30) break;
			  }
	  	  //}
	  	  setResp("user", result);
	  	  returnJson();
	}
	
	public void test11() {
		SyncTaskExcutor.exc(new TestSyncTask(0));
		SyncTaskExcutor.exc(new TestSyncTask(1));
		SyncTaskExcutor.exc(new TestSyncTask(2));
	}
	
	public void test12() {
		AsyncTaskExcutor.exc(new TestSyncTask(0));
		AsyncTaskExcutor.exc(new TestSyncTask(1));
		AsyncTaskExcutor.exc(new TestSyncTask(2));
	}
	
	
	public static void main(String[] args){
  	  /*String filePath = "D:\\temp\\";
  	  String fileName=  "20180519.xls";
  	  int count = 0 ;
  	  // for(int i=1;i<5;i++) {
  		  List<List<String>> userList = Excel.readExcel(filePath, fileName,1);
		  for(List<String> user:userList) {
			System.out.println(user.get(3)+"|"+user.get(4)+"|"+user.get(5)+"|"+user.get(6));
			String usr_id = WXRequestUtil.MD5(user.get(5));
			String p_id = WXRequestUtil.MD5(user.get(3));
			System.out.println(usr_id+"   -  "+p_id);
		  //}
			count++;
			if(count>40) break;
  	  }*/
		
		System.out.println(new Md5Utils().getMD5(Base64.decodeAsString("RTEwQURDMzk0OUJBNTlBQkJFNTZFMDU3RjIwRjg4M0U=")));
    }
	
	
	

	
}
