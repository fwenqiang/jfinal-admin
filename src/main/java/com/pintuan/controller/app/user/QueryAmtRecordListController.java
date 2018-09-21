package com.pintuan.controller.app.user;

import java.util.List;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.AmtRecord;
import com.pintuan.service.UserService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询收支列表
 * 
 * @author zjh 2018-6-9
 */
@ControllerBind(controllerKey = "/pintuan/app/queryAmtRecordList")
public class QueryAmtRecordListController extends BaseProjectController {
	private UserService userService = new UserService();

	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		List<AmtRecord> amtRecordList = userService.findAmtRecordList(usr_ide_key);
        setResp(Fields.AMT_RECORD_LIST,DBModelUtils.toMaps(amtRecordList));
        returnJson();
	}	
	
}
