package com.pintuan.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.util.ErrorCodeDict;
import com.pintuan.common.Fields;
import com.pintuan.model.RespBody;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.util.StrUtils;

/**
 * 返回的响应头设置
 * 
 */
public class HeaderResponseInterceptor implements Interceptor {

	public void intercept(Invocation inv) {
		try {
			handleRequestParam(inv);
			inv.invoke();
			handleResponseSuccess(inv);
		} catch (CoreException e) {
			e.printStackTrace();
			handleResponseFail(inv, e.getErrCode(),e.getErrMsg());
		}catch (Exception e) {
			e.printStackTrace();
			handleResponseFail(inv, ErrCode.SYSTEM_ERROR,ErrorCodeDict.getStr(ErrCode.SYSTEM_ERROR));
		}
		setResponseHeader(inv);
	}
    /**将req_message里的参数放到context
     * **/
	private void handleRequestParam(Invocation inv) {
		String reqMsg = inv.getController().getPara(Fields.REQ_MESSAGE);
		if (StrUtils.isNotEmpty(reqMsg)) {
			try {
			@SuppressWarnings("unchecked")
			Map<String, Object> parMap = (Map<String, Object>) JSONUtils.parse(reqMsg);
			((BaseProjectController) inv.getController()).setDataMap(parMap);
			}catch(Exception e) {
				e.printStackTrace();
				throw new CoreException(ErrCode.REQ_MSG_PARSE_ERROR);
			}
		}
	}

	private void handleResponseSuccess(Invocation inv) {
		RespBody respBody = ((BaseProjectController) inv.getController()).getRespBody();
		String jsonStr = respBody.getJsonData();
		if(((BaseProjectController) inv.getController()).isReTurnJson()) {
			System.out.println("respData:"+jsonStr);
		     inv.getController().renderJson(jsonStr);
		}
	}

	private void handleResponseFail(Invocation inv, String errCode,String errMsg) {
		RespBody respBody = ((BaseProjectController) inv.getController()).getRespBody();
		respBody.setFail(errCode,errMsg);
		String jsonStr = respBody.getJsonData();
		System.out.println("respData:"+jsonStr);
		inv.getController().renderJson(jsonStr);
	}

	private void setResponseHeader(Invocation inv) {
		HttpServletResponse response = inv.getController().getResponse();
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
	}
}
