package com.pintuan.controller.app.rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询全区等级排行
 * 
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/queryRank")
@Before(CheckUserKeyInterceptor.class)
public class QueryRankController extends BaseProjectController {

	private UserService userService = new UserService();
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		int page = getNPage();
		int size = getNSize();
		Assert.isTrue(size <= 20, ErrCode.SIZE_OVERFLOW);
		List<Record> rankList = userService.findRank(page, size);
		setResp(Fields.RANK_LIST, build(rankList));

		returnJson();
	}

	private List<Map<String, Object>> build(List<Record> rankList) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (rankList != null) {
			for (Record rank : rankList) {
				long sum = bonusService.findBonusSum(rank.getStr(Fields.USER_ID));
				long selfSum = bonusService.findSelfBonusSum(rank.getStr(Fields.USER_ID));
				long childSum = bonusService.findChildBonusSum(rank.getStr(Fields.USER_ID));
				long grandChildSum = sum - selfSum - childSum;
				result.add(buildItem(rank, sum, selfSum, childSum, grandChildSum));
			}
		}
		return result;
	}

	private Map<String, Object> buildItem(Record rank, long sum, long selfSum, long childSum, long grandChildSum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(rank.getColumns());
		map.put("sum", sum);
		map.put("selfSum", selfSum);
		map.put("childSum", childSum);
		map.put("grandChildSum", grandChildSum);
		return map;
	}

}
