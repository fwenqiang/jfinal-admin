package com.pintuan.controller.app.user;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.CommonService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 用户注册
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/userRegister")
public class UserRegisterController extends BaseProjectController {
	private UserService userService = new UserService();
	private CommonService commonService = new CommonService();
	private BonusService bonusService = new BonusService();
	public void index() throws CoreException {
		String phoneNo = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PHONE_NO_IS_NULL).toString();
        String ideCde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.IDENTIFY_CODE_IS_NULL).toString();
        isNotBlank(Fields.PASSWORD,ErrCode.PASSWORD_IS_NULL);
        commonService.validateIdentifyCode(phoneNo, ideCde, "1");
        List<Record> userList = userService.findUserByPhone(phoneNo);
        Assert.isEmpty(userList, ErrCode.PHONE_EXIST);        
        User user = userService.add(getContext());   
        // 添加钱包和基金
        BonusCfg  myPaket = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_1);
        BonusCfg  bossFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_2);
        BonusCfg  helpFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_3);
        setResp(Fields.USER_IDENTIFY_KEY, user.getStr(Fields.USER_ID));
        returnJson();
	}	
}
