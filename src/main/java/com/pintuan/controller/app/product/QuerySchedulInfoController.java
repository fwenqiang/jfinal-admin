package com.pintuan.controller.app.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.Product;
import com.pintuan.model.ProductCfg;
import com.pintuan.model.Scheduling;
import com.pintuan.service.ProductService;
import com.pintuan.service.SchedulingService;
import com.pintuan.util.DBModelUtils;
import com.pintuan.util.SchUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询排单列表
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/querySchedulInfo")
public class QuerySchedulInfoController extends BaseProjectController {
	private SchedulingService schedulingService = new SchedulingService();
	private ProductService productService = new ProductService();

	public void index() throws CoreException {
		String amtTyp = isNotNullAndGet(Fields.AMT_TYP, ErrCode.AMT_TYP_IS_NULL).toString();
		String sdlSqnTyp = isNotNullAndGet(Fields.SDL_SQN_TYP, ErrCode.SDL_SQN_TYP_IS_NULL).toString();
		if (Constants.SDL_SQN_TYP_LAST.equals(sdlSqnTyp)) {
			queryLast(amtTyp);
		} else if (Constants.SDL_SQN_TYP_NOW.equals(sdlSqnTyp)) {
			queryThis(amtTyp);
		} else if (Constants.SDL_SQN_TYP_NEXT.equals(sdlSqnTyp)) {
			queryNext(amtTyp);
		}
		returnJson();
	}

	// 上次排单
	private void queryLast(String amtTyp) {
		Scheduling scheduling = schedulingService.find(amtTyp, Constants.SDL_STATE_OVER);
		if (scheduling == null) {
			Scheduling nowSdl = schedulingService.find(amtTyp, Constants.SDL_STATE_DOING);
			if (nowSdl == null) {
				nowSdl = initScheduling(amtTyp);
			}
			List<ProductCfg> productCfgList = schedulingService.findProCfgBySdlId(nowSdl.get(Fields.SDL_ID) + "");

			setResp(Fields.BUY_CNT_SUM, 0);
			setResp(Fields.TAR_NUM, 0);
			setResp(Fields.END_TME, new Date());
			setResp(Fields.CREATE_TIME, new Date());
			setResp(Fields.USE_TME, submitTime(new Date(), new Date()));
			setResp(Fields.STATE, "3");
			setResp(Fields.SDL_PRODUCT_LIST, DBModelUtils.toMaps(productCfgList));
		} else {
			List<ProductCfg> productCfgList = schedulingService.findProCfgBySdlId(scheduling.get(Fields.SDL_ID) + "");

			setRespMap(scheduling.getAttrs());
			Date end_time = scheduling.getDate(Fields.END_TME);
			setResp(Fields.END_TME, end_time);
			setResp(Fields.USE_TME, submitTime(end_time, scheduling.getDate(Fields.CREATE_TIME)));
			setResp(Fields.SDL_PRODUCT_LIST, DBModelUtils.toMaps(productCfgList));
		}

	}

	// 本次排单
	private void queryThis(String amtTyp) {
		Scheduling scheduling = schedulingService.find(amtTyp, Constants.SDL_STATE_DOING);
		if (scheduling == null) {
			scheduling = initScheduling(amtTyp);
		}
		List<ProductCfg> productCfgList = schedulingService.findProCfgBySdlId(scheduling.get(Fields.SDL_ID) + "");

		setRespMap(scheduling.getAttrs());
		Date end_time = new Date();
		setResp(Fields.END_TME, end_time);
		setResp(Fields.USE_TME, submitTime(end_time, scheduling.getDate(Fields.CREATE_TIME)));
		setResp(Fields.SDL_PRODUCT_LIST, DBModelUtils.toMaps(productCfgList));

	}

	// 下次排单
	private void queryNext(String amtTyp) {
		Scheduling scheduling = schedulingService.find(amtTyp, Constants.SDL_STATE_DOING);
		if (scheduling == null) {
			scheduling = initScheduling(amtTyp);
		}
		List<ProductCfg> productCfgList = schedulingService.findProCfgBySdlId(scheduling.get(Fields.SDL_ID) + "");
		Scheduling next = nextSchduling(scheduling);
		setRespMap(next.getAttrs());
		for (int i = 0; i < productCfgList.size(); i++) {
			productCfgList.get(i).set(Fields.BUY_CNT, 0);
		}
		setResp(Fields.SDL_PRODUCT_LIST, DBModelUtils.toMaps(productCfgList));

	}

	private String submitTime(Date d1, Date d2) {
		try {
			long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);

			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
			return days + "天" + hours + "小时" + minutes + "分";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
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

	private Scheduling nextSchduling(Scheduling now) {
		int tur_num = SchUtil.getNextTurNum(now.getInt(Fields.TUR_NUM), now.getInt(Fields.GRID));
		int grid = SchUtil.getNextGrid(now.getInt(Fields.GRID));
		BigDecimal amt = SchUtil.getNextAmt(now.getStr(Fields.AMT_TYP));
		int tar_num = SchUtil.getTarNum(tur_num, grid);
		Scheduling scheduling = new Scheduling();
		scheduling.set(Fields.GRID, grid);
		scheduling.set(Fields.TUR_NUM, tur_num);
		scheduling.set(Fields.BUY_CNT_SUM, 0);
		scheduling.set(Fields.AMT, amt);
		scheduling.set(Fields.AMT_TYP, now.getStr(Fields.AMT_TYP));
		scheduling.set(Fields.TAR_NUM, tar_num);
		scheduling.set(Fields.STATE, Constants.SDL_STATE_UNBEGIN);
		return scheduling;
	}

}
