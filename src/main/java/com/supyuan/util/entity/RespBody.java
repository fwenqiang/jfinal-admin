package com.supyuan.util.entity;

import javax.servlet.http.HttpServletResponse;

public class RespBody {

private boolean result;
private int code=200;
private String msg;
private int resultcode; 
private Object data;

public RespBody() {
}

public RespBody(HttpServletResponse response) {
	// HttpServletResponse response = getResponse();
	 response.setHeader("Access-Control-Allow-Origin","*");    
	// 响应类型    
	 response.setHeader("Access-Control-Allow-Methods","POST");    
	// 响应头设置    
	response.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
}
public boolean isResult() {
	return result;
}
public void setResult(boolean result) {
	this.result = result;
}
public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public int getResultcode() {
	return resultcode;
}
public void setResultcode(int resultcode) {
	this.resultcode = resultcode;
}
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}

}