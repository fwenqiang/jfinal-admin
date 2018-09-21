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
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.supyuan.util.encrypt.Base64;
import com.supyuan.util.encrypt.Md5Utils;
// import com.supyuan.util.encrypt.Md5Utils;
// import com.zhiweism.util.MD5;  
  
/** 
 * 异步回调接口 
 * @param request 
 * @param response 
 * @throws Exception 
 */  
public class WXResponseUtil{
/*public String WeixinParentNotifyPage(HttpServletRequest request,HttpServletResponse response) throws Exception{  
      
    ServletInputStream instream = request.getInputStream();  
    StringBuffer sb = new StringBuffer();  
    int len = -1;  
    byte[] buffer = new byte[1024];  
      
    while((len = instream.read(buffer)) != -1){  
        sb.append(new String(buffer,0,len));  
    }  
    instream.close();  
      
    Map<String,String> map = WXRequestUtil.doXMLParseWithSorted(sb.toString());//接受微信的通知参数  
    Map<String,String> return_data = new HashMap<String,String>();  
  
    //创建支付应答对象  
    ResponseHandler resHandler = new ResponseHandler(request, response);  
      
    resHandler.setAllparamenters(map);  
    resHandler.setKey(ConstantUtil.PARTNER_KEY[0]);  
      
    //判断签名  
    if(resHandler.isTenpaySign()){  
        if(!map.get("return_code").toString().equals("SUCCESS")){  
            return_data.put("return_code", "FAIL");  
            return_data.put("return_msg", "return_code不正确");  
        }else{  
            if(!map.get("result_code").toString().equals("SUCCESS")){  
                return_data.put("return_code", "FAIL");  
                return_data.put("return_msg", "result_code不正确");  
            }  
              
              
              
            String out_trade_no = map.get("out_trade_no").toString();  
            String time_end = map.get("time_end").toString();  
            BigDecimal total_fee = new BigDecimal(map.get("total_fee").toString());  
            //付款完成后，支付宝系统发送该交易状态通知  
            System.out.println("交易成功");  
            Map order = orderdao.PaymentEndGetOrderInfo(out_trade_no);  
              
            if(order == null){  
                System.out.println("订单不存在");  
                return_data.put("return_code", "FAIL");  
                return_data.put("return_msg", "订单不存在");  
                return WXRequestUtil.GetMapToXML(return_data);  
            }  
              
            int order_type = (int) order.get("order_type");  
            boolean payment_status = (boolean) order.get("payment_status");  
            int supplier_id = (int) order.get("supplier_id");  
              
            BigDecimal p = new BigDecimal("100");    
            BigDecimal amount = (BigDecimal) order.get("amount");  
              
            amount  = amount.multiply(p);  
              
            //如果订单已经支付返回错误  
            if(payment_status){  
                System.out.println("订单已经支付");  
                return_data.put("return_code", "SUCCESS");  
                return_data.put("return_msg", "OK");  
                return WXRequestUtil.GetMapToXML(return_data);  
            }  
              
              
            //如果支付金额不等于订单金额返回错误  
            if(amount.compareTo(total_fee)!=0){  
                System.out.println("资金异常");  
                return_data.put("return_code", "FAIL");  
                return_data.put("return_msg", "金额异常");  
                return WXRequestUtil.GetMapToXML(return_data);  
            }  
              
            //更新订单信息  
            if(orderdao.PaymentEndUpdateOrder(out_trade_no, time_end)){  
                System.out.println("更新订单成功");  
                //如果该订单是幼儿产品  并且 存在代理  
                if(order_type == 2){  
                    if(supplier_id != 0){  
                        Map su = userdao.getSupplierInfo(supplier_id);  
                        String phone = (String) su.get("phone_number");  
                        String nickname = (String) su.get("nickname");  
                        String app_token = (String) su.get("app_token");  
                          
                        String content = "【三盛科创】尊敬的"+ nickname +"您好。您在我们平台出售的商品有新用户下单。请您点击该链接查看发货信息。"+Config.WEB_SERVER+"/order/SupplierOrderInfo.do?order_number="+out_trade_no+"&sid="+app_token+".请您务必妥善包管。";  
                        MessageUtil.SendMessage(phone,content);  
                    }  
                }else{  
                    orderdao.UpdateOrderStatus(out_trade_no, 3);//更新订单为已完成  
                }  
                  
                return_data.put("return_code", "SUCCESS");  
                return_data.put("return_msg", "OK");  
                return WXRequestUtil.GetMapToXML(return_data);  
            }else{  
                return_data.put("return_code", "FAIL");  
                return_data.put("return_msg", "更新订单失败");  
                return WXRequestUtil.GetMapToXML(return_data);  
            }  
        }  
    }else{  
        return_data.put("return_code", "FAIL");  
        return_data.put("return_msg", "签名错误");  
    }  
      
    String xml = WXRequestUtil.GetMapToXML(return_data);  
    return xml;  
}*/  
}