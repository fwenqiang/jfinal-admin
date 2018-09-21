package com.pintuan.app.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.controller.app.user.UserLoginController;
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
public class UserLoginTest{
	
	public static void main(String[] args) throws CoreException {
		String value = "{\"amt_typ\":\"1990\",\"pro_typ\":\"沉香\",\"n_page\":\"0\",\"n_size\":\"4\"}";//"{\"usr_ide_key\":\"cd0b3a1a856641b2b35765938ed452c6\"}";
		String decode = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=wxd678efh567hg6787&biz_content=%7B%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22da07c375cdc84f16877bdd4e74daa3ac%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22App%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95Java%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.baidu.com&sign=l1ewrcFjPP9doG%2FnhYW89QUb3gBtypNBkX5H3%2FyeOgL3Z5RZKRoQyW1u5pUvbOQ2y2TiPIBzS4Elyz4LVYveqwnyxpO6E3AqZbDrAlBbKeanBrtmqrFwOtuwK4b3U0gW3m6tZABZsDCaYeIH6T8bfkllUVfhiqsfH0LP6NFU2E7lwv4ypplnT90rmtFFrbmlGY8ONGw0RZZQUioUYg3M8KpNjunnzwU8Hw5SToMpAjFa7aw3ZlEfQ%2BbVW3rYEMu%2BXBTJap8PoHVqGix%2B2rozu6CgPMmrC48LvPXAmNdawaruij83kKNJjCkUszlifIl9GPvMrRVsCl2tObJ6K1vvYA%3D%3D&sign_type=RSA2&timestamp=2018-04-22+20%3A09%3A04&version=1.0";
		try {
			System.out.println(URLEncoder.encode(value, "utf-8"));
			//System.out.println(URLDecoder.decode(decode, "utf-8"));
			//%7B%22pho_no%22%3A%2218825180355%22%2C%22pwd%22%3A%22MTIzNDU2%22%2C%22ide_cde%22%3A%2223%22%7D
			
			//%7B%22pho_no%22%3A%2218825180355%22%2C%22ide_cde_typ%22%3A%221%22%7D
			//"{\"pho_no\":\"18825180300\",\"ide_cde_typ\":\"1\"}"
			//%7B%22pho_no%22%3A%2218825180300%22%2C%22ide_cde_typ%22%3A%221%22%7D
			
			//"{\"pho_no\":\"18825180300\",\"ide_cde\":\"1234\",\"pwd\":\"MTIzNDU2Nw==\"}"
			//%7B%22pho_no%22%3A%2218825180300%22%2C%22ide_cde%22%3A%221234%22%2C%22pwd%22%3A%22MTIzNDU2Nw%3D%3D%22%7D
			
			//"{\"pho_no\":\"18825180301\",\"ide_cde\":\"1234\",\"ide_cde_typ\":\"1\"}"
			// %7B%22pho_no%22%3A%2218825180301%22%2C%22ide_cde%22%3A%221234%22%2C%22ide_cde_typ%22%3A%221%22%7D
			
			//"{\"usr_ide_key\":\"cd0b3a1a856641b2b35765938ed452c6\"}"
			//%7B%22usr_ide_key%22%3A%22cd0b3a1a856641b2b35765938ed452c6%22%7D
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
/*		UserLoginController controller = new UserLoginController();
		controller.setData(Fields.PHONE_NO, "18825180300");
		controller.setData(Fields.PASSWORD, "MTIzNDU2");
		controller.index();
		System.out.println(controller.getRespBody().getJsonData());*/
	}	
}
