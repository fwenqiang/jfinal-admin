package com.pintuan.common;

/**
 * 错误码常量
 * 
 * @author zjh 2018-3-24
 */
public class ErrCode {
	
	/** 后台系统级错误*/
	public static final String SYSTEM_ERROR = "PINTUAN001000";
	
	/** 密码或账户出错*/
	public static final String LOGIN_ERROR = "PINTUAN001001";
	
	/** 手机号为空*/
	public static final String PHONE_NO_IS_NULL = "PINTUAN001002";
	
	/** 密码为空*/
	public static final String PASSWORD_IS_NULL = "PINTUAN001003";
	
	/** 验证码为空*/
	public static final String IDENTIFY_CODE_IS_NULL = "PINTUAN001004";
	
	/** 验证码错误*/
	public static final String IDENTIFY_CODE_ERROR = "PINTUAN001005";
	
	/** 手机号已被注册*/
	public static final String PHONE_EXIST = "PINTUAN001006";
	
	/** 请求数据解析出错*/
	public static final String REQ_MSG_PARSE_ERROR = "PINTUAN001007";
	
	/** 验证码类型为空*/
	public static final String IDENTIFY_CODE_TYPE_IS_NULL = "PINTUAN001008";
	
	/** 验证码ID为空*/
	public static final String IDENTIFY_ID_IS_NULL = "PINTUAN001009";
	
	/** 新密码为空*/
	public static final String NEW_PWD_IS_NULL = "PINTUAN001010";
	
	/** 验证码ID错误*/
	public static final String IDENTIFY_ID_ERROR = "PINTUAN001011";
	
	/** 发送短信失败*/
	public static final String SEND_SMS_ERROR = "PINTUAN001012";
	
	/** 用户未登录*/
	public static final String WITHOUT_LOGIN = "PINTUAN001013";
	
	/** 用户不存在*/
	public static final String USER_UNEXIST = "PINTUAN001014";
	
	/** 微信登录code为空*/
	public static final String CODE_IS_NULL = "PINTUAN001015";
	
	/** 微信登录授权失败*/
	public static final String WEIXIN_OAUTH_ERROR = "PINTUAN001016";
	
	/** 广告位置为空*/
	public static final String LOCATION_IS_NULL = "PINTUAN001017";
	
	/** 价格类型为空*/
	public static final String AMT_TYP_IS_NULL = "PINTUAN001018";
	
	/** 排单次序类型为空*/
	public static final String SDL_SQN_TYP_IS_NULL = "PINTUAN001019";
	
	/** 商品类型为空*/
	public static final String PRO_TYP_IS_NULL = "PINTUAN001020";
	
	/** 商品ID为空*/
	public static final String PRO_ID_IS_NULL = "PINTUAN001021";
	
	
	
	/** 今天已签到*/
	public static final String TODAY_ALREADY_SIGN_IN = "PINTUAN001022";
	
	/** 广告ID为空*/
	public static final String BOR_ID_IS_NULL = "PINTUAN001023";
	
	/** 阿里统一下单失败*/
	public static final String ALI_UNIFIED_FAIL = "PINTUAN001024";
	
	/** 微信统一下单失败*/
	public static final String WX_UNIFIED_FAIL = "PINTUAN001025";
	
	/** 商品不存在*/
	public static final String PRO_NOT_EXIST = "PINTUAN001026";
	
	/** 订单编号为空*/
	public static final String ORD_ID_IS_NULL = "PINTUAN001027";
	
	/** 订单不存在*/
	public static final String ORDER_NOT_EXIST = "PINTUAN001028";
	
	/** 支付渠道为空*/
	public static final String PAY_CHL_IS_NULL = "PINTUAN001029";
	
	/** 发起渠道为空*/
	public static final String REQ_CHL_IS_NULL = "PINTUAN001030";
	
	/** 支付渠道有误*/
	public static final String PAY_CHL_ERROR = "PINTUAN001031";
	
	/** 参数有误*/
	public static final String PAMAS_ERROR = "PINTUAN001032";
	
