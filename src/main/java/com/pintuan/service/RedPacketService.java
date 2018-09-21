package com.pintuan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.RedPacket;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.extend.UuidUtils;

public class RedPacketService extends BaseService {

	
	public void add(String usr_id, String crt_usr_id, String crt_usr_nme, String tit_url,BigDecimal amt) {
		RedPacket redPacket = new RedPacket();
		redPacket.set(Fields.PAC_ID, UuidUtils.getUUID2());
		redPacket.set(Fields.USER_ID, usr_id);
		redPacket.set(Fields.CRT_USR_ID, crt_usr_id);
		redPacket.set(Fields.CRT_USR_NME, crt_usr_nme);
		redPacket.set(Fields.TIT_URL, tit_url);
		redPacket.set(Fields.CREATE_TIME, new Date());
		redPacket.set(Fields.AMT, amt);
		redPacket.set(Fields.STATE, Constants.REDPACKET_STATE_INIT);
		redPacket.save();
	}
	
	public List<RedPacket> findRedPacketList(String usr_id) {
		return RedPacket.dao.findByWhere("where usr_id=?",usr_id);
	}
	
	public RedPacket findRedPacket(String pac_id) {
		return RedPacket.dao.findById(pac_id);		
	}
	
	public void updateRedPacket(RedPacket redPacket) {
		if(redPacket==null) return ;
		redPacket.update();	
	}

}
