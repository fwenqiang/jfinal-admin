package com.pintuan.controller.app.bonus;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.service.BonusService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询果实收取记录
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/queryBonusRecord")
@Before(CheckUserKeyInterceptor.class)
public class QueryBonusRecordController extends BaseProjectController {
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		String fro_usr_id = isNotNullAndGet(Fields.FRO_USR_ID, ErrCode.PAMAS_ERROR).toString();
		List<Record> bonusList = bonusService.findBonusRecord(fro_usr_id,usr_ide_key);
		setResp(Fields.RECORD_LIST, DBModelUtils.toMaps(bonusList, Record.class));
		returnJson();
	}

	

}
