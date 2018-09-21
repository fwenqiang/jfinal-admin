package com.pintuan.base;

import com.pintuan.util.ErrorCodeDict;

/**
 * 核心异常
 * 
 * @author zjh 2018-3-24
 */
public class CoreException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String errCode;
	private String errMsg;

	public CoreException(){
        super();
    }
    public CoreException(String errCode){
        super(errCode+"["+ErrorCodeDict.getStr(errCode)+"]");
        this.errCode = errCode;
        this.errMsg = ErrorCodeDict.getStr(errCode);
    }
    public CoreException(String errCode,String msg){
        super(errCode+"["+msg+"]");
        this.errCode = errCode;
        this.errMsg = msg;
    }
    
    public CoreException(String errCode,String msg,Throwable cause){
        super(errCode+"["+msg+"]",cause);
        this.errCode = errCode;
        this.errMsg = msg;
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
    
	

}
