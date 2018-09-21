package com.pintuan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.AmtRecord;
import com.pintuan.model.BankCard;
import com.pintuan.model.Deposit;
import com.pintuan.model.User;
import com.pintuan.model.UserGrid;
import com.pintuan.model.UserPayInfo;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;
import org.apache.commons.lang.StringUtils;

public class UserService extends BaseService {

	public static void main(String[] args) {
		System.out.println(new Md5Utils().getMD5(Base64.decodeAsString("MTIzNDU2")));
	}

	/**
	 * 通过手机号和密码查找用户
	 *
	 * @param phoneNo
	 * @param pwd
	 */
	public Record findUser(String phoneNo, String pwd) {
		String sql = "select * from pt_user where pho_no=? and pwd=?";
		Record record = Db.findFirst(sql, phoneNo, new Md5Utils().getMD5(Base64.decodeAsString(pwd)));
		return record;
	}

	/**
	 * 通过手机号和密码查找用户
	 *
	 * @param thd_id
	 * @param pwd
	 */
	public Record findUserByThdIdAndPwd(String thd_id, String pwd) {
		// System.out.println("pwd="+new Md5Utils().getMD5(Base64.decodeAsString(pwd)));
		String sql = "select * from pt_user where thd_id=? and pwd=? and state=?";
		Record record = Db.findFirst(sql, thd_id, new Md5Utils().getMD5(Base64.decodeAsString(pwd)),
				Constants.ACCESS_STATE);
		return record;
	}

	/**
	 * 通过手机号用户列表
	 *
	 * @param phoneNo
	 */
	public List<Record> findUserByPhone(String phoneNo) {
		String sql = "select * from pt_user where pho_no=?";
		List<Record> record = Db.find(sql, phoneNo);
		return record;
	}

	/**
	 * 通过手机号用户
	 *
	 * @param phoneNo
	 */
	public User findFirstUserByPhone(String phoneNo) {
		String whereSql = " where pho_no=? and state=?";
		return User.dao.findFirstByWhere(whereSql, phoneNo, Constants.ACCESS_STATE);
	}

	/**
	 * 通过手机号用户列表
	 *
	 * @param phoneNo
	 */
	public List<User> findUserListByPhone(String phoneNo) {
		String sql = "select usr_nme,thd_id from pt_user where pho_no=?";
		return User.dao.find(sql, phoneNo);
	}

	/**
	 * 添加用户
	 *
	 * @param context
	 */
	public User add(Map<String, Object> context) {
		User model = new User();
		if (!StrUtils.isEmpty((String) context.get(Fields.P_ID))) {
			model.set(Fields.P_ID, context.get(Fields.P_ID));
		}
		model.set(Fields.USER_ID, UuidUtils.getUUID2());
		model.set(Fields.PHONE_NO, context.get(Fields.PHONE_NO));
		model.set(Fields.PASSWORD,
				new Md5Utils().getMD5(Base64.decodeAsString(context.get(Fields.PASSWORD).toString())));
		model.set(Fields.USER_NAME, context.get(Fields.USER_NAME));
		model.set(Fields.STATE, Constants.ACCESS_STATE);
		model.set(Fields.USER_TYPE, Constants.USER_TYPE_COMMON);
		model.set(Fields.CREATE_TIME, new Date());
		model.set(Fields.POI_SCR, 0);
		model.set(Fields.TIT_URL, Constants.DEFAULT_USER_TITLE_URL);
		if (context.get(Fields.THD_ID) != null)
			model.set(Fields.THD_ID, context.get(Fields.THD_ID));
		model.save();
		UserGrid userGrid = new UserGrid();
		userGrid.set(Fields.USER_ID, model.get(Fields.USER_ID));
		userGrid.set(Fields.USR_TIT, "平民");
		userGrid.set(Fields.USR_GRI, "石头");
		userGrid.set(Fields.USR_RAN, 0);
		userGrid.save();
		return model;
	}

	/**
	 * 删除用户
	 *
	 */
	public void delete(String pho_no) {
		String sql = "delete from pt_user where pho_no=?";
		Db.update(sql, pho_no);
	}

