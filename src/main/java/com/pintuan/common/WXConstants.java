package com.pintuan.common;

import java.math.BigDecimal;

import com.supyuan.util.Config;

/**
 * 常量类
 * 
 * @author zjh 2018-3-24
 */
public class WXConstants {

	/**url前缀**/
	public static final String URL_PREFIX = Config.getStr("pintuan.urlPrefix");
	
	/** 交易结果-1成功*/
	public static final String TRAN_SUCCESS = "1";
	
	/** 交易结果-0失败*/
	public static final String TRAN_FAIL = "0";
	
	/** 商户秘钥*/
	public static final String API_KEY = "iwjyp1688chenhaoWJYP888jujun6941";//"35ed389577c76b857eea90cee34bb067";
	
	/**应用ID */
	public static final String APP_ID = "wx7ec3e52e3d74b1cb";
	/**app应用秘钥 */
	public static final String APP_SECRET = "170d2e902c339ef8a2638107cb6da0e3";  	
	
	/**APP应用ID */
	public static final String APP_APP_ID = "wx7ec3e52e3d74b1cb";//"wxf83c6e9b04004342";
	/**APP端应用秘钥 */
	public static final String APP_APP_SECRET = "170d2e902c339ef8a2638107cb6da0e3";//"dc3d3ec6eb769a6654abe87f95baa577";  
	
	/**公众号应用ID */
	public static final String GZ_APP_ID = "wx278d0cececc6696e";
	/**公众号应用秘钥 */
	public static final String GZ_APP_SECRET = "0f6765de7ef5b3a06c6b3fb5b0ecc482";  
	
	
	/** 商户号 */
	public static final String MCH_ID = "1502864511";
	
	/** 移动端扩展字段值*/
	public static final String PACKAGE_VALUE = "Sign=WXPay";
	
	/**微信后台通知地址 */
	public static final String WEIXIN_NOTIFY = "http://admin.iwjup.com/jfinal-admin-1.0/pintuan/app/wxpaycallback";
	
	/**微信统一下单支付地址 */
	public static final String WEIXIN_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**微信订单查询地址 */
	public static final String WEIXIN_ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/** 返回成功*/
	public static final String REP_SUCCESS = "SUCCESS";
	
	/** 微信通讯返回码*/
	public static final String RETURN_CODE = "return_code";
	
	/** 微信业务结果返回码*/
	public static final String RESULT_CODE = "result_code";
	
	/**
	 * 交易状态
	 * SUCCESS—支付成功
	 * 
	 * REFUND—转入退款
	 * 
	 * NOTPAY—未支付
	 * 
	 * CLOSED—已关闭
	 * 
	 * REVOKED—已撤销（刷卡支付）
	 * 
	 * USERPAYING--用户支付中
	 * 
	 * PAYERROR--支付失败(其他原因，如银行返回失败)
	 */
	public static final String TRADE_STATE = "trade_state";
	
	public static final String TRADE_STATE_SUCCESS = "SUCCESS";
	public static final String TRADE_STATE_REFUND = "REFUND";
	public static final String TRADE_STATE_NOTPAY = "NOTPAY";
	public static final String TRADE_STATE_REVOKED = "REVOKED";
	public static final String TRADE_STATE_USERPAYING = "USERPAYING";
	public static final String TRADE_STATE_PAYERROR = "PAYERROR";
	
	
	
	
}
