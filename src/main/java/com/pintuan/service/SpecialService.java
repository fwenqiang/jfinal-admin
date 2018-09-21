package com.pintuan.service;

import java.math.BigDecimal;
import java.util.Date;

// import org.springframework.util.Assert;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.common.Fields;
import com.pintuan.model.Special;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.extend.UuidUtils;

/**特色业务**/
public class SpecialService extends BaseService {

	/**
	 * 添加
	 * 
	 */
	public Special add(BigDecimal amt,String usr_id,String spe_typ) {
		Special special = new Special();
		special.set(Fields.SPE_ID, UuidUtils.getUUID2());
		special.set(Fields.USER_ID, usr_id);
		special.set(Fields.AMT, amt);
		special.set(Fields.STATE, "I");
		special.set(Fields.SPE_TYP, spe_typ);
		special.set(Fields.CREATE_TIME, new Date());
		special.save();
		return special;
	}
	
	
	
	
	/**
	 * 
	 * 
	 */
	public void updateState(String spe_id) {
		String sql = "update pt_special set state=? where spe_id=?";
		Db.update(sql,"S",spe_id);
	}
	
	public Special find(String usr_id,String spe_typ) {
		return Special.dao.findFirstByWhere("where usr_id=? and spe_typ=? and state='I'", usr_id,spe_typ);
	}
	
	public Special findById(String spe_id,String state) {
		return Special.dao.findFirstByWhere("where spe_id=? and state=?", spe_id,state);
	}

}
