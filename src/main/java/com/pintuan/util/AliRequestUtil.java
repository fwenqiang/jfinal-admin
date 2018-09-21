package com.pintuan.util;  
  
import java.io.BufferedReader;  
import java.io.ByteArrayInputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;  
import java.net.HttpURLConnection;  
import java.net.InetAddress;  
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;  
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.List;  
import java.util.Map;  
  
import javax.net.ssl.SSLContext;  
  
import org.apache.http.Consts;  
import org.apache.http.HttpEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;  
import org.apache.http.conn.ssl.SSLContexts;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.util.EntityUtils;  
//import org.glassfish.jersey.internal.util.Base64;  
import org.jdom.Document;  
import org.jdom.Element;  
import org.jdom.input.SAXBuilder;

import com.alibaba.druid.support.json.JSONUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.pintuan.base.CoreException;
import com.pintuan.common.AliConstants;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
// import com.supyuan.util.encrypt.Md5Utils;
// import com.zhiweism.util.MD5;  
import com.supyuan.util.extend.UuidUtils;
  
/* 
 * 用户发起统一下单请求 
 * 作者：董志平 
 */  
@SuppressWarnings("deprecation")
public class AliRequestUtil {  
	
	//实例化客户端
	private static AlipayClient alipayClient = new DefaultAlipayClient(AliConstants.ALI_UNIFIEDORDER_URL, AliConstants.ALI_APP_ID, AliConstants.APP_PRIVATE_KEY, AliConstants.FORMAT, AliConstants.CHARSET, AliConstants.ALIPAY_PUBLIC_KEY, AliConstants.ENCRYPT_TYPE);
      
    public static void main(String[] args) {  
    	BigDecimal amt = new BigDecimal("0.119999");
    	String res = SendPayment("苹果","20170106113324",amt);
    	 System.out.println(res);
    }  
      
    /* 
     * 发起支付请求 
     * body 商品描述 
     * out_trade_no 订单号 
     * total_fee    订单金额        单位  元 
     * product_id   商品ID 
     */  
    public static String SendPayment(String body,String out_trade_no,BigDecimal total_fee){  
    	//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
    	AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
    	//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
    	AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
    	model.setBody(body);
    	model.setSubject(AliConstants.SUBJECT);
    	model.setOutTradeNo(out_trade_no);
    	model.setTimeoutExpress(AliConstants.TIMEOUT_EXPRESS);
    	model.setTotalAmount(total_fee.toString());
    	model.setProductCode(out_trade_no);
    	request.setBizModel(model);
    	request.setNotifyUrl(AliConstants.ALI_NOTIFY_URL);
    	try {
    	        //这里和普通的接口调用不同，使用的是sdkExecute
    	        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);    	        
    	        return response.getBody();//就是orderString 可以直接给客户端请求，无需再做处理。
    	    } catch (AlipayApiException e) {
    	        throw new CoreException(ErrCode.ALI_UNIFIED_FAIL);
    	}
    } 
}  