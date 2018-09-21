package com.pintuan.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.common.Fields;
import com.pintuan.model.Address;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 用户地址
 * 
 * @author Administrator
 *
 */
public class AddressService extends BaseService {

	/**
	 * 查询用户地址列表
	 * 
	 * @param usr_id
	 * @param is_def
	 * @return
	 */

	public List<Address> findByUsrId(String usr_id, String is_def) {
		if (StrUtils.empty(is_def)) {
			String sqlWhere = "where usr_id=?";
			return Address.dao.findByWhere(sqlWhere, usr_id);
		} else {
			String sqlWhere = "where usr_id=? and is_def=?";
			return Address.dao.findByWhere(sqlWhere, usr_id, is_def);
		}
	}

	/**
	 * 删除地址
	 * 
	 * @param adr_id
	 * @return
	 */
	public int delete(String adr_id) {
		String sql = "delete from pt_address where adr_id=?";
		return Db.update(sql, adr_id);
	}

	/**
	 * 添加地址
	 * 
	 * @param usr_id
	 * @param rec_nme
	 * @param rec_pho
	 * @param rec_adr
	 * @param is_def
	 */
	public void add(String usr_id, String rec_nme, String rec_pho, String rec_adr, String is_def) {
		Address address = new Address();
		address.set(Fields.ADR_ID, UuidUtils.getUUID2());
		address.set(Fields.USER_ID, usr_id);
		address.set(Fields.REC_NME, rec_nme);
		address.set(Fields.REC_PHO, rec_pho);
		address.set(Fields.REC_ADR, rec_adr);
		address.set(Fields.IS_DEF, is_def);
		address.save();
	}

	/**
	 * 更改默认设置
	 * 
	 * @param usr_id
	 * @return
	 */
	public int updateDefaultSet(String usr_id) {
		String sql = "update pt_address set is_def=? where usr_id=?";
		return Db.update(sql, "0", usr_id);
	}

	/**
	 * 更改地址
	 * 
	 * @param adr_id
	 * @param rec_nme
	 * @param rec_pho
	 * @param rec_adr
	 * @param is_def
	 */
	public void update(String adr_id, String rec_nme, String rec_pho, String rec_adr, String is_def) {
		Address address = new Address();
		address.set(Fields.ADR_ID, adr_id);
		if (StrUtils.isNotEmpty(rec_nme))
			address.set(Fields.REC_NME, rec_nme);
		if (StrUtils.isNotEmpty(rec_pho))
			address.set(Fields.REC_PHO, rec_pho);
		if (StrUtils.isNotEmpty(rec_adr))
			address.set(Fields.REC_ADR, rec_adr);
		if (StrUtils.isNotEmpty(is_def))
			address.set(Fields.IS_DEF, is_def);
		address.update();
	}

}
