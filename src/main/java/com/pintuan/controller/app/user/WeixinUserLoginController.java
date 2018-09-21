package com.pintuan.controller.app.user;

import java.util.Map;

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.OAuth;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.OAuthService;
import com.pintuan.service.UserService;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 微信用户登录
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/xeixinUserLogin")
public class WeixinUserLoginController extends BaseProjectController {
	private UserService userService = new UserService();
	private OAuthService oauthService = new OAuthService();
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		String code = isNotNullAndGet(WeixinFields.CODE,ErrCode.CODE_IS_NULL).toString();
		
        Map<String,Object> repMap = WXRequestUtil.getAccessToken(code);
        if(repMap.get(WeixinFields.ERRCODE)!=null) {
        	throw new CoreException(ErrCode.WEIXIN_OAUTH_ERROR);
        }
        String access_token = (String)repMap.get(WeixinFields.ACCESS_TOKEN);
        String openid = (String)repMap.get(WeixinFields.OPENID);
        Map<String,Object> userInfo = WXRequestUtil.getUserInfo(access_token, openid);
        if(userInfo.get(WeixinFields.ERRCODE)!=null) {
        	throw new CoreException(ErrCode.WEIXIN_GETUSERINFO_ERROR);
        }
        User user = userService.findByOpenid(openid);
        if(user!=null) {
        	setResp(Fields.USER_IDENTIFY_KEY,user.get(Fields.USER_ID));
            returnJson();
            return;
        }
        user = new User();
        OAuth oauth = oauthService.find(userInfo);
        if(oauth!=null) {
        	long count = userService.findChildrenUserCount((String)oauth.get(Fields.P_ID));
        	if(count<4) {  //只能有4个徒弟
        	   user.set(Fields.P_ID, oauth.get(Fields.P_ID));
        	}
        	oauthService.update(oauth);
        }
        userService.add(userInfo,user);
        // 添加钱包和基金
        BonusCfg  myPaket = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_1);
        BonusCfg  bossFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_2);
        BonusCfg  helpFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_3);
        setResp(Fields.USER_IDENTIFY_KEY,user.get(Fields.USER_ID));
        returnJson();
        
	}	
	
}
