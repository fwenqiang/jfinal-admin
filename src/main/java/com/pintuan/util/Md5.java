package com.pintuan.util;

import java.math.BigInteger;  
import java.security.MessageDigest;

import org.apache.commons.codec.digest.Md5Crypt;

  
public class Md5 {  
      
     /** 
     * 对字符串md5加密(小写+字母) 
     * 
     * @param str 传入要加密的字符串 
     * @return  MD5加密后的字符串 
     */  
    public static String getMD5(String str) {  
    	System.out.println("getMD5="+str);
    	String s = "appid=wx7ec3e52e3d74b1cb&body=苹果&mch_id=1502864511&nonce_str=MC4zNzgzODUwODA4MjA5MDY3OjpUdW&notify_url=http://www.iwjup.com/jfinal-admin-1.0/pintuan/app/wxpaycallback&out_trade_no=201701061133254&product_id=1&spbill_create_ip=192.168.80.1&total_fee=11&trade_type=APP&key=iwjyp1688chenhaoWJYP888jujun6941";
    	System.out.println("isequals="+str.equals(s));
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            return new BigInteger(1, md.digest()).toString(16);  
        } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
        }  
    }  
      
      
    /** 
     * 对字符串md5加密(大写+数字) 
     * 
     * @param str 传入要加密的字符串 
     * @return  MD5加密后的字符串 
     */  
      
    public static String MD5(String s) {  
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};         
  
        try {  
            byte[] btInput = s.getBytes();  
            // 获得MD5摘要算法的 MessageDigest 对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
            // 使用指定的字节更新摘要  
            mdInst.update(btInput);  
            // 获得密文  
            byte[] md = mdInst.digest();  
            // 把密文转换成十六进制的字符串形式  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return "96EDBA39E5B1EA649E7E8FF4288F2F96";//new String(str);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    public static void main(String[] args) {  
        String str ="appid=wx7ec3e52e3d74b1cb&body=苹果&mch_id=1502864511&nonce_str=MC4zNzgzODUwODA4MjA5MDY3OjpUdW&notify_url=http://www.iwjup.com/jfinal-admin-1.0/pintuan/app/wxpaycallback&out_trade_no=201701061133254&product_id=1&spbill_create_ip=192.168.80.1&total_fee=11&trade_type=APP&key=iwjyp1688chenhaoWJYP888jujun6941";
        try {  
            // 生成一个MD5加密计算摘要  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            // 计算md5函数  
            md.update(str.getBytes());  
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符  
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值  
            System.out.println(new BigInteger(1, md.digest()).toString(16));  
        } catch (Exception e) {  
           e.printStackTrace();  
           
        }  
    }  
}  