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
import java.security.MessageDigest;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.alibaba.druid.support.json.JSONUtils;
import com.pintuan.base.CoreException;
import com.pintuan.common.Fields;
import com.pintuan.common.WXConstants;
import com.pintuan.common.WeixinFields;
import com.pintuan.model.bean.WXResult;
import com.supyuan.util.encrypt.Base64;

  
/* 
 * 用户发起统一下单请求 
 */  
@SuppressWarnings("deprecation")
public class WXRequestUtil {  
      
    public static void main(String[] args) {  
    	//BigDecimal amt = new BigDecimal("0.11");
    	// Map<String,String> res = SendPayment("苹果","2017010611332544",amt,"1");
    	// System.out.println(res);
    	
    	/*WXResult res = queryOrder("bb427ad03113454494609f667ae9d310");
    	System.out.println("查询成功："+res.getData());
    	
    	String appid = WXConstants.APP_ID;
    	String partnerid = WXConstants.MCH_ID;
    	String prepayid = "wx0510340506468648d1c20d112740406061";
    	String noncestr = "WkcyMYiitzNYgjsG";
    	String timestamp = "1525487645";
    	System.out.println(getAPPSign(prepayid,noncestr,timestamp));*/
    	
    	
    	String code = "0016HnkB18TYnf0ZJghB1ZLqkB16Hnkp";
    	getAccessToken(code);
    }  
      
    /* 
     * 发起支付请求 
     * body 商品描述 
     * out_trade_no 订单号 
     * total_fee    订单金额        单位  元 
     * product_id   商品ID 
     */  
    public static Map<String,String> SendPayment(String body,String out_trade_no,BigDecimal total_fee,String product_id){  
        String url = WXConstants.WEIXIN_UNIFIEDORDER_URL;  
        String xml = WXParamGenerate(body,out_trade_no,total_fee,product_id);  
        System.out.println("xml="+xml);
        String res = httpsRequest(url,"POST",xml);  
        // System.out.println("res="+res);
        Map<String,String> data = null;  
        try {  
            data = doXMLParse(res);
            // System.out.println("data="+data);
        } catch (Exception e) {  
        	e.printStackTrace();
        }  
        return data;  
    } 
    
    /* 
     * 查询订单 
     * out_trade_no 订单号 
     */  
    public static WXResult queryOrder(String out_trade_no){  
        String url = WXConstants.WEIXIN_ORDERQUERY_URL;  
        String xml = WXQueryOrdParamGenerate(out_trade_no);  
        String res = httpsRequest(url,"POST",xml);  
        Map<String,String> data = null;  
        try {  
            data = doXMLParse(res);
        } catch (Exception e) {  
        	e.printStackTrace();
        }  
        System.out.println("res="+data.toString());
        return WXResult.build(data);  
    } 
    
    //****************************************************//
      
    /**随机数**/
    public static String NonceStr(){  
        String res = Base64.encodeAsString(Math.random()+"::"+new Date().toString()).substring(0, 30);  
        return res;  
    }  
    
    
      
     public static String GetIp() {  
        InetAddress ia=null;  
        try {  
            ia=InetAddress.getLocalHost();  
            String localip=ia.getHostAddress();  
            return localip;  
        } catch (Exception e) {  
            return null;  
        }  
    }  
       
     public static String GetSign(Map<String,String> param){  
        String StringA =  WXRequestUtil.formatUrlMap(param, false, false);  
        // System.out.println("md5Str:"+StringA+"&key="+WXConstants.API_KEY);
        String stringSignTemp = MD5(StringA+"&key="+WXConstants.API_KEY).toUpperCase();
        // System.out.println("stringSignTemp:"+stringSignTemp);
        return stringSignTemp;  
     }  
       
     //Map转xml数据  
     public static String GetMapToXML(Map<String,String> param){  
         StringBuffer sb = new StringBuffer();  
         sb.append("<xml>");  
         for (Map.Entry<String,String> entry : param.entrySet()) {   
                sb.append("<"+ entry.getKey() +">");  
                sb.append(entry.getValue());  
                sb.append("</"+ entry.getKey() +">");  
        }    
         sb.append("</xml>");  
         return sb.toString();  
     }  
      
      
    //微信统一下单参数设置  
    public static String WXParamGenerate(String description,String out_trade_no,BigDecimal total_fee,String product_id){  
        int fee = total_fee.multiply(new BigDecimal("100")).intValue();
        Map<String,String> param = new HashMap<String,String>();  
        param.put("appid", WXConstants.APP_ID);  
        param.put("body", description);  
        param.put("mch_id", WXConstants.MCH_ID);  
        param.put("nonce_str",NonceStr());  
        param.put("notify_url", WXConstants.WEIXIN_NOTIFY);  
        param.put("out_trade_no",out_trade_no); //商户订单号 
        param.put("spbill_create_ip", GetIp());  
        param.put("total_fee", fee+"");  
        param.put("trade_type", "APP");   //NATIVE
        param.put("product_id", product_id);  
          
        String sign = GetSign(param);  
          
        param.put("sign", sign);  
        return GetMapToXML(param);  
    }  
    
