package com.pintuan.model.bean;

import java.util.Map;

import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.WXConstants;

public class WXResult {
	private Map<String, String> data;
	private String errCode;
	private String errMsg;
	private String state;

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public static WXResult build(Map<String, String> res) {
		WXResult result = new WXResult();
		result.setData(res);
		if (result.tranSuccess()) {
			if (result.isSuccess()) { // 业务成功
				switch (result.get("trade_state")) {
				case "NOTPAY":
					result.setState(Constants.ORDER_STATE_INIT);
					break;
				case "CLOSED":
					result.setState(Constants.ORDER_STATE_ABORT);
					break;
				case "SUCCESS":
					result.setState(Constants.ORDER_STATE_SUCCESS);
					break;
				case "REVOKED":
					result.setState("R");
					break;
				case "USERPAYING":
					result.setState(Constants.ORDER_STATE_INIT);
					break;
				case "PAYERROR":
					result.setState(Constants.ORDER_STATE_ABORT);
					break;
				case "REFUND":
					result.setState("R");
					break;
				}
			} else {
				result.setState("F");
			}
		} else { // 通讯失败
			result.setErrCode(ErrCode.TRAN_FAIL);
			result.setErrMsg("transfer fail");
		}
		// result.setState("S"); //TODO:测试
		return result;
	}

	/**
	 * 是否成功
	 **/
	public boolean isSuccess() {
		// return true; //TODO:测试
		return this.data != null && WXConstants.REP_SUCCESS.equals(this.data.get(WXConstants.RETURN_CODE))
				&& WXConstants.REP_SUCCESS.equals(this.data.get(WXConstants.RESULT_CODE));
	}

	/**
	 * 通讯是否成功
	 **/
	public boolean tranSuccess() {
		return this.data != null && WXConstants.REP_SUCCESS.equals(this.data.get(WXConstants.RETURN_CODE));
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Map<String, String> getData() {
		return data;
	}

	public String get(String key) {
		if (this.data == null)
			return null;
		return this.data.get(key);
	}

}
