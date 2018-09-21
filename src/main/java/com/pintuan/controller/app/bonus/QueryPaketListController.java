package com.pintuan.controller.app.bonus;

import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.Config;
import com.pintuan.service.BonusService;
import com.pintuan.service.ConfigService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询钱包列表
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryPaketList")
@Before(CheckUserKeyInterceptor.class)
public class QueryPaketListController extends BaseProjectController {
	private BonusService bonusService = new BonusService();

	private ConfigService configService = new ConfigService();
	
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		List<BonusCfg> pakets = bonusService.findBonusCfgs(usr_ide_key);
        setResp(Fields.PAKET_LIST,DBModelUtils.toMaps(pakets));
        //是否显示钱包
		Config config = configService.findConfig(Fields.CFG_ID);
		setResp(Fields.IS_PAKET_LIST_SHOW,config.getInt("show_packet")==0?false:true);
        returnJson();
	}	
	
	
	
}
