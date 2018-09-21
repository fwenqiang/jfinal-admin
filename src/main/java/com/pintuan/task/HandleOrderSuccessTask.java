package com.pintuan.task;

import java.math.BigDecimal;
import java.util.List;

import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.RedPacket;
import com.pintuan.model.Scheduling;
import com.pintuan.model.User;
import com.pintuan.model.UserGrid;
import com.pintuan.service.OrderService;
import com.pintuan.service.ProductService;
import com.pintuan.service.RedPacketService;
import com.pintuan.service.SchedulingService;
import com.pintuan.service.UserService;
import com.pintuan.util.SchUtil;
import com.supyuan.util.StrUtils;
import com.supyuan.util.task.Task;

/**
 * 
 * 处理下单成功任务
 * 
 * @author Administrator
 *
 */
public class HandleOrderSuccessTask extends Task {
	private String pro_id;
	private String ord_id;
	private String usr_id;
	private ProductService productService = new ProductService();
	private SchedulingService schedulingService = new SchedulingService();
	private OrderService orderService = new OrderService();
	private UserService userService = new UserService();
	private RedPacketService redPacketService = new RedPacketService();

	public HandleOrderSuccessTask(String usr_id, String pro_id, String ord_id) {
		// setTaskName(taskName);
		this.pro_id = pro_id;
		this.ord_id = ord_id;
		this.usr_id = usr_id;
	}

	@Override
	public void customRun() {

		Product product = productService.findById(pro_id);
		schedulingService.addOne(product.getStr(Fields.AMT_TYP));
		// 商品配置表
		productService.addOneInProCfg(product.getStr(Fields.AMT_TYP), product.getStr(Fields.PRO_TYP));
		Scheduling scheduling = schedulingService.find(product.getStr(Fields.AMT_TYP));
		// BigDecimal amt = scheduling.getBigDecimal(Fields.AMT);
		if (scheduling == null) { // 如果没有则初始化
			scheduling = initScheduling(product.getStr(Fields.AMT_TYP));
			schedulingService.addOne(product.getStr(Fields.AMT_TYP));
			productService.addOneInProCfg(product.getStr(Fields.AMT_TYP), product.getStr(Fields.PRO_TYP));
		}
		dealSchdual(product.getStr(Fields.AMT_TYP), scheduling);
		// 商品表
		productService.addOne(pro_id);
		orderService.updateOrderSdlId(ord_id, scheduling.get(Fields.SDL_ID) + "");
		User user = userService.find(this.usr_id);
		//添加168红包
		addRedPacket(user, product.getStr(Fields.AMT_TYP));
		//绑定关系
		handerUserBand(user, product.getStr(Fields.AMT_TYP));
		// 处理父级称谓
		updateUserTitle(user);
	}

	// 处理排单
	private void dealSchdual(String amt_typ, Scheduling scheduling) {
		// Scheduling scheduling = schedulingService.find(amt_typ);
		if (scheduling.getInt(Fields.BUY_CNT_SUM) >= scheduling.getInt(Fields.TAR_NUM)) { // 如果排单已满
			schedulingService.updateState(scheduling.get(Fields.SDL_ID) + "", Constants.SDL_STATE_OVER); // 结束当前排单
			int tur_num = SchUtil.getNextTurNum(scheduling.getInt(Fields.TUR_NUM), scheduling.getInt(Fields.GRID));
			int grid = SchUtil.getNextGrid(scheduling.getInt(Fields.GRID));
			BigDecimal amt = SchUtil.getNextAmt(amt_typ);
			int tar_num = SchUtil.getTarNum(tur_num, grid);
			Scheduling nowSdl = schedulingService.add(grid, tur_num, amt, amt_typ, tar_num); // 创建新排单

			productService.updateProCfgState(scheduling.get(Fields.SDL_ID) + "", Constants.SDL_STATE_OVER); // 结束当前商品配置表的项目

			// 插入三类产品
			Product product1 = productService.find(amt_typ, "新旅游");
			productService.addProductCfg(nowSdl, product1);
			Product product2 = productService.find(amt_typ, "新健康");
			productService.addProductCfg(nowSdl, product2);
			Product product3 = productService.find(amt_typ, "新教育");
			productService.addProductCfg(nowSdl, product3);
		}
	}

	/** 初始化本次排单 **/
	private Scheduling initScheduling(String amt_typ) {
		Scheduling scheduling = schedulingService.find(amt_typ, "2");
		BigDecimal amt = SchUtil.getNextAmt(amt_typ);
		Scheduling nowSdl = null;
		if (scheduling == null) { // 第一次
			nowSdl = schedulingService.add(1, 1, amt, amt_typ, SchUtil.getTarNum(1, 1));
		} else {
			int tur_num = SchUtil.getNextTurNum(scheduling.getInt(Fields.TUR_NUM), scheduling.getInt(Fields.GRID));
			int grid = SchUtil.getNextGrid(scheduling.getInt(Fields.GRID));
			nowSdl = schedulingService.add(tur_num, grid, amt, amt_typ, SchUtil.getTarNum(tur_num, grid));
		}
		// 插入三类产品
		Product product1 = productService.find(amt_typ, "新旅游");
		productService.addProductCfg(nowSdl, product1);
		Product product2 = productService.find(amt_typ, "新健康");
		productService.addProductCfg(nowSdl, product2);
		Product product3 = productService.find(amt_typ, "新教育");
		productService.addProductCfg(nowSdl, product3);
		return nowSdl;
	}

	// 当用户购买成功后，形成绑定关系：1父级只能有4个；2购买1990产品
	private void handerUserBand(User user, String amt_typ) {
		if (user == null) {
			return;
		}
		if (!"1990".equals(amt_typ)) {
			return;
		}
		long count = userService.findChildrenUserCount(user.getStr(Fields.P_ID));
		System.out.println("******count=" + count);
		if (count > 4) { // 包括他自己
			userService.updatePidAndBuyTime(usr_id);
		} else {
			userService.updateBuyTime(usr_id);
		}
	}

	// 更新父级用户称谓
	private void updateUserTitle(User user) {
		if (user == null)
			return;
		long chdNum = userService.findChildrenUserCount(user.getStr(Fields.P_ID));
		List<Order> order = orderService.findByUserId(user.getStr(Fields.P_ID));
		if (order == null || order.isEmpty())
			return;
		String usr_tit = SchUtil.getUserTitle((int) chdNum);
		UserGrid userGrid = userService.findUserGrid(user.getStr(Fields.P_ID));
		if (userGrid == null) {
			userGrid = userService.addUserGrid(user.getStr(Fields.P_ID));
		}
		userGrid.set(Fields.USR_TIT, usr_tit);
		userGrid.update();
	}

	// 添加红包
	private void addRedPacket(User user, String amt_typ) {
		if (user == null) {
			return;
		}
		if (!"1990".equals(amt_typ)) {
			return;
		}
		if (StrUtils.isEmpty(user.getStr(Fields.P_ID)))
			return;
		List<RedPacket> redPakets = redPacketService.findRedPacketList(user.getStr(Fields.P_ID));
		if(redPakets!=null&&redPakets.size()>=4) {
			return ;
		}
		redPacketService.add(user.getStr(Fields.P_ID), user.getStr(Fields.USER_ID), user.getStr(Fields.USER_NAME),user.getStr(Fields.TIT_URL),
				new BigDecimal("168"));
	}

}
