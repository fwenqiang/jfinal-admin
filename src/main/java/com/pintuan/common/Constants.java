package com.pintuan.common;

import java.math.BigDecimal;

import com.supyuan.util.Config;

/**
 * 常量类
 * 
 * @author zjh 2018-3-24
 */
public class Constants {

	/**
	 * 底层调试标示
	 */
	public static final boolean DEBUG = Config.getToBoolean("CONSTANTS.DEBUG");

	/** url前缀 **/
	public static final String URL_PREFIX = Config.getStr("pintuan.urlPrefix");

	/** 控制台用户唯一Key */
	public static final String CONSOLE_USER_KEY = "CONSOLE_USER_KEY";

	/** 交易结果-1成功 */
	public static final String TRAN_SUCCESS = "1";

	/** 交易结果-0失败 */
	public static final String TRAN_FAIL = "0";

	/** */
	public static final String API_KEY = "";

	/** 应用ID */
	public static final String APP_ID = "wxd678efh567hg6787";

	/** 商户号 */
	public static final String MCH_ID = "1230000109";

	/** */
	public static final String WEIXIN_NOTIFY = "";

	/** 微信统一下单支付地址 */
	public static final String WEIXIN_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/** 返回成功 */
	public static final String REP_SUCCESS = "SUCCESS";

	/** 返回成功 */
	public static final BigDecimal BIG_DECIMAL_100 = new BigDecimal(100);

	/** 删除状态：2 */
	public static final String DELETE_STATE = "2";

	/** 可用状态：1 */
	public static final String ACCESS_STATE = "1";

	/** 不可用状态：0 */
	public static final String NEGATIVE_STATE = "0";

	/** 短信验证码类型：注册 */
	public static final String IDE_TYP_REGISTER = "1";

	/** 短信验证码类型：重设密码 */
	public static final String IDE_TYP_RESETPWD = "2";
	
	/** 短信验证码类型：更换手机 */
	public static final String IDE_TYP_UPDATE_PHONE = "3";

	/** 用户类型：1-管理员, */
	public static final String USER_TYPE_ADMIN = "1";

	/** 用户类型：2-普通用户 */
	public static final String USER_TYPE_COMMON = "2";

	/** 用户类型：3-商城用户 */
	public static final String USER_TYPE_CER = "3";

	/** 用户类型：4-第三方普通用户 */
	public static final String USER_TYPE_THD_COMMON = "4";

	/** 用户类型：5-第三方商城用户 */
	public static final String USER_TYPE_THD_CER = "5";
	//******************排单相关************************//

	/** 排单状态：1-已结束 */
	public static final String SDL_STATE_OVER = "1";

	/** 排单状态：2-进行中 */
	public static final String SDL_STATE_DOING = "2";

	/** 排单状态：3-未开始 */
	public static final String SDL_STATE_UNBEGIN = "3";
	
	/** 排单次序类型：1-上次排单 */
	public static final String SDL_SQN_TYP_LAST = "1";

	/** 排单次序类型：2-本次排单 */
	public static final String SDL_SQN_TYP_NOW = "2";

	/** 排单次序类型：3-下次排单 */
	public static final String SDL_SQN_TYP_NEXT = "3";

	// ******************ORDER相关************************//
	/** 流水状态:I-预下单 */
	public static final String JNL_STATE_INIT = "I";

	/** 流水状态:A-已作废（超时） */
	public static final String JNL_STATE_ABORT = "A";

	/** 流水状态:S-支付成功 */
	public static final String JNL_STATE_SUCCESS = "S";

	/** 流水状态:F-支付失败 */
	public static final String JNL_STATE_FAIL = "F";

	/** 流水状态:R-已退款 */
	public static final String JNL_STATE_REFUND = "R";

	// ******************订单相关************************//
	/** 订单状态:B-开始 */
	public static final String ORDER_STATE_BEGIN = "B";
	
	/** 订单状态:I-预下单 */
	public static final String ORDER_STATE_INIT = "I";

	/** 订单状态:A-已作废（超时） */
	public static final String ORDER_STATE_ABORT = "A";

	/** 订单状态:S-成功支付，待发货 */
	public static final String ORDER_STATE_SUCCESS = "S";

	/** 订单状态:T-已发货 */
	public static final String ORDER_STATE_TRANSFORM = "T";

	/** 订单状态:E-已签收 */
	public static final String ORDER_STATE_RECEIPT = "E";
	// ************************************************//

	/** 支付渠道:微信 */
	public static final String PAY_CHL_WX = "10";

	/** 支付渠道:阿里 */
	public static final String PAY_CHL_ALI = "20";

	/** 支付渠道:银联 */
	public static final String PAY_CHL_UNIPAY = "30";

	// ************************************************//

	/** 钱包类型：1-余额 */
	public static final String BONUS_CFG_TYP_1 = "1";

	/** 钱包类型：2-boass基金 */
	public static final String BONUS_CFG_TYP_2 = "2";

	/** 钱包类型：3-互助基金 */
	public static final String BONUS_CFG_TYP_3 = "3";

	/** 果实类型：S-已收取 */
	public static final String BONUS_STATE_SUCCESS = "S";
	
	//*************************************************//
	/** 商品配置状态：1-已结束； */
	public static final String PROCFG_STATE_OVER = "1";
	/** 商品配置状态：2-进行中； */
	public static final String PROCFG_STATE_DONG = "2";
	/** 商品配置状态：3-未开始 */
	public static final String PROCFG_STATE_UNBEGIN = "3";
	
	/** 用户默认头像 */
	public static final String DEFAULT_USER_TITLE_URL = "http://admin.iwjup.com/usr/usr_def.png";
	
	//*************************************************//
	/** 红包状态：1-可拆 */
	public static final String REDPACKET_STATE_INIT = "1";
	
	/** 红包状态：2-已拆 */
	public static final String REDPACKET_STATE_PICKED = "2";
	
	
	/** 提现状态：申请提现 */
	public static final String DEPOSIT_STATE_1 = "1";
	
	/** 提现状态：通过 */
	public static final String DEPOSIT_STATE_2 = "2";
	
	/** 提现状态：驳回 */
	public static final String DEPOSIT_STATE_3 = "3";
	/** 提现状态：提现成功 */
	public static final String DEPOSIT_STATE_4 = "4";
	
	public static final String SHEET1 = "sheet1";
	
	//public static final String HOST_URL="http://116.62.189.213:9999/jfinal-admin-1.0";

	public static final String HOST_URL="http://120.78.68.118";
}
