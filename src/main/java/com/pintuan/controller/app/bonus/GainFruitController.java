package com.pintuan.controller.app.bonus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Bonus;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Order;
import com.pintuan.model.User;
import com.pintuan.model.UserGrid;
import com.pintuan.service.BonusService;
import com.pintuan.service.OrderService;
import com.pintuan.service.SchedulingService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.SchUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 收取果实
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/gainFruit")
@Before(CheckUserKeyInterceptor.class)
public class GainFruitController extends BaseProjectController {
	private BonusService bonusService = new BonusService();
	private UserService userService = new UserService();
	private OrderService orderService = new OrderService();
	private SchedulingService schedulingService = new SchedulingService();

	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY, ErrCode.PAMAS_ERROR).toString();
		String ser_typ = isNotNullAndGet(Fields.SER_TYP, ErrCode.PAMAS_ERROR).toString();
		String tre_typ = isNotNullAndGet(Fields.TRE_TYP, ErrCode.PAMAS_ERROR).toString();
		String usr_id = isNotNullAndGet(Fields.USER_ID, ErrCode.PAMAS_ERROR).toString();
		String sqn_no = isNotNullAndGet(Fields.SQN_NO, ErrCode.PAMAS_ERROR).toString();
		User user = (User) getAttribute(Fields.ATTR_USER_ENTITY);
		
		checkSqn(sqn_no);

		String amt_typ = "1990";
		BigDecimal fee = new BigDecimal("168");
		BigDecimal fee_50 = new BigDecimal("0.5");
		BigDecimal fee_25 = new BigDecimal("0.25");
		BigDecimal fee_05 = new BigDecimal("0.05");
		BigDecimal fee_9 = new BigDecimal("0.9");
		if ("2".equals(tre_typ)) {
			amt_typ = "800";
			//fee = new BigDecimal("1268");
			fee = new BigDecimal("880");
		}
		if ("2".equals(ser_typ)) { // 徒弟
			fee = fee_50.multiply(fee);
		} else if ("3".equals(ser_typ)) {
			fee = fee_25.multiply(fee);
		}

		BonusCfg bonusCfg = bonusService.findBonusCfg(usr_ide_key, Constants.BONUS_CFG_TYP_1);
		if (bonusCfg == null) {
			bonusCfg = bonusService.addMyPaket(usr_ide_key, Constants.BONUS_CFG_TYP_1);
		}
		Bonus fruitBonus = bonusService.findFruitBonus(bonusCfg.getStr(Fields.BON_CFG_ID), usr_ide_key, usr_id, amt_typ,
				sqn_no);
		Assert.isEmpty(fruitBonus, ErrCode.FRUIT_ALREADY_GAIN);
		// 检验是否可以摘取
		checkCondition(usr_ide_key, usr_id, ser_typ, tre_typ, sqn_no);
		Bonus bonus = bonusService.addFruitBonus(bonusCfg.getStr(Fields.BON_CFG_ID), fee, usr_ide_key, usr_id, amt_typ,
				sqn_no);

		bonusService.updatePaket(fee_9.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_1);
		bonusService.updatePaket(fee_05.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_2);
		bonusService.updatePaket(fee_05.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_3);
		addAmtRecord( usr_ide_key, fee_9.multiply(fee));
		updateUserGrid(usr_ide_key, tre_typ, ser_typ);
		
		//给师父加钱
		/*
		if(StringUtils.isNotBlank(user.getStr(Fields.P_ID))){
			User parent = userService.find(user.getStr(Fields.P_ID));
			if(parent!=null){
				bonusCfg = bonusService.findBonusCfg(parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
				if (bonusCfg == null) {
					bonusCfg = bonusService.addMyPaket(parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
				}
				
				bonusService.updatePaket(fee_50.multiply(fee), parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
				addAmtRecord( parent.getStr(Fields.USER_IDENTIFY_KEY), fee_50.multiply(fee));
			}
			
			//给师父的师父加钱
			if(StringUtils.isNotBlank(parent.getStr(Fields.P_ID))){
				parent = userService.find(parent.getStr(Fields.P_ID));
				if(parent!=null){
					bonusCfg = bonusService.findBonusCfg(parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
					if (bonusCfg == null) {
						bonusCfg = bonusService.addMyPaket(parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
					}
					
					bonusService.updatePaket(fee_25.multiply(fee), parent.getStr(Fields.USER_IDENTIFY_KEY), Constants.BONUS_CFG_TYP_1);
					addAmtRecord( parent.getStr(Fields.USER_IDENTIFY_KEY), fee_25.multiply(fee));
				}
			}
		}*/
		
		returnJson();
	}
	
	
	
	private void addAmtRecord(String usr_id,BigDecimal amt) {
		BonusCfg bonusCfg = bonusService.findBonusCfg(usr_id, Constants.BONUS_CFG_TYP_1);
		userService.addAmtRecord(usr_id, bonusCfg.getStr(Fields.BON_CFG_ID), "06", "果实收益", amt, "1", "果实收益",usr_id);
	}

	private int checkSqn(String sqn) {
		int n = Integer.parseInt(sqn);
		switch (n) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		case 4:
			return 4;
		case 5:
			return 5;
		case 6:
			return 6;
		case 7:
			return 7;
		default:
			throw new CoreException(ErrCode.SQN_NO_ERROR);
		}
	}

	private void updateUserGrid(String usr_id, String tre_typ, String ser_typ) {
		UserGrid userGrid = userService.findUserGrid(usr_id);
		if (userGrid == null) {
			userGrid = userService.addUserGrid(usr_id);
		}
		if ("1".equals(ser_typ)) {
			String grid = SchUtil.getNextUserGrid(userGrid.getStr(Fields.USR_GRI), tre_typ);
			userGrid.set(Fields.USR_GRI, grid);
		}

		userGrid.set(Fields.USR_RAN, userGrid.getInt(Fields.USR_RAN) + 1);
		userGrid.set(Fields.UPDATE_TIME, new Date());
		userGrid.update();
	}

	// **************************判断能不能收******************************
	private void checkCondition(String usr_ide_key, String usr_id, String ser_typ, String tre_typ, String sqn_no) {
		Map<Integer, Object> bonusList = getBonusList(usr_ide_key, usr_id, ser_typ, tre_typ);
		if (bonusList.get(Integer.parseInt(sqn_no)) == null) {
			throw new CoreException(ErrCode.FRUIT_CANNOT_BE_GAIN);
		}
	}

	private Map<Integer, Object> getBonusList(String usr_ide_key, String usr_id, String ser_typ, String tre_typ)
			throws CoreException {
		Map<Integer, Object> bonusList = null;
		if ("1".equals(ser_typ)) { // 查自己
			bonusList = findSelfTree(tre_typ, usr_ide_key);
		} else if ("2".equals(ser_typ)) { // 查徒弟
			bonusList = findChildrenTree(tre_typ, usr_ide_key, usr_id);
		} else { // 查徒孙
			bonusList = findGrandChildTree(tre_typ, usr_ide_key, usr_id);
		}
		return bonusList;
	}

	private Map<Integer, Object> findSelfTree(String tre_typ, String usr_id) {
		String amt_typ = "800";
		if ("1".equals(tre_typ)) { // 沉香树
			amt_typ = "1990";
			Order order = orderService.findByUserId(usr_id, amt_typ);
			if (order == null) {
				return build(null, 0);
			}
			List<Bonus> gainBonus = bonusService.queryGainBonus(usr_id, usr_id, amt_typ);
			int sum = (int) schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
			return build(gainBonus, sum);
		} else {// 奇楠树
			Order order = orderService.findByUserId(usr_id, amt_typ);
			if (order == null) {
				return build(null, 0);
			}
			List<Bonus> gainBonus = bonusService.queryGainBonus(usr_id, usr_id, amt_typ);
			// 排单的轮数
			int schNum = (int) schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
			int chdNum = (int) userService.findChildrenUserCount(usr_id);
			int sum = SchUtil.get800Bonus(chdNum, schNum);
			return build(gainBonus, sum);
		}
	}

	private Map<Integer, Object> findChildrenTree(String tre_typ, String usr_ide_key, String usr_id) {
		String amt_typ = "800";// 奇楠树
		if ("1".equals(tre_typ)) { // 沉香树
			amt_typ = "1990";
			Order order = orderService.findByUserId(usr_id, amt_typ);
			if (order == null) {
				return build(null, 0);
			}
			List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id, amt_typ);
			long sum = schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
			return build(gainBonus, (int) sum);
		} else {
			Order order = orderService.findByUserId(usr_id, amt_typ);
			if (order == null) {
				return build(null, 0);
			}
			List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id, amt_typ);
			// 排单的轮数
			int schNum = (int) schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
			int chdNum = (int) userService.findChildrenUserCount(usr_id);
			int sum = SchUtil.get800Bonus(chdNum, schNum);
			return build(gainBonus, sum);
		}
	}

	// 徒孙的只有沉香树
	private Map<Integer, Object> findGrandChildTree(String tre_typ, String usr_ide_key, String usr_id) {
		String amt_typ = "1990";// 奇楠树
		if ("1".equals(tre_typ)) { // 沉香树
			Order order = orderService.findByUserId(usr_id, amt_typ);
			if (order == null) {
				return build(null, 0);
			}
			List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id, amt_typ);
			long sum = schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
			return build(gainBonus, (int) sum);
		} else {
			return build(null, 0);
		}
	}

	/**
	 * 
	 * @param bonusList
	 *            已摘
	 * @param sum
	 *            可摘
	 * @return
	 */
	private Map<Integer, Object> build(List<Bonus> bonusList, int sum) {
		Map<Integer, Object> map = new HashMap<Integer, Object>();
		Map<Integer, Integer> sqn = getSqnMap();
		for (int i = 0; i < 7; i++) { // 已摘
			if (bonusList != null && bonusList.size() > i) {
				sqn.remove(bonusList.get(i).get(Fields.SQN_NO));
			} else if (sum > i) { // 可摘
				int sqn_no = getNextSqn(sqn);
				map.put(sqn_no, sqn_no);
			} else { // 不可摘
				getNextSqn(sqn);
			}
		}
		return map;
	}

	// 初始化一个1-7的map
	private Map<Integer, Integer> getSqnMap() {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < 7; i++) {
			map.put(i + 1, i + 1);
		}
		return map;
	}

	// 获得map里的节点
	private int getNextSqn(Map<Integer, Integer> hm) {
		Set<Integer> keSet = hm.keySet();
		for (Iterator<Integer> iterator = keSet.iterator(); iterator.hasNext();) {
			Integer sqn = iterator.next();
			hm.remove(sqn);
			return sqn;
		}
		return 0;
	}

}
