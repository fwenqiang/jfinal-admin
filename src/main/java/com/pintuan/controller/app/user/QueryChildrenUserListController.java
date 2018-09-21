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
import com.pintuan.util.SchUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询徒弟列表
 * 
 * @author zjh 2018-5-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryChildrenUserList")
@Before(CheckUserKeyInterceptor.class)    
public class QueryChildrenUserListController extends BaseProjectController {
	
	private OrderService orderService = new OrderService();
	private UserService userService = new UserService();
	private SchedulingService schedulingService = new SchedulingService();
	private BonusService bonusService = new BonusService();
	
	public void index() throws CoreException {
		String usr_ide_key = getData(Fields.USER_IDENTIFY_KEY);
        List<Record> userList = userService.findChildrenUser(usr_ide_key);
        
        List<Map<String,Object>> list = DBModelUtils.toMaps(userList,Record.class);
        if(CollectionUtils.isNotEmpty(list)){
        	for(Map<String,Object> map : list){
        		String usr_id = map.get(Fields.USER_ID).toString();
        		String amt_typ = "1990";
        		Order order = orderService.findByUserId(usr_id,amt_typ);
        		int sum1990 = 0;
        		if(order!=null){
        			List<Bonus> bonusList = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
        			sum1990 = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
        			sum1990 = this.build(bonusList, sum1990);
        		}
        		
        		amt_typ = "800";
        		order = orderService.findByUserId(usr_id,amt_typ);
        		int sum800 = 0;
        		if(order!=null){
        			List<Bonus> bonusList = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
        			int schNum = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
        			int chdNum = (int)userService.findChildrenUserCount(usr_id);
        	    	int sum = SchUtil.get800Bonus(chdNum,schNum);
        			sum800 = this.build(bonusList, sum);
        		}
        		
        		map.put("sum1990", sum1990);
        		map.put("sum800", sum800);
        	}
        }
        setResp(Fields.USR_LIST, list);
        returnJson();
	}	
	
	private List<Record> check(List<Record> userList){
		if(userList==null) return null;
		if(userList.size()>4) {
			
		}
		return null;
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
