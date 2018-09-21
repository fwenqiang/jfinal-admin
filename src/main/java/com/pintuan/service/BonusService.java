package com.pintuan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// import org.springframework.util.Assert;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.Bonus;
import com.pintuan.model.BonusCfg;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.extend.UuidUtils;

/**我的钱包**/
public class BonusService extends BaseService {

	/**
	 * 添加我的钱包
	 * 
	 */
	public BonusCfg addMyPaket(String usr_id, String bon_cfg_typ) {
		BonusCfg paket = new BonusCfg();
		paket.set(Fields.BON_CFG_ID, UuidUtils.getUUID2());
		paket.set(Fields.USER_ID, usr_id);
		paket.set(Fields.TOL_AMT, BigDecimal.ZERO);
		paket.set(Fields.BON_CFG_TYP, bon_cfg_typ);
		paket.save();
		return paket;
	}
	
	
	/**
	 * 分红收支入钱包
	 * 
	 */
	public void bonusToPaket(Bonus bonus,BonusCfg paket) {
		
		BigDecimal all = paket.getBigDecimal(Fields.TOL_AMT).add(bonus.getBigDecimal(Fields.FEE));
		paket.set(Fields.TOL_AMT, all);
		paket.update();
	}
	
	/**
	 * 分红收支入钱包
	 * 
	 */
	public void updatePaket(BigDecimal amt,String usr_id,String bon_cfg_typ) {
		String sql = "update pt_bonus_cfg set tol_amt=tol_amt+? where usr_id=? and bon_cfg_typ=?";
		Db.update(sql,amt,usr_id,bon_cfg_typ);
	}
	
	/**
	 * 提现出红包
	 * 
	 */
	public void subPaket(BigDecimal amt,String usr_id,String bon_cfg_typ) {
		String sql = "update pt_bonus_cfg set tol_amt=tol_amt-? where usr_id=? and bon_cfg_typ=?";
		Db.update(sql,amt,usr_id,bon_cfg_typ);
	}
	//***********************************************************************************************
	
	/**
	 * 查询已获得分红
	 * 
	 */
	public List<Bonus> queryGainBonus(String to_usr_id,String fro_usr_id,String amt_typ) {
		String where = "where to_usr_id=? and fro_usr_id=? and amt_typ=?";
		return Bonus.dao.findByWhere(where, to_usr_id,fro_usr_id,amt_typ);
	}
	
	/**
	 * 添加果实收取记录
	 * 
	 */
	public Bonus addFruitBonus(String bon_cfg_id,BigDecimal fee,String to_usr_id,String fro_usr_id,String amt_typ,String sqn_no) {
		Bonus bonus = new Bonus();
		bonus.set(Fields.BON_ID, UuidUtils.getUUID2());
		bonus.set(Fields.BON_CFG_ID, bon_cfg_id);
		bonus.set(Fields.FEE, fee);
		bonus.set(Fields.TO_USR_ID, to_usr_id);
		bonus.set(Fields.FRO_USR_ID, fro_usr_id);
		bonus.set(Fields.AMT_TYP, amt_typ);
		bonus.set(Fields.SQN_NO, sqn_no);
		bonus.set(Fields.CREATE_TIME, new Date());
		bonus.set(Fields.STATE, Constants.BONUS_STATE_SUCCESS);
		bonus.save();
		return bonus;
	}
	
	/**
	 * 查询果实收取记录
	 * 
	 */
	public Bonus findFruitBonus(String bon_cfg_id,String to_usr_id,String fro_usr_id,String amt_typ,String sqn_no) {
		String where = "where bon_cfg_id=? and to_usr_id=? and fro_usr_id=? and amt_typ=? and sqn_no=?";
		return Bonus.dao.findFirstByWhere(where, bon_cfg_id,to_usr_id,fro_usr_id,amt_typ,sqn_no);
	}
	
	/**
	 * 查询我的钱包
	 * 
	 */
	public BonusCfg findBonusCfg(String usr_id, String bon_cfg_typ) {
		String where = "where usr_id=? and bon_cfg_typ=?";
		return BonusCfg.dao.findFirstByWhere(where, usr_id,bon_cfg_typ);
	}
	
	/**
	 * 查询我的钱包
	 * 
	 */
	public List<BonusCfg> findBonusCfgs(String usr_id) {
		String where = "where usr_id=?";
		return BonusCfg.dao.findByWhere(where, usr_id);
	}
	
	/**
	 * 查询果实收取记录
	 * @param usr_id
	 * @return
	 */
	public List<Record> findBonusRecord(String usr_id) {
		String sql = "SELECT b.bon_id,b.fee,b.crt_tme, u1.usr_nme to_usr_nme,u2.usr_nme fro_usr_nme from pt_bonus b , pt_user u1 ,pt_user u2 where b.to_usr_id=u1.usr_id and b.fro_usr_id=u2.usr_id and b.fro_usr_id=? ORDER BY b.crt_tme desc";
		return Db.find(sql,usr_id);
	}
	
	/**
	 * 查询果实收取记录
	 * @param fro_usr_id
	 * @param to_usr_id
	 * @return
	 */
	public List<Record> findBonusRecord(String fro_usr_id,String to_usr_id) {
		String sql = "SELECT b.bon_id,b.fee,b.crt_tme, u1.usr_nme to_usr_nme,u2.usr_nme fro_usr_nme from pt_bonus b , pt_user u1 ,pt_user u2 where b.to_usr_id=u1.usr_id and b.fro_usr_id=u2.usr_id and b.fro_usr_id=? and b.to_usr_id=? ORDER BY b.crt_tme desc";
		return Db.find(sql,fro_usr_id,to_usr_id);
	}
	/**
	 * 查询收获果实的总量
	 * @param to_usr_id
	 * @return
	 */
	public long findBonusSum(String to_usr_id) {
		String sql = "select count(1) from pt_bonus where to_usr_id=?";
		return Db.queryLong(sql,to_usr_id);
	}
	
	/**
	 * 查询收获自己果实的总量
	 * @param to_usr_id
	 * @return
	 */
	public long findSelfBonusSum(String usr_id) {
		String sql = "select count(1) from pt_bonus where to_usr_id=? and fro_usr_id=?";
		return Db.queryLong(sql,usr_id,usr_id);
	}
	
	/**
	 * 查询收获徒弟果实的总量
	 * @param to_usr_id
	 * @return
	 */
	public long findChildBonusSum(String usr_id) {
		String sql = "select count(1) from pt_bonus b where b.to_usr_id=? and b.fro_usr_id in (select usr_id from pt_user u where u.p_id=b.to_usr_id)";
		return Db.queryLong(sql,usr_id);
	}
	
	/**
	 * 查询平台基金金额
	 * @param bon_cfg_typ
	 * @return
	 */
	public BigDecimal findBonusCfgSum(String bon_cfg_typ) {
		String sql = "select sum(c.tol_amt) from pt_user u,pt_bonus_cfg c where u.usr_id=c.usr_id and c.bon_cfg_typ=?";
		return Db.queryBigDecimal(sql,bon_cfg_typ);
	}
	

}