	/**
	 * 保存用户，如果没有ID，则新增用户
	 *
	 * @param context
	 * @return 0-失败；1-保存；2-新增
	 */
	public int save(User user) {
		int result = 0;
		if (user == null) {
			return result;
		}
		if (StrUtils.isEmpty(user.getStr(Fields.USER_ID))) {
			user.set(Fields.USER_ID, UuidUtils.getUUID2());
			user.save();
			UserGrid userGrid = new UserGrid();
			userGrid.set(Fields.USER_ID, user.get(Fields.USER_ID));
			userGrid.set(Fields.USR_TIT, "平民");
			userGrid.set(Fields.USR_GRI, "石头");
			userGrid.set(Fields.USR_RAN, 0);
			userGrid.save();
			result = 2;
		} else {
			user.update();
			result = 1;
		}
		return result;
	}

	public User find(String id) {
		return User.dao.findById(id);
	}

	public User findByThdId(String thd_id) {
		String where = "where thd_id=?";
		return User.dao.findFirstByWhere(where, thd_id);
	}

	public User findByOpenid(String openid) {
		String whereSql = " where openid=?";
		return User.dao.findFirstByWhere(whereSql, openid);
	}

	/**
	 * 添加用户
	 *
	 * @param context
	 */
	public User add(Map<String, Object> userInfo, User user) {
		if (user == null) {
			user = new User();
		}
		user.set(Fields.USER_ID, UuidUtils.getUUID2());
		user.set(Fields.USER_NAME, userInfo.get(WeixinFields.NICKNAME));
		user.set(Fields.STATE, Constants.ACCESS_STATE);
		user.set(Fields.USER_TYPE, Constants.USER_TYPE_COMMON);
		user.set(Fields.CREATE_TIME, new Date());
		user.set(Fields.POI_SCR, 0);
		user.set(WeixinFields.OPENID, userInfo.get(WeixinFields.OPENID));
		user.save();
		return user;
	}

	// 查询子用户
	public List<Record> findChildrenUser(String p_id) {
		String sql = "select u.usr_id,u.tit_url,u.usr_nme,o.pro_amt, o.crt_tme,o.state from pt_user u, (select * from pt_order where state in ('S','T','E') and pro_amt=?) o where u.usr_id=o.usr_id and u.p_id=? order by o.crt_tme desc";
		return Db.find(sql, new BigDecimal("1990"), p_id);
	}

	public List<Map<String,Object>> findSameUser(int sameNum,int page, int size) {
		String sql = "select * from (select count(*) as total_account ,pho_no from pt_user where usr_id in(select usr_id from pt_order where state in ('S','T','E'))  group by pho_no) a where total_account >=? order by total_account desc";

		sql += "  limit " + (page * size) + "," + size;

		List<Record> list = Db.find(sql,sameNum);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

		if(CollectionUtils.isNotEmpty(list)){
			for(Record r : list){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("total_account", r.getLong("total_account"));
				map.put("pho_no", r.getStr("pho_no"));

				result.add(map);
			}
		}
		return result;
	}

	public long countSameUser(int sameNum) {
		String sql = "select count(*) from (select count(*) as total_account ,pho_no from pt_user where usr_id in(select usr_id from pt_order where state in ('S','T','E'))  group by pho_no) a where total_account > ?";
		return Db.queryLong(sql,sameNum);
	}

	public Map<String,Object> findRelateChildrenUserMap(User user){

		Map<String, Object> result = new HashMap<String, Object>();

		String p_id = user.getStr(Fields.USER_ID);
		String usr_nme = user.getStr(Fields.USER_NAME);

		result.put("name", usr_nme);

		Map<String,List<Record>> map = this.findAllRelateChildrenUser(p_id);
		List<Map<String,Object>> childList = this.formatChildMap(map, p_id);
		if(CollectionUtils.isNotEmpty(childList)){
			result.put("children", childList);
		}

		return result;
	}

	public List<Map<String,Object>> formatChildMap(Map<String,List<Record>> allMap,String p_id){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		List<Record> childList = allMap.get(p_id);
		if(CollectionUtils.isNotEmpty(childList)){

			for(Record r : childList){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("name", r.getStr(Fields.USER_NAME));

				List<Map<String,Object>> childMap = this.formatChildMap(allMap, r.getStr(Fields.USER_ID));
				if(CollectionUtils.isNotEmpty(childMap)){
					map.put("children", childMap);
				}
				list.add(map);
			}
		}

		return list;
	}

