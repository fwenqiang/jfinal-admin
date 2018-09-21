package com.pintuan.controller.app.thirdCallback;

import java.util.Map;

import com.pintuan.base.CoreException;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.OAuth;
import com.pintuan.service.OAuthService;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 微信网页登录1. 
 * 第三方发起微信授权登录请求，
 * 微信用户允许授权第三方应用后，
 * 微信会拉起应用或重定向到第三方网站，
 * 并且带上授权临时票据code参数；
<br>
返回说明
1.用户允许授权后，将会重定向到redirect_uri的网址上，并且带上code和state参数:<br>
redirect_uri?code=CODE&state=STATE
<br>
2.若用户禁止授权，则重定向后不会带上code参数，仅会带上state参数:<br>
redirect_uri?state=STATE

 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/wxsnsoauth")
public class WXSnsOAuthCallBackController extends BaseProjectController {
	private static final String path = "/pages/pintuan/weixin_";
	private OAuthService oauthService = new OAuthService();
	
	public void index() throws CoreException {
        //boolean isGet = request.getMethod().toLowerCase().equals("get");
		   
            String code =getPara(WeixinFields.CODE);
            System.out.println("code="+code);
            String state = getPara(WeixinFields.STATE); //父ID
            Map<String,Object> repMap = WXRequestUtil.getGZAccessToken(code);
            if(repMap.get(WeixinFields.ERRCODE)!=null) {
                System.out.println("error");
                return ;
            }
            String access_token = (String)repMap.get(WeixinFields.ACCESS_TOKEN);
            String openid = (String)repMap.get(WeixinFields.OPENID);
            Map<String,Object> userInfo = WXRequestUtil.getGZUserInfo(access_token, openid);
            if(userInfo.get(WeixinFields.ERRCODE)!=null) {
                System.out.println("error");
                //重定向
                render(path + "login_result.html");
                return ;
            }
            OAuth oauth = oauthService.findByOpenId(openid);
            if(oauth==null) {
                oauthService.add(userInfo, state);
            }
            //重定向
            render(path + "login_result.html");
            
	}	
	
	
}