   //微信查询订单参数设置  
    public static String WXQueryOrdParamGenerate(String out_trade_no){  
        Map<String,String> param = new HashMap<String,String>();  
        param.put("appid", WXConstants.APP_ID);       
        param.put("mch_id", WXConstants.MCH_ID);  
        param.put("nonce_str",NonceStr());  
        param.put("out_trade_no",out_trade_no); //商户订单号 
          
        String sign = GetSign(param);            
        param.put("sign", sign);  
        return GetMapToXML(param);  
    }
    
    /**获得APP签名**/
    public static String getAPPSign(String prepayid,String noncestr,String timestamp){  
        Map<String,String> param = new HashMap<String,String>();  
        param.put(WeixinFields.APPID, WXConstants.APP_ID);  
        param.put(WeixinFields.PARTNERID, WXConstants.MCH_ID);  
        param.put(WeixinFields.PREPAYID, prepayid);  
        param.put(WeixinFields.NONCESTR,noncestr);  
        param.put(WeixinFields.TIMESTAMP, timestamp);
        param.put(WeixinFields.PACKAGE, WXConstants.PACKAGE_VALUE);   
        String sign = GetSign(param);            
        return sign;  
    }  
      
    //发起微信支付请求  
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {    
      try {    
          URL url = new URL(requestUrl);    
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();    
            
          conn.setDoOutput(true);    
          conn.setDoInput(true);    
          conn.setUseCaches(false);    
          // 设置请求方式（GET/POST）    
          conn.setRequestMethod(requestMethod);    
          conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");    
          // 当outputStr不为null时向输出流写数据    
          if (null != outputStr) {    
              OutputStream outputStream = conn.getOutputStream();    
              // 注意编码格式    
              outputStream.write(outputStr.getBytes("UTF-8"));    
              outputStream.close();    
          }    
          // 从输入流读取返回内容    
          InputStream inputStream = conn.getInputStream();    
          InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
          String str = null;  
          StringBuffer buffer = new StringBuffer();    
          while ((str = bufferedReader.readLine()) != null) {    
              buffer.append(str);    
          }    
          // 释放资源    
          bufferedReader.close();    
          inputStreamReader.close();    
          inputStream.close();    
          inputStream = null;    
          conn.disconnect();    
          return buffer.toString();    
      } catch (ConnectException ce) {    
          System.out.println("连接超时：{}"+ ce);    
      } catch (Exception e) {    
          System.out.println("https请求异常：{}"+ e);    
      }    
      return null;    
    }    
        
