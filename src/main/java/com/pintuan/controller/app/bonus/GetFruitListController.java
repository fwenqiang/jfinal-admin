package com.pintuan.controller.app.bonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Bonus;
import com.pintuan.model.Order;
import com.pintuan.service.BonusService;
import com.pintuan.service.OrderService;
import com.pintuan.service.SchedulingService;
import com.pintuan.service.UserService;
import com.pintuan.util.SchUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 获得用户果实列表
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/getFruitList")
@Before(CheckUserKeyInterceptor.class)    
public class GetFruitListController extends BaseProjectController {
	private BonusService bonusService = new BonusService();
	private OrderService orderService = new OrderService();
	private SchedulingService schedulingService = new SchedulingService();
	private UserService userService = new UserService();
	
	
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String ser_typ = isNotNullAndGet(Fields.SER_TYP,ErrCode.PAMAS_ERROR).toString();
		String tre_typ = isNotNullAndGet(Fields.TRE_TYP,ErrCode.PAMAS_ERROR).toString();
		String usr_id = getData(Fields.USER_ID);
		
		List<Map<String,Object>> bonusList = null;
		if("1".equals(ser_typ)) {  //查自己
			bonusList =findSelfTree( tre_typ, usr_ide_key);
		}else if("2".equals(ser_typ)){  //查徒弟
			bonusList =findChildrenTree( tre_typ, usr_ide_key, usr_id);
		}else {  //查徒孙
			bonusList =findGrandChildTree( tre_typ, usr_ide_key, usr_id);
		} 
        setResp(Fields.FRUIT_LIST,bonusList);
        returnJson();
	}	
	
	private List<Map<String,Object>> findSelfTree(String tre_typ,String usr_id){
		String amt_typ = "800";
        if("1".equals(tre_typ)) {  //沉香树
        	amt_typ = "1990";
	        Order order = orderService.findByUserId(usr_id,amt_typ);
	    	if(order==null) {	
	    		return build(null,0);
	    	}
	    	List<Bonus> gainBonus = bonusService.queryGainBonus(usr_id, usr_id,amt_typ);
	    	int sum = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
	    	return build(gainBonus,sum);
	    }else {//奇楠树
	    	Order order = orderService.findByUserId(usr_id,amt_typ);
	    	if(order==null) {	
	    		return build(null,0);
	    	}
	    	List<Bonus> gainBonus = bonusService.queryGainBonus(usr_id, usr_id,amt_typ);
	    	//排单的轮数
	    	int schNum = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
	    	int chdNum = (int)userService.findChildrenUserCount(usr_id);
	    	int sum = SchUtil.get800Bonus(chdNum,schNum);
	    	return build(gainBonus,sum);
	    }
	}
	
    private List<Map<String,Object>> findChildrenTree(String tre_typ,String usr_ide_key,String usr_id){
    	String amt_typ = "800";//奇楠树
        if("1".equals(tre_typ)) {  //沉香树
        	amt_typ = "1990";
	        Order order = orderService.findByUserId(usr_id,amt_typ);
	    	if(order==null) {
	    		return build(null,0);
	    	}
	    	List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
	    	long sum = schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
	    	return build(gainBonus,(int)sum);
        }else {
        	Order order = orderService.findByUserId(usr_id,amt_typ);
	    	if(order==null) {	
	    		return build(null,0);
	    	}
	    	List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
	    	//排单的轮数
	    	int schNum = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
	    	int chdNum = (int)userService.findChildrenUserCount(usr_id);
	    	int sum = SchUtil.get800Bonus(chdNum,schNum);
	    	return build(gainBonus,sum);
        }
	}
    //徒孙的只有沉香树
    private List<Map<String,Object>> findGrandChildTree(String tre_typ,String usr_ide_key,String usr_id){
    	String amt_typ = "1990";//奇楠树
        if("1".equals(tre_typ)) {  //沉香树
	        Order order = orderService.findByUserId(usr_id,amt_typ);
	    	if(order==null) {
	    		return build(null,0);
	    	}
	    	List<Bonus> gainBonus = bonusService.queryGainBonus(usr_ide_key, usr_id,amt_typ);
	    	int sum = (int)schedulingService.findUserCanGainBonus(order.getInt(Fields.SDL_ID), amt_typ)-1;
	    	return build(gainBonus,sum);
        }else {
        	return build(null,0);
        }
   	}
	
	/**
	 * 
	 * @param bonusList  已摘
	 * @param sum  可摘
	 * @return
	 */
	private List<Map<String,Object>> build(List<Bonus> bonusList,int sum){
		List<Map<String,Object>> fruitList = new ArrayList<Map<String,Object>>();
		Map<Integer,Integer> sqn = getSqnMap();
		for(int i=0;i<7;i++) {  //已摘
			if(bonusList!=null&&bonusList.size()>i) {
				Map<String,Object> item = new HashMap<String,Object>();
				item.put(Fields.BON_ID, bonusList.get(i).get(Fields.BON_ID));
				item.put(Fields.STATE, "S");
				item.put(Fields.SQN_NO, bonusList.get(i).get(Fields.SQN_NO));
				sqn.remove(bonusList.get(i).get(Fields.SQN_NO));
				item.put(Fields.IS_REMIND,0);
				fruitList.add(item);
			}else if(sum>i){  //可摘
				Map<String,Object> item = new HashMap<String,Object>();
				item.put(Fields.STATE, "P");
				item.put(Fields.SQN_NO, getNextSqn(sqn));
				item.put(Fields.IS_REMIND,1);
				fruitList.add(item);
			}else {  //不可摘
				Map<String,Object> item = new HashMap<String,Object>();
				item.put(Fields.STATE, "F");
				item.put(Fields.SQN_NO, getNextSqn(sqn));
				item.put(Fields.IS_REMIND,0);
				fruitList.add(item);
			}
		}
		return fruitList;
	}
	
	//初始化一个1-7的map
	private Map<Integer,Integer> getSqnMap(){
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i=0;i<7;i++) {
			map.put(i+1, i+1);
		}
		return map;
	}
	
	//获得map里的节点
	private int getNextSqn(Map<Integer,Integer> hm) {
        Set<Integer> keSet=hm.keySet();  
        for (Iterator<Integer> iterator = keSet.iterator(); iterator.hasNext();) {  
        	Integer sqn = iterator.next();  
            hm.remove(sqn);
            return sqn;
        }  
        return 0;
	}
	
}
