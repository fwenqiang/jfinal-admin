package com.pintuan.model;

import java.util.HashMap;
import java.util.Map;


import com.alibaba.druid.support.json.JSONUtils;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.util.ErrorCodeDict;

public class RespBody {

private Map<String,Object> rspBody = new HashMap<String,Object>();
private Map<String,Object> rspHead = new HashMap<String,Object>();
private Map<String,Object> response = new HashMap<String,Object>();
private int result = 1;  // 0-失败，1成功

public RespBody() {
		rspHead.put(Fields.TRAN_SUCCESS, Constants.TRAN_SUCCESS);
}

public void setFail(String errCode,String errMsg) {
	rspHead.put(Fields.TRAN_SUCCESS, Constants.TRAN_FAIL);
	rspHead.put(Fields.ERROR_CODE, errCode);
	rspHead.put(Fields.ERROR_MESSAGE, errMsg);//ErrorCodeDict.getStr(errCode));
	result = 0;
}
public void setData(String key,Object value) {
	rspBody.put(key, value);
}

public void setDataMap(Map<String,Object> data) {
	this.rspBody.putAll(data);
}

public String getJsonData() {
	if(this.result==0) {
		response.put(Fields.RSP_HEAD, this.rspHead);
		return JSONUtils.toJSONString(response);
	}else {
		response.put(Fields.RSP_HEAD, this.rspHead);
		response.put(Fields.RSP_BODY, this.rspBody.isEmpty()?null:this.rspBody);
		return JSONUtils.toJSONString(response);
	}	
}

}