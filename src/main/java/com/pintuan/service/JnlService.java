package com.pintuan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.common.WXConstants;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.Jnl;
import com.pintuan.model.Order;
import com.pintuan.model.User;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.system.rolemenu.SysRoleMenu;
import com.supyuan.util.DateUtils;
import com.supyuan.util.NumberUtils;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;

public class JnlService extends BaseService {
	
	/**
	 * 初始化微信流水
	 * 
	 * @param context
	 */
	public Jnl initWXJnl(String ord_id,BigDecimal tol_fee,String pay_chl,String req_chl,User user) {
		Jnl jnl = new Jnl();
		jnl.set(Fields.JNL_ID, UuidUtils.getUUID2());
		jnl.set(Fields.STATE, Constants.JNL_STATE_ABORT);
		jnl.set(Fields.TOL_FEE, tol_fee);
		jnl.set(Fields.CREATE_TIME, new Date());
		jnl.set(Fields.PAY_CHL, pay_chl);
		jnl.set(Fields.REQ_CHL, req_chl);
		jnl.set(Fields.APPID, WXConstants.APP_ID);
		jnl.set(Fields.MCH_ID, WXConstants.MCH_ID);
		jnl.set(Fields.ORD_ID, ord_id);
		jnl.set(Fields.OPENID, user.get(Fields.OPENID));
		jnl.set(Fields.USER_ID, user.get(Fields.USER_ID));
		jnl.set(Fields.USER_NAME, user.get(Fields.USER_NAME));
		jnl.save();
		return jnl;
	}
	
	/**
	 * 更新流水状态
	 * 
	 */
	public Jnl updateJnlState(Jnl jnl,String state) {
		if(jnl==null) return jnl;
		jnl.set(Fields.STATE, state);
		jnl.update();
		return jnl;
	}
	
	/**
	 * 查询
	 * 
	 */
	public Jnl findByOrdId(String ord_id) {
		String where = "where ord_id=?";
		return Jnl.dao.findFirstByWhere(where, ord_id);
	}
	
	//
	public Jnl findByUserId(String usr_id,String amt_typ) {
		String sql = "where usr_id=? and state in ('S','T','E') and amt_typ=?";
		return Jnl.dao.findFirstByWhere(sql, usr_id,amt_typ);
	}
	
	//
	public List<Jnl> findByUserId(String usr_id) {
		String sql = "where usr_id=? and state in ('S','T','E')";
		return Jnl.dao.findByWhere(sql, usr_id);
	}
	
	public void deleteJnl(String jnl_id) {
		Db.deleteById("pt_jnl", "jnl_id",jnl_id);
	}
	
	public void deleteJnlByOrdId(String ord_id) {
		String sql = "delete from pt_jnl where ord_id=?";
		Db.update(sql,ord_id);
	}

}
