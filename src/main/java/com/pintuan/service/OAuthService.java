package com.pintuan.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.OAuth;
import com.pintuan.model.User;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.system.rolemenu.SysRoleMenu;
import com.supyuan.util.DateUtils;
import com.supyuan.util.NumberUtils;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;

public class OAuthService extends BaseService {


	/**
	 * 通过openid查询列表
	 * 
	 * @param openid
	 */
	public OAuth findByOpenId(String openid) {
		return OAuth.dao.findById(openid);
	}
	
	/**
	 * 更改成已使用状态
	 * @param oauth
	 */
	public void update(OAuth oauth) {
		oauth.set(Fields.STATE, "S");
		oauth.update();
	}
	
	/**
	 * 查询权限用户
	 * 
	 * @param openid
	 */
	public OAuth find(Map<String,Object> userInfo) {
		String where="where nickname=? and sex=? and province=? and city=? and country=? and state=?";
		return OAuth.dao.findFirstByWhere(where, userInfo.get("nickname"),userInfo.get("sex"),userInfo.get("province"),userInfo.get("city"),userInfo.get("country"),"I");
	}

	/**
	 * 添加第三方授权用户
	 * 
	 * @param context
	 */
	public OAuth add(Map<String, Object> context,String p_id) {
		OAuth model = new OAuth();
		model.set(Fields.P_ID, p_id);
		model.set(WeixinFields.OPENID, context.get(WeixinFields.OPENID));
		model.set(WeixinFields.NICKNAME, context.get(WeixinFields.NICKNAME).toString());
		model.set(WeixinFields.SEX, context.get(WeixinFields.SEX));
		model.set(WeixinFields.PROVINCE, context.get(WeixinFields.PROVINCE));
		model.set(WeixinFields.CITY, context.get(WeixinFields.CITY));
		model.set(WeixinFields.COUNTRY, context.get(WeixinFields.COUNTRY));
		model.set(WeixinFields.HEADIMGURL, context.get(WeixinFields.HEADIMGURL));
		//model.set(WeixinFields.PRIVILEGE, context.get(WeixinFields.PRIVILEGE));
		model.set(WeixinFields.UNIONID, context.get(WeixinFields.UNIONID));
		model.set(Fields.STATE, "I");
		model.save();

		return model;
	}
	

}