	public Map<String,List<Record>> findAllRelateChildrenUser(String p_id) {
		Map<String,List<Record>> result = new HashMap<String,List<Record>>();

		Map<String,List<Record>> recordMap = this.findRelateChildrenUserMap(p_id);
		List<Record> childList = recordMap.get(p_id);

		if(CollectionUtils.isNotEmpty(childList)){
			result.putAll(recordMap);
			for(Record r : childList){
				Map<String,List<Record>> resultMap = this.findAllRelateChildrenUser(r.getStr(Fields.USER_ID));
				result.putAll(resultMap);
			}
		}

		return result;
	}

	public List<Record> findRelateChildrenUser(String p_id) {
		String sql = "select u.usr_id,u.usr_nme from pt_user u where u.p_id=? order by u.usr_nme";
		return Db.find(sql, p_id);
	}

	public Map<String,List<Record>> findRelateChildrenUserMap(String p_id) {
		List<Record> list = this.findRelateChildrenUser(p_id);

		Map<String,List<Record>> result = new HashMap<String,List<Record>>();
		result.put(p_id, list);

		return result;
	}



	// 查询子用户个数
	public long findChildrenUserCount(String p_id) {
		String sql = "select count(1) from pt_user u, (select * from pt_order where state in ('S','T','E') and pro_amt=?) o where u.usr_id=o.usr_id and u.p_id=? ";
		return Db.queryLong(sql, new BigDecimal("1990"), p_id);
	}

	// 查询父级的子用户个数
	public long findParentChildrenUserCount(String usr_id) {
		String sql = "select count(1) from pt_user u, (select * from pt_order where state in ('S','T','E') and pro_amt=?) o where u.usr_id=o.usr_id and u.p_id=(select p_id from pt_user where usr_id=?)";
		return Db.queryLong(sql, new BigDecimal("1990"), usr_id);
	}

	// 查询徒孙
	public List<Record> findGrandChildUser(String p_id) {
		String sqlWhere = "select u.usr_id,u.tit_url,u.usr_nme,o.pro_amt, o.crt_tme,o.state from pt_user u, (select * from pt_order where state in ('S','T','E') and pro_amt=?) o where u.usr_id=o.usr_id and u.p_id in (select usr_id from pt_user where p_id=?) order by o.crt_tme";
		return Db.find(sqlWhere, new BigDecimal("1990"), p_id);
	}

	// 查询徒孙个数
	public long findGrandChildUserCount(String p_id) {
		String sql = "select count(1) from pt_user u, (select * from pt_order where state in ('S','T','E') and pro_amt=?) o where u.usr_id=o.usr_id and u.p_id in (select usr_id from pt_user where p_id=?) ";
		return Db.queryLong(sql, new BigDecimal("1990"), p_id);
	}

	/**************************************************************/
	/**
	 * 查询用户列表
	 *
	 * @param thd_id
	 * @param usr_nme
	 * @param pho_no
	 * @param page
	 * @param size
	 * @return
	 */
	public List<User> find(String thd_id, String usr_nme, String pho_no,String order_amount, int page, int size) {
		String whereSql = " where 1=1 ";
		if (StrUtils.isNotEmpty(thd_id)) {
			whereSql = whereSql + " and thd_id like concat('%','" + thd_id + "', '%')";
		}
		if (StrUtils.isNotEmpty(usr_nme)) {
			whereSql = whereSql + " and usr_nme like concat('%','" + usr_nme + "', '%')";
		}
		if (StrUtils.isNotEmpty(pho_no)) {
			whereSql = whereSql + " and pho_no like concat('%','" + pho_no + "', '%')";
		}
		if (StrUtils.isNotEmpty(order_amount)) {
			if("1990".equals(order_amount) || "800".equals(order_amount)){
				whereSql = whereSql + " and usr_id in (select usr_id from pt_order where pro_amt = "+order_amount+".00 and state<>'I' )";
			}else{
				whereSql = whereSql + " and usr_id not in (select usr_id from pt_order where  state<>'I' )";
			}
		}
		whereSql += " order by crt_tme desc limit " + (page * size) + "," + size;
		return User.dao.findByWhere(whereSql);
	}