	/** 通讯失败*/
	public static final String TRAN_FAIL = "PINTUAN001033";
	
	/** 微信登录获用户取信息失败*/
	public static final String WEIXIN_GETUSERINFO_ERROR = "PINTUAN001034";
	
	/** 图片类型为空*/
	public static final String IMG_TYP_IS_NULL = "PINTUAN001035";
	
	/** 同类商品重复下单*/
	public static final String SAME_TYPE_ORDER_DUPLICATE = "PINTUAN001036";
	
	/** 果实已摘取*/
	public static final String FRUIT_ALREADY_GAIN = "PINTUAN001037";
	
	/** sqn_no的取值出错（1-7）*/
	public static final String SQN_NO_ERROR = "PINTUAN001038";
	
	/** 旧密码为空*/
	public static final String OLD_PWD_IS_NULL = "PINTUAN001039";
	
	/** 密码错误*/
	public static final String PASSWORD_ERROR = "PINTUAN001040";
	
	/** 重复领取*/
	public static final String DOUBLE_GAIN = "PINTUAN001041";
	
	/** 订单状态不是未支付*/
	public static final String ORDER_STATE_NOT_INIT = "PINTUAN001042";
	
	/** 账号已经存在*/
	public static final String THD_ID_IS_EXIST = "PINTUAN001043";
		
	/** 上级推荐人账号错误*/
	public static final String P_THD_ID_ERROR = "PINTUAN001044";
	
	/** 上级推荐人账号不可以是自己的账号*/
	public static final String P_THD_ID_CANT_BE_SAME_WITHSELF = "PINTUAN001045";
	
	/** 账号不存在*/
	public static final String THD_ID_UN_EXIST = "PINTUAN001046";
	
	/** 推荐账号的徒弟已达到4个*/
	public static final String CHILDREN_OVER_4 = "PINTUAN001047";
	
	/** 创建分享图片失败*/
	public static final String CREATE_SHARE_IMG_ERR = "PINTUAN001048";
	
	/** 需要先下单1990才能下单800*/
	public static final String NEED_1990_FIRST = "PINTUAN001049";
	
	/** 需要拿满7次分红*/
	public static final String NEED_HAVE_7_SCHDULING = "PINTUAN001050";
	
	/** 该订单已无效，请重新下单*/
	public static final String ABORT_ORDER = "PINTUAN001051";
	
	/** 还无法摘取该果实*/
	public static final String FRUIT_CANNOT_BE_GAIN = "PINTUAN001052";
	
	/** 状态不正确*/
	public static final String STATE_ERROR = "PINTUAN001053";
	
	/** 还存在等待处理的订单 */
	public static final String INIT_ORDER_IS_NOT_NULL = "PINTUAN001054";
	
	/** 流水不存在*/
	public static final String JNL_UN_EXIST = "PINTUAN001055";
	
	/** 红包不存在*/
	public static final String RED_PACKET_UN_EXIST = "PINTUAN001056";
	
	/** 有提现处理中*/
	public static final String DEPOSIT_DEALING = "PINTUAN001057";
	
	/** 无法提现*/
	public static final String CANNOT_WITHDROW_DEPOSIT = "PINTUAN001058";
	
	/** 提现金额超出余额*/
	public static final String DEPOSIT_AMT_ERROR = "PINTUAN001059";
	
	/** 没有上传文件*/
	public static final String DONT_UPLOAD_FILE = "PINTUAN001060";
	
	/** 发票已存在*/
	public static final String INVOICE_EXIST = "PINTUAN001061";
	
	/** 手机号码错误*/
	public static final String PHO_NO_ERROR = "PINTUAN001062";
	
	/** 数量超过范围*/
	public static final String SIZE_OVERFLOW = "PINTUAN001063";
	
	/** 不存在*/
	public static final String UN_EXIST = "PINTUAN001064";
	
	/**
	 * 转账金额大于余额
	 */
	public static final String TRANSFER_AMT_ERR = "PINTUAN001065";

	/**
	 * 用户状态异常
	 */
	public static final String LOGIN_USER_STATE_ERROR = "PINTUAN001066";
	
}
