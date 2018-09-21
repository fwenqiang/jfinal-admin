package com.pintuan.common;

import java.math.BigDecimal;

import com.supyuan.util.Config;

/**
 * 阿里常量类
 * 
 * @author zjh 2018-3-24
 */
public class AliConstants {
	
	/**阿里统一下单支付地址 */
	public static final String ALI_UNIFIEDORDER_URL = "https://openapi.alipay.com/gateway.do";
	
	/**应用ID */
	public static final String ALI_APP_ID = "wxd678efh567hg6787";
	
	/**开发者应用私钥，由开发者自己生成 */
	public static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCiTPx5tlAHZnWimODyMQI4sq8HR0A1BhrFysihXWj8OKU w4Y5mANDiPCdLyQb1Sx4D1GCk6Vy5gjapxZKFamiFddU5M4pdNMA0KNBOd0VQYNHb1AkCVURTnAWMnQ87p5H8Yso4LDeQZW UbYfvVbG87S03LLWp0e7fA//0wi//V4OrVPgp5ddD2oHQ1SpNK3WTzFGELjw/HnVrcrqkes1pNthfug57DQH3p5FaFub +vS1EqLuu5hLrJQnzM3SMZyLcuwf3ANhZyKX +ZaiWfwcGJxg/VyhHkH1O1+9rP1YtdlM54P0a49RkdiSSJi10z5+rCK9YgTTIE +SCDQWTDvgIlAgMBAAECggEAQWniAHQ4gSMMmoIfd73E9XEfFkHjYPua4sk +X7s0SyPRgIgISphdmDFCbUsSoN6rtENuyEi99I7E1VwqF9exVMkPLUTWqYZPSTaB7n4n06OSMZWeI/CV31Ts76WFtDdvoV d3xXgc1Pfo7kZpp7VSh4VsT014sIYEo6cJNv3WGS8tU +3b0/ZHm6jdbdOvFDRmHke9ekfdT9cHkp2fEd97A8QTTlprdPvx3qhyYrvGW5ArEFdX +CBrG45AMD4sWNN1cdwgMlEOpknA0jV90VP9LsWz6GyQuXsZGFfj69kPFLrbINT++5FRpl8GUiEnxbyr4Q +dEE15a8kLsLxxgSwT6QKBgQDu6qR8B7S7jloZnKPG8UH3juj0bp014jWthZypnLyzBSHinXr8XR8+KvE9AkyjBR3E7Y2BF Ga6uadorEGMN0BVmUanBTGzoVUn2pYl+nS6yNy9ZY21cOBGAgFQYTdHdP96g7RlKoVFCsmO+gmhWIuVNKR45rS +esp3RakgiG10kwKBgQCt5+TdQpLHvNJe828PuozFJ2c5pzl1VigL1sIe/dvHcsQ//ibf7a +ETNDHzMDH2SioM9iFdS15z1rr0u8WpvzuuRMI5p5DdZYwICaDvMbANYPSlSM/h4ECstNTNelZO4vF7ycTj47jfHrN5c7S1 lYE0o8T4MY8+2DZuKQWlj9ZZwKBgQDHzrKbC/LJfIa7rrWFlpUdzYXjjnKp8rIDmZ9odi2V5i/tjtihH9wWKOGxrS +ckpXP8P+LuR0lAkkQkCcNddO3AsYEvqJPYRiPiQ5TWr+uyndZagn2eR0HT3v71iHd/ +n9cxI2HTZxhFEG4vOU3TF3Khx3BDKarqSO1VJZY99uQwKBgQCSUnlIiclCced3qM +bplTnVb4OzlsUFQfg3jO7K9GOQUxKo6dF5pE1egR4+BiEjn0c +XKG6A75XLSdk39DygErQ2F2rcQf8oIJwCNn2KBJ/T0LJBziSTsNcDVK4COUxvtV8WEpnjKVyZjyQRgCYgFCUX +9M/TYPpJA3pCByK1IpQKBgQCbop0bm5dBbuhwmiEcXt1GwdOn26WsVe3rJnV4pME/UwjSBg +lsknrLyGsLnJBOqIr0Bq/QE/TZ4rFDe9+0v9cgoMY7NUkNT74yD+3zeEF2+gPxO9/PXc1/4+LWJhOLLHNP2s +6Wxt6bpmS1NuDTqpGtJeCADoQ1HybSYAKjbB9g==";
	
	/**支付宝公钥，由支付宝生成 */
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAokz8ebZQB2Z1opjg8jECOLKvB0dANQYaxcrIoV1o/DilMOGOZgD Q4jwnS8kG9UseA9RgpOlcuYI2qcWShWpohXXVOTOKXTTANCjQTndFUGDR29QJAlVEU5wFjJ0PO6eR/GLKOCw3kGVlG2H71W xvO0tNyy1qdHu3wP/9MIv/1eDq1T4KeXXQ9qB0NUqTSt1k8xRhC48Px51a3K6pHrNaTbYX7oOew0B96eRWhbm/r0tRKi7ru YS6yUJ8zN0jGci3LsH9wDYWcil/mWoln8HBicYP1coR5B9Ttfvaz9WLXZTOeD";
	
	/**请求和签名使用的字符编码格式，支持GBK和UTF-8*/
	public static final String CHARSET = "UTF-8";
	
	/**商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2 */
	public static final String ENCRYPT_TYPE = "RSA2";  
	
	/**参数返回格式，只支持json */
	public static final String FORMAT = "json";  
	
	/**回调地址 */
	public static final String ALI_NOTIFY_URL = Constants.HOST_URL+"/pintuan/app/alipaycallback";
	
	/**阿里下单主题 */
	public static final String SUBJECT = "吾家优品";
	
	/**订单超时时间 */
	public static final String TIMEOUT_EXPRESS = "30m";
}