    //退款的请求方法    
    public static String httpsRequest2(String requestUrl, String requestMethod, String outputStr) throws Exception {    
          KeyStore keyStore  = KeyStore.getInstance("PKCS12");    
          StringBuilder res = new StringBuilder("");    
          FileInputStream instream = new FileInputStream(new File("/home/apiclient_cert.p12"));    
          try {    
              keyStore.load(instream, "".toCharArray());    
          } finally {    
              instream.close();    
          }    
  
          // Trust own CA and all self-signed certs    
          // @SuppressWarnings("deprecation")
		SSLContext sslcontext = SSLContexts.custom()    
                  .loadKeyMaterial(keyStore, "1313329201".toCharArray())    
                  .build();    
          // Allow TLSv1 protocol only    
          // @SuppressWarnings("deprecation")
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(    
                  sslcontext,    
                  new String[] { "TLSv1" },    
                  null,    
                  SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);    
          CloseableHttpClient httpclient = HttpClients.custom()    
                  .setSSLSocketFactory(sslsf)    
                  .build();    
          try {    
  
              HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");    
              httpost.addHeader("Connection", "keep-alive");    
              httpost.addHeader("Accept", "*/*");    
              httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");    
              httpost.addHeader("Host", "api.mch.weixin.qq.com");    
              httpost.addHeader("X-Requested-With", "XMLHttpRequest");    
              httpost.addHeader("Cache-Control", "max-age=0");    
              httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");    
               StringEntity entity2 = new StringEntity(outputStr ,Consts.UTF_8);    
               httpost.setEntity(entity2);    
              System.out.println("executing request" + httpost.getRequestLine());    
  
              CloseableHttpResponse response = httpclient.execute(httpost);    
                 
              try {    
                  HttpEntity entity = response.getEntity();    
                      
                  System.out.println("----------------------------------------");    
                  System.out.println(response.getStatusLine());    
                  if (entity != null) {    
                      System.out.println("Response content length: " + entity.getContentLength());    
                      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));    
                      String text = "";  
                      res.append(text);    
                      while ((text = bufferedReader.readLine()) != null) {    
                          res.append(text);    
                          System.out.println(text);    
                      }    
                         
                  }    
                  EntityUtils.consume(entity);    
              } finally {    
                  response.close();    
              }    
          } finally {    
              httpclient.close();    
          }    
          return  res.toString();    
              
    }  
        
    //xml解析    
    @SuppressWarnings("rawtypes")
	public static Map<String, String> doXMLParse(String strxml) throws Exception {    
          strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");    
          if(null == strxml || "".equals(strxml)) {    
              return null;    
          }    
              
          Map<String,String> m = new HashMap<String,String>();     
          InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));    
          SAXBuilder builder = new SAXBuilder();    
          Document doc = builder.build(in);    
          Element root = doc.getRootElement();    
          List list = root.getChildren();    
          Iterator it = list.iterator();    
          while(it.hasNext()) {    
              Element e = (Element) it.next();    
              String k = e.getName();    
              String v = "";    
              List children = e.getChildren();    
              if(children.isEmpty()) {    
                  v = e.getTextNormalize();    
              } else {    
                  v = getChildrenText(children);    
              }    
                  
              m.put(k, v);    
          }    
              
          //关闭流    
          in.close();     
          return m;    
    }    
        
    @SuppressWarnings("rawtypes")
	public static String getChildrenText(List children) {    
          StringBuffer sb = new StringBuffer();    
          if(!children.isEmpty()) {    
              Iterator it = children.iterator();    
              while(it.hasNext()) {    
                  Element e = (Element) it.next();    
                  String name = e.getName();    
                  String value = e.getTextNormalize();    
                  List list = e.getChildren();    
                  sb.append("<" + name + ">");    
                  if(!list.isEmpty()) {    
                      sb.append(getChildrenText(list));    
                  }    
                  sb.append(value);    
                  sb.append("</" + name + ">");    
              }    
          }     
          return sb.toString();    
    }  
    
    // TODO
    public static String formatUrlMap(Map<String,String> paraMap,boolean urlencode,boolean pama2) {
    	String buff = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());
            Collections.sort(infoIds,
                    new Comparator<Map.Entry<String, String>>() {
           public int compare(Map.Entry<String, String> o1,
                  Map.Entry<String, String> o2) {
                 return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<String, String> item = infoIds.get(i);
                //System.out.println(item.getKey());
                if (item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlencode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    buff += key + "=" + val + "&";
                }
            }
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            throw new CoreException("001",e.getMessage());
        }
        return buff;
    }
    
    /**微信网页授权登录<br>
     * 第二步：通过code获得access_token
     * appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
     * secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * code	            是	填写第一步获取的code参数
     * grant_type	是	填authorization_code
     * **/
    public static Map<String,Object> getAccessToken(String code) {
    	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXConstants.APP_APP_ID+"&secret="+WXConstants.APP_APP_SECRET+"&code="+code+"&grant_type=authorization_code";  
    	String res = httpsRequest(url,"GET",null); 
    	System.out.println("getAccessToken="+res);
    	@SuppressWarnings("unchecked")
		Map<String,Object> resMap = (Map<String,Object>)JSONUtils.parse(res);
    	return resMap;
    }
    
    /**微信公众号授权登录<br>
     * 第二步：通过code获得access_token
     * appid	是	应用唯一标识，在微信开放平台提交应用审核通过后获得
     * secret	是	应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * code	            是	填写第一步获取的code参数
     * grant_type	是	填authorization_code
     * **/
    public static Map<String,Object> getGZAccessToken(String code) {
    	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WXConstants.GZ_APP_ID+"&secret="+WXConstants.GZ_APP_SECRET+"&code="+code+"&grant_type=authorization_code";  
    	String res = httpsRequest(url,"GET",null); 
    	System.out.println("getAccessToken="+res);
    	@SuppressWarnings("unchecked")
		Map<String,Object> resMap = (Map<String,Object>)JSONUtils.parse(res);
    	return resMap;
    }
    
    /**微信公众号授权登录<br>
     * 获取用户个人信息（UnionID机制）<br>
     * **/
    public static Map<String,Object> getGZUserInfo(String access_token,String openid) {
    	String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;  
    	String res = httpsRequest(url,"GET",null); 
    	@SuppressWarnings("unchecked")
		Map<String,Object> resMap = (Map<String,Object>)JSONUtils.parse(res);
    	System.out.println("getUserInfo="+resMap);
    	return resMap;
    }
    
    /**微信网页授权登录<br>
     * 刷新access_token有效期<br>
     * 1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
     * 2. 若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
     * **/
    public static Map<String,Object> refreshToken(String refresh_token) {
    	String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+WXConstants.APP_ID+"&grant_type=refresh_token&refresh_token="+refresh_token;  
    	String res = httpsRequest(url,"GET",null); 
    	@SuppressWarnings("unchecked")
		Map<String,Object> resMap = (Map<String,Object>)JSONUtils.parse(res);
    	System.out.println(resMap);
    	return resMap;
    }
    
    /**微信网页授权登录<br>
     * 获取用户个人信息（UnionID机制）<br>
     * **/
    public static Map<String,Object> getUserInfo(String access_token,String openid) {
    	String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;  
    	String res = httpsRequest(url,"GET",null); 
    	@SuppressWarnings("unchecked")
		Map<String,Object> resMap = (Map<String,Object>)JSONUtils.parse(res);
    	System.out.println("getUserInfo="+resMap);
    	return resMap;
    }
    
    // md5
    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        String result = ret.toString();
        return result;
    }
    
    
}  