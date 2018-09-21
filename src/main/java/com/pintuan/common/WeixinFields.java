package com.pintuan.common;

/**
 * 微信域
 * 
 * @author zjh 2018-3-24
 */
public class WeixinFields {

	
	/** 签名 */
	public static final String SIGNATURE = "signature";
	
	/**  时间戳 */
	public static final String TIMESTAMP = "timestamp";
	
	/** 随机数 */
	public static final String NONCE = "nonce";
	
	/** 随机字符串 */
	public static final String ECHOSTR = "echostr";
	
	/** 授权临时票据code参数 */
	public static final String CODE = "code";
	
	/** 用于保持请求和回调的状态，授权请求后原样带回给第三方。 */
	public static final String STATE = "state";
	
	/** 用于保持请求和回调的状态，接口调用凭证 */
	public static final String ACCESS_TOKEN = "access_token";
	
	/** access_token接口调用凭证超时时间，单位（秒） */
	public static final String EXPIRES_IN = "expires_in";
	
	/** 用户刷新access_token */
	public static final String REFRESH_TOKEN = "refresh_token";
	
	/** 授权用户唯一标识 */
	public static final String OPENID = "openid";
	
	/** 用户授权的作用域，使用逗号（,）分隔 */
	public static final String SCOPE = "scope";
	
	/** 微信网页授权登录错误码 */
	public static final String ERRCODE = "errcode";
	
	/** 应用唯一标识 */
	public static final String APPID  = "appid";
	
	/** 普通用户昵称 */
	public static final String NICKNAME  = "nickname";
	
	/** 普通用户性别，1为男性，2为女性 */
	public static final String SEX  = "sex";
	
	/** 普通用户个人资料填写的省份 */
	public static final String PROVINCE  = "province";
	
	/** 普通用户个人资料填写的城市 */
	public static final String CITY  = "city";
	
	/** 国家，如中国为CN */
	public static final String COUNTRY  = "country";
	
	/** 用户头像 */
	public static final String HEADIMGURL  = "headimgurl";
	
	/** 用户特权信息 */
	public static final String PRIVILEGE  = "privilege";
	
	/** 用户统一标识 */
	public static final String UNIONID  = "unionid";
	
	/** 移动端-扩展字段值*/
	public static final String PACKAGE = "package";
	
	/** 移动端-商户号*/
	public static final String PARTNERID = "partnerid";
	
	/** 移动端-预支付交易会话ID*/
	public static final String PREPAYID = "prepayid";
	
	/** 移动端-随机字符串*/
	public static final String NONCESTR = "noncestr";
	
	/** 签名 */
	public static final String SIGN = "sign";
	
	/** 微信商户号 */
	public static final String MCH_ID = "mch_id";
	
}
