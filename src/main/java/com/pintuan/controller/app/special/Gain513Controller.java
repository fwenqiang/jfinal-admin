package com.pintuan.controller.app.special;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Img;
import com.pintuan.model.Product;
import com.pintuan.model.Special;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.ImageService;
import com.pintuan.service.ProductService;
import com.pintuan.service.SpecialService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 收获513特色业务
 * 
 * @author zjh 2018-4-26
 */
@ControllerBind(controllerKey = "/pintuan/app/gain513")
@Before(CheckUserKeyInterceptor.class)   
public class Gain513Controller extends BaseProjectController {
	private SpecialService specialService = new SpecialService();
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
        String spe_id = isNotNullAndGet(Fields.SPE_ID, ErrCode.PAMAS_ERROR).toString();
		Special special = specialService.findById(spe_id,"I");
		if(special!=null) {
			bonusService.updatePaket(special.getBigDecimal(Fields.AMT), user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_1);
			specialService.updateState(spe_id);
		}else {
			throw new CoreException(ErrCode.DOUBLE_GAIN);
		}
	
		returnJson();
	}

}