	/**
	 * by eric 2018.09.18 21:16
	 * @param thd_id
	 * @param usr_nme
	 * @param pho_no
	 * @param order_amount
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Record> findUserList(String thd_id, String usr_nme, String pho_no,String order_amount, int page, int size) {
		String whereSql = " select u.*,b.tol_amt from pt_user u LEFT JOIN pt_bonus_cfg b on u.usr_id=b.usr_id where b.bon_cfg_typ='1' ";
		if (StrUtils.isNotEmpty(thd_id)) {
			whereSql = whereSql + " and u.thd_id like concat('%','" + thd_id + "', '%')";
		}
		if (StrUtils.isNotEmpty(usr_nme)) {
			whereSql = whereSql + " and u.usr_nme like concat('%','" + usr_nme + "', '%')";
		}
		if (StrUtils.isNotEmpty(pho_no)) {
			whereSql = whereSql + " and u.pho_no like concat('%','" + pho_no + "', '%')";
		}
		if (StrUtils.isNotEmpty(order_amount)) {
			if("1990".equals(order_amount) || "800".equals(order_amount)){
				whereSql = whereSql + " and u.usr_id in (select usr_id from pt_order where pro_amt = "+order_amount+".00 and state<>'I' )";
			}else{
				whereSql = whereSql + " and u.usr_id not in (select usr_id from pt_order where  state<>'I' )";
			}
		}
		whereSql += " order by u.crt_tme desc limit " + (page * size) + "," + size;
		return Db.find(whereSql);
	}

	/**
	 * 查询用户转账记录 by eric 2018.09.18 21:52
	 * @param usr_nme
	 * @param pho_no
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Record> queryUserTransferList(String usr_nme,String pho_no,int page, int size) {
		String whereSql = "select r.*,u.usr_nme,u.pho_no from pt_amt_record r LEFT JOIN pt_user u on r.usr_id=u.usr_id where r.red_det ='好友互转' ";
		if (StrUtils.isNotEmpty(usr_nme)) {
			whereSql = whereSql + " and u.usr_nme like concat('%','" + usr_nme + "', '%')";
		}
		if (StrUtils.isNotEmpty(pho_no)) {
			whereSql = whereSql + " and u.pho_no like concat('%','" + pho_no + "', '%')";
		}
		whereSql += " order by r.crt_tme desc limit " + (page * size) + "," + size;
		List<Record> record = Db.find(whereSql);
		return record;
	}

	/**
	 * 查询用户转账记录 总记录数 by eric 2018.09.18 21:52
	 * @param usrName
	 * @param phone
	 * @return
	 */
	public long queryUserTransferListSize(String usrName,String phone) {
		String whereSql = "select count(1) from pt_amt_record r LEFT JOIN pt_user u on r.usr_id=u.usr_id where r.red_det ='好友互转' ";
		if (StrUtils.isNotEmpty(usrName)) {
			whereSql = whereSql + " and u.usr_nme like concat('%','" + usrName + "', '%')";
		}
		if (StrUtils.isNotEmpty(phone)) {
			whereSql = whereSql + " and u.pho_no like concat('%','" + phone + "', '%')";
		}
		return Db.queryLong(whereSql);
	}

	/**
	 * 查询条数
	 *
	 * @param thd_id
	 * @param usr_nme
	 * @param pho_no
	 * @return
	 */
	public long findSize(String thd_id, String usr_nme, String pho_no, String order_amount) {
		String whereSql = "select count(1) from pt_user where 1=1 ";
		if (StrUtils.isNotEmpty(thd_id)) {
			whereSql = whereSql + " and thd_id like concat('%','" + thd_id + "', '%')";
		}
		if (StrUtils.isNotEmpty(usr_nme)) {
			whereSql = whereSql + " and usr_nme like concat('%','" + usr_nme + "', '%')";
		}
		if (StrUtils.isNotEmpty(pho_no)) {
			whereSql = whereSql + " and pho_no like concat('%','" + pho_no + "', '%')";
		}
		if (StrUtils.isNotEmpty(order_amount)) {
			if("1990".equals(order_amount) || "800".equals(order_amount)){
				whereSql = whereSql + " and usr_id in (select usr_id from pt_order where pro_amt = "+order_amount+".00 and state<>'I' )";
			}else{
				whereSql = whereSql + " and usr_id not in (select usr_id from pt_order where  state<>'I' )";
			}
		}
		return Db.queryLong(whereSql);
	}

	/**
	 * 去掉p_id并更新时间
	 *
	 */
	public void updatePidAndBuyTime(String usr_id) {
		String sql = "update pt_user set p_id='',buy_tme=? where usr_id=?";
		Db.update(sql, new Date(), usr_id);
	}

	/**
	 * 更新时间
	 *
	 */
	public void updateBuyTime(String usr_id) {
		String sql = "update pt_user set buy_tme=? where usr_id=?";
		Db.update(sql, new Date(), usr_id);
	}

