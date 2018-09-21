package com.pintuan.controller.app.user;

import java.util.List;

import org.apache.commons.lang.StringUtils;

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
import com.supyuan.util.StrUtils;

/**
 * 会员注册
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/memberRegister")
public class MemberRegisterController extends BaseProjectController {
	private UserService userService = new UserService();
	private CommonService commonService = new CommonService();
	private BonusService bonusService = new BonusService();
	public void index() throws CoreException {
		String thd_id = isNotNullAndGet(Fields.THD_ID,ErrCode.PAMAS_ERROR).toString();
        String phoneNo = isNotNullAndGet(Fields.PHONE_NO,ErrCode.PAMAS_ERROR).toString();
        String ideCde = isNotNullAndGet(Fields.IDENTIFY_CODE,ErrCode.PAMAS_ERROR).toString();
        String p_thd_id = getData(Fields.P_THD_ID);
        
        isNotBlank(Fields.USER_NAME,ErrCode.PAMAS_ERROR);
        isNotBlank(Fields.PASSWORD,ErrCode.PAMAS_ERROR);
        Assert.isEmpty(userService.findByThdId(thd_id),ErrCode.THD_ID_IS_EXIST);
        String p_id = null;
		if (StrUtils.isNotEmpty(p_thd_id)) {
			User parent = userService.findByThdId(p_thd_id);
			Assert.notEmpty(parent, ErrCode.P_THD_ID_ERROR);
			p_id = parent.getStr(Fields.USER_ID);
			this.setData(Fields.P_ID, p_id);
		}
		
        commonService.validateIdentifyCode(phoneNo, ideCde, "1");  
        User user = userService.add(getContext());   
        // 添加钱包和基金
        BonusCfg  myPaket = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_1);
        BonusCfg  bossFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_2);
        BonusCfg  helpFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_3);
        setResp(Fields.USER_IDENTIFY_KEY, user.getStr(Fields.USER_ID));
        returnJson();
	}	
}
