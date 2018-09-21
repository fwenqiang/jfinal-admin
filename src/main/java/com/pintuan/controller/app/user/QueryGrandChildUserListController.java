package com.pintuan.controller.app.user;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Bonus;
import com.pintuan.model.Order;
import com.pintuan.service.BonusService;
import com.pintuan.service.OrderService;
import com.pintuan.service.SchedulingService;
import com.pintuan.service.UserService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询徒孙列表
 * 
 * @author zjh 2018-5-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryGrandChildUserList")
@Before(CheckUserKeyInterceptor.class)
public class QueryGrandChildUserListController extends BaseProjectController {
	private OrderService orderService = new OrderService();
	private UserService userService = new UserService();
	private BonusService bonusService = new BonusService();
	private SchedulingService schedulingService = new SchedulingService();

	public void index() throws CoreException {
		String usr_ide_key = getData(Fields.USER_IDENTIFY_KEY);
		List<Record> userList = userService.findGrandChildUser(usr_ide_key);

		List<Map<String, Object>> list = DBModelUtils.toMaps(userList, Record.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				String usr_id = map.get(Fields.USER_ID).toString();
				String amt_typ = "1990";
				Order order = orderService.findByUserId(usr_id, amt_typ);
				int sum1990 = 0;
				if (order != null) {
					List<Bonus> bonusList = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
        			sum1990 = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
        			sum1990 = this.build(bonusList, sum1990);
				}

				map.put("sum1990", sum1990);

			}
		}

		setResp(Fields.USR_LIST, list);
		returnJson();
	}

	private int build(List<Bonus> bonusList,int sum){
		int result = 0;
		for(int i=0;i<7;i++) {  //已摘
			if(bonusList!=null&&bonusList.size()>i) {
				
			}else if(sum>i){  //可摘
				result ++;
			}else {  //不可摘
				
			}
		}
		return result;
	}
}