	public BigDecimal queryBonusCfgSum(String usr_id) {
		String sql = "select sum(tol_amt) from pt_bonus_cfg where usr_id=?";
		return Db.queryBigDecimal(sql, usr_id);
	}

	public UserGrid findUserGrid(String usr_id) {
		return UserGrid.dao.findById(usr_id);
	}

	public UserGrid addUserGrid(String usr_id) {
		UserGrid userGrid = new UserGrid();
		userGrid.set(Fields.USER_ID, usr_id);
		userGrid.set(Fields.USR_TIT, "平民");
		userGrid.set(Fields.USR_GRI, "石头");
		userGrid.set(Fields.USR_RAN, 0);
		userGrid.set(Fields.UPDATE_TIME, new Date());
		userGrid.save();
		return userGrid;
	}

	// *******************************************//
	public UserPayInfo findUserPayInfo(String usr_id) {
		return UserPayInfo.dao.findFirstByWhere("where usr_id=?", usr_id);
	}

	public void addUserPayInfo(UserPayInfo userPayInfo) {
		if (userPayInfo == null)
			return;
		userPayInfo.save();
	}

	public void updateUserPayInfo(UserPayInfo userPayInfo) {
		if (userPayInfo == null)
			return;
		userPayInfo.update();
	}

	public List<BankCard> findBankCardList(String usr_id) {
		return BankCard.dao.findByWhere("where usr_id=?", usr_id);
	}

	public void addBankCard(BankCard bankCard) {
		if (bankCard == null)
			return;
		bankCard.save();
	}

	public void updateBankCard(BankCard bankCard) {
		if (bankCard == null)
			return;
		bankCard.update();
	}

	public void deleteBankCard(String car_id) {
		Db.deleteById("pt_bank_card", "car_id", car_id);
	}

	public void addDeposit(Deposit deposit) {
		if (deposit == null)
			return;
		deposit.save();
	}

	public Deposit findDeposit(String usr_id, String state) {
		return Deposit.dao.findFirstByWhere("where usr_id=? and state=?", usr_id, state);
	}

	public void deleteById(String usr_id) {
		String sql = "delete from pt_user where usr_id=?";
		Db.update(sql, usr_id);
	}

	public AmtRecord addAmtRecord(String usr_id, String bon_cfg_id, String red_typ, String red_nme, BigDecimal amt,
								  String dir_typ, String red_det,String target_usr_id) {
		AmtRecord amtRecord = new AmtRecord();
		amtRecord.set(Fields.RED_ID, UuidUtils.getUUID2());
		amtRecord.set(Fields.USER_ID, usr_id);
		amtRecord.set(Fields.BON_CFG_ID, bon_cfg_id);
		amtRecord.set(Fields.RED_TYP, red_typ);
		amtRecord.set(Fields.RED_NME, red_nme);
		amtRecord.set(Fields.AMT, amt);
		amtRecord.set(Fields.DIR_TYP, dir_typ);
		amtRecord.set(Fields.RED_DET, red_det);
		amtRecord.set(Fields.CREATE_TIME, new Date());
		amtRecord.set(Fields.TARGET_USR_ID, target_usr_id);
		amtRecord.save();
		return amtRecord;
	}

	public List<AmtRecord> findAmtRecordList(String usr_id) {
		return AmtRecord.dao.findByWhere("where usr_id=? order by crt_tme desc", usr_id);
	}

	public List<Record> findRank(int page, int size) {
		String sql = "select u.usr_nme,u.tit_url,g.* from pt_user u,pt_user_grid g where u.usr_id=g.usr_id order by g.usr_ran  desc,g.upd_tme desc limit ?,?";
		return Db.find(sql, page * size, size);
	}

	// 查询用户列表
	public List<Record> findUserList(String type,int page, int size) {
		String sql = "select p.usr_nme p_usr_nme,p.thd_id p_thd_id,u.* from pt_user u left join pt_user p on u.p_id=p.usr_id order by u.crt_tme limit ?,?";
		if("1".equals(type)){
			sql = "select p.usr_nme p_usr_nme,p.thd_id p_thd_id,u.* from pt_user u left join pt_user p on u.p_id=p.usr_id where u.usr_id in(select usr_id from pt_order o JOIN pt_product p on o.pro_id=p.pro_id where o.state in ('S','T','E' ) and p.amt_typ='1990') order by u.crt_tme limit ?,?";
		}
		return Db.find(sql, page * size, size);
	}

}
