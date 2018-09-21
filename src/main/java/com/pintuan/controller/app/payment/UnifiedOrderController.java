package com.pintuan.controller.app.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.common.WXConstants;
import com.pintuan.model.IdentifyCode;
import com.pintuan.model.Jnl;
import com.pintuan.model.PosterBoard;
import com.pintuan.service.CommonService;
import com.pintuan.service.JnlService;
import com.pintuan.service.PosterBoardService;
import com.pintuan.service.ProductService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.pintuan.util.WXRequestUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 统一下单
 *                                      
 *                                             
 * @author zjh 2018-4-29
 */
@ControllerBind(controllerKey = "/pintuan/app/unifiedOrder")
public class UnifiedOrderController extends BaseProjectController {
	
	private JnlService jnlService = new JnlService();
	private ProductService productService = new ProductService();
	
	public void index() throws CoreException {
		//String pro_id = isNotNullAndGet(Fields.PRO_ID, ErrCode.PRO_ID_IS_NULL).toString();
		//String usr_id = isNotNullAndGet(Fields.USER_IDENTIFY_KEY, ErrCode.USER_UNEXIST).toString();
		/*String body = "body";
		BigDecimal tol_fee = new BigDecimal("0.01");
		Jnl jnl = jnlService.initJnl(tol_fee);
		Map<String,String> res = WXRequestUtil.SendPayment(body, jnl.getStr(Fields.JNL_ID), tol_fee, null);
		if(WXConstants.REP_SUCCESS.equals(res.get(Fields.RETURN_CODE))) {
    		System.out.println("交易成功："+res);
    		setResp("appid", res.get("appid"));
    		setResp("sign", res.get("sign"));
    		setResp("partnerid", res.get("mch_id"));
    		setResp("prepayid", res.get("prepay_id"));
    		setResp("noncestr", res.get("nonce_str"));
    		setResp("timestamp", getTimestamp());
    	}else {
    		System.out.println("交易失败:"+res);
    		jnlService.updateJnlState(jnl, Constants.JNL_STATE_ABORT);
    		throw new CoreException(ErrCode.WX_UNIFIED_FAIL);
    	}
        returnJson();*/
	}
	
	//时间戳
	private String getTimestamp() {
		String t = System.currentTimeMillis()+"";
    	return t.substring(0, 10);
	}
	
	
}
