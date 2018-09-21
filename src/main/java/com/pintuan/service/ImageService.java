package com.pintuan.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.Img;
import com.pintuan.model.User;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.system.rolemenu.SysRoleMenu;
import com.supyuan.util.DateUtils;
import com.supyuan.util.NumberUtils;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;

public class ImageService extends BaseService {

	/**
	 * 通过第三方外键查找图片
	 * 
	 * @param pro_id
	 * @param state
	 */	
	public List<Img> findByThdId(String thd_id,String img_typ,String state) {
		String sqlWhere = "where thd_id=? and img_typ=? and state=?";
		return Img.dao.findByWhere(sqlWhere, thd_id,img_typ,state);				
	}
	
	/**
	 * 通过第三方外键查找图片
	 * 
	 * @param pro_id
	 * @param state
	 */	
	public List<Img> findByThdId(String thd_id,String state) {
		String sqlWhere = "where thd_id=? and state=?";
		return Img.dao.findByWhere(sqlWhere, thd_id,state);				
	}
	
	/**
	 * 删除图片
	 */
	public int delete(String img_id) {
		String sql = "delete from pt_img where img_id=?";
		return Db.update(sql,img_id);
	}
	
	/**
	 * 添加图片
	 */
	public void add(String pro_id,String url,String img_typ,String size,String picel) {
		Img img = new Img();
		img.set(Fields.IMG_ID, UuidUtils.getUUID2());
		img.set(Fields.THD_ID, pro_id);
		img.set(Fields.IMG_URL, url);
		img.set(Fields.IMG_SRT, 0);
		img.set(Fields.IMG_TYP, img_typ);
		img.set(Fields.STATE, Constants.ACCESS_STATE);
		img.set(Fields.IMG_SIZE, size);
		img.set(Fields.PICEL, picel);
		img.save();
	}

}
