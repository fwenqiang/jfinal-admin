package com.pintuan.service;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

// import org.springframework.util.Assert;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.IdentifyCode;
import com.pintuan.model.User;
import com.pintuan.util.Assert;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
import com.supyuan.util.extend.UuidUtils;

public class CommonService extends BaseService {

	/**
	 * 校验验证码
	 * 
	 * @param phoneNo
	 * @param ideCde
	 * @param ideCdeTyp
	 */
	public IdentifyCode validateIdentifyCode(String phoneNo, String ideCde,String ideCdeTyp) {
		String sql = " where pho_no=? and ide_cde=? and ide_cde_typ=? and val_tme>=?";
		Date current = new Date();
		IdentifyCode record = IdentifyCode.dao.findFirstByWhere(sql, phoneNo, ideCde,ideCdeTyp,current);
		Assert.notEmpty(record, ErrCode.IDENTIFY_CODE_ERROR);
		return record;
	}
	
	/**
	 * 查询
	 * 
	 * @param ideId
	 */
	public IdentifyCode findIdentify(String ideId) {		
		return IdentifyCode.dao.findById(ideId);
	}
	
	/**
	 * 生成验验证码
	 * 
	 * @param phoneNo
	 * @param ideCde
	 * @param ideCdeTyp
	 */
	public IdentifyCode createIdentifyCode(String phoneNo,String ideCdeTyp) {
		
		IdentifyCode ideCode = this.findIdentifyCode(phoneNo, ideCdeTyp);
		if(ideCode!=null) return ideCode;
		ideCode = new IdentifyCode();
		ideCode.set(Fields.IDENTIFY_ID, UuidUtils.getUUID2());
		ideCode.set(Fields.PHONE_NO, phoneNo);
		ideCode.set(Fields.IDENTIFY_CODE_TYPE, ideCdeTyp);
		Date now = new Date();
		ideCode.set(Fields.CREATE_TIME, now );
		ideCode.set(Fields.VALIDATE_TIME, DateUtils.addSeconds(now, 60*30));
		ideCode.set(Fields.UPDATE_TIME, DateUtils.addSeconds(now, 60*15));
		ideCode.set(Fields.IDENTIFY_CODE, UuidUtils.randomNumStr(4)); 
		ideCode.save();
		return ideCode;
	}
	
	public IdentifyCode findIdentifyCode(String phoneNo,String ideCdeTyp) {
		
		return IdentifyCode.dao.findFirstByWhere("where pho_no=? and ide_cde_typ=? and val_tme>=?", phoneNo,ideCdeTyp,new Date());
	}

}
