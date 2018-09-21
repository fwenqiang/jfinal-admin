package com.pintuan.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.model.Deposit;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;

public class DepositService extends BaseService {

	/**
	 * 查询提现列表
	 */
	public List<Record> findDepositList(String usr_nme, String state, String crt_time, int page, int size) {
		String usr_nme_sql = "u.usr_nme like concat('%','" + usr_nme + "', '%') and ";
		if (StrUtils.isEmpty(usr_nme)) {
			usr_nme_sql = "";
		}

		String crt_time_sql = "d.crt_tme<='" + crt_time + "' and ";
		if (StrUtils.isEmpty(crt_time)) {
			crt_time_sql = "";
		}

		String sql = "select d.*,u.tit_url,u.thd_id,u.usr_nme as name,u.pho_no, case d.dep_typ when '1' then '微信' when '2' then '支付宝' when '3' then '银行卡' else '其他' end as cn_dep_typ from pt_deposit d left join pt_user u on d.usr_id=u.usr_id where "
				+ usr_nme_sql + " " + crt_time_sql + " d.state='1' order by d.crt_tme desc limit ?,?";
		if (!"1".equals(state)) {
			sql = "select d.*,u.tit_url,u.thd_id,u.usr_nme as name,u.pho_no, case d.dep_typ when '1' then '微信' when '2' then '支付宝' when '3' then '银行卡' else '其他' end as cn_dep_typ from pt_deposit d left join pt_user u on d.usr_id=u.usr_id where "
					+ usr_nme_sql + " " + crt_time_sql + " d.state!='1' order by d.crt_tme desc limit ?,?";
		}

		return Db.find(sql, page * size, size);
	}

	/**
	 * 查询条数
	 * 
	 * @param usr_nme
	 * @param state
	 * @return
	 */
	public long findSize(String usr_nme, String state, String crt_time) {
		String usr_nme_sql = "u.usr_nme like concat('%','" + usr_nme + "', '%') and ";
		if (StrUtils.isEmpty(usr_nme)) {
			usr_nme_sql = "";
		}

		String crt_time_sql = "d.crt_tme<='" + crt_time + "' and ";
		if (StrUtils.isEmpty(crt_time)) {
			crt_time_sql = "";
		}

		String sql = "select count(1) from pt_deposit d left join pt_user u on d.usr_id=u.usr_id where " + usr_nme_sql
				+ " " + crt_time_sql + " d.state='1'";
		if (!"1".equals(state)) {
			sql = "select count(1) from pt_deposit d left join pt_user u on d.usr_id=u.usr_id where " + usr_nme_sql
					+ " " + crt_time_sql + " d.state!='1'";
		}
		return Db.queryLong(sql);
	}

	/**
	 * 查询提现列表
	 */
	public List<Deposit> findDepositList(String usr_id) {
		return Deposit.dao.findByWhere("where usr_id=? order by upd_tme desc", usr_id);
	}

	/**
	 * 查询提现列表
	 */
	public Deposit findById(String dep_id) {
		return Deposit.dao.findById(dep_id);
	}

}
