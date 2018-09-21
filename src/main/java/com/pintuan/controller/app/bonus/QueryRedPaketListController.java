package com.pintuan.controller.app.bonus;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Config;
import com.pintuan.model.RedPacket;
import com.pintuan.service.ConfigService;
import com.pintuan.service.RedPacketService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询红包列表
 * 
 * @author zjh 2018-5-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryRedPaketList")
@Before(CheckUserKeyInterceptor.class)
public class QueryRedPaketListController extends BaseProjectController {
	private RedPacketService redPacketService = new RedPacketService();

	private ConfigService configService = new ConfigService();
	
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		List<RedPacket> redPakets = redPacketService.findRedPacketList(usr_ide_key);
		if(redPakets!=null&&redPakets.size()>4) {
			for(int i=redPakets.size();i>4;i--) {
				String sql = "delete from pt_red_packet where pac_id=?";
				Db.update(sql, redPakets.get(i-1).getStr(Fields.PAC_ID));
			}
		}
        setResp(Fields.RED_PAKET_LIST,DBModelUtils.toMaps(redPakets));
		//是否显示红包
		Config config = configService.findConfig(Fields.CFG_ID);
		setResp(Fields.IS_RED_PAKET_LIST_SHOW,config.getInt("show_red_packet")==0?false:true);
        returnJson();
	}
	
	
	
}
