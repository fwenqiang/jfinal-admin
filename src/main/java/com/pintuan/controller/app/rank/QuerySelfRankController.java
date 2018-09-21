package com.pintuan.controller.app.rank;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.model.UserGrid;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询全区等级排行
 * 
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/querySelfRank")
@Before(CheckUserKeyInterceptor.class)
public class QuerySelfRankController extends BaseProjectController {

	private UserService userService = new UserService();
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		UserGrid userGrid = userService.findUserGrid(user.getStr(Fields.USER_ID));
		if(userGrid==null) {
			userGrid = userService.addUserGrid(user.getStr(Fields.USER_ID));
		}
		long sum = bonusService.findBonusSum(user.getStr(Fields.USER_ID));
		long selfSum = bonusService.findSelfBonusSum(user.getStr(Fields.USER_ID));
		long childSum = bonusService.findChildBonusSum(user.getStr(Fields.USER_ID));
		long grandChildSum = sum - selfSum - childSum;
		Map<String, Object> map = buildItem(buildUser( user,userGrid), sum, selfSum, childSum, grandChildSum);
		setRespMap(map);
		returnJson();
	}

	private Map<String, Object> buildUser(User user,UserGrid userGrid) {
		Map<String, Object> map = user.getAttrs();
		map.putAll(userGrid.getAttrs());
		return map;
	}

	private Map<String, Object> buildItem(Map<String, Object> user, long sum, long selfSum, long childSum, long grandChildSum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(user);
		map.put("sum", sum);
		map.put("selfSum", selfSum);
		map.put("childSum", childSum);
		map.put("grandChildSum", grandChildSum);
		return map;
	}

}
