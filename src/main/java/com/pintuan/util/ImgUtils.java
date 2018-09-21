/**
 * Copyright 2015-2025 FLY的狐狸(email:jflyfox@sina.com qq:369191470).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.pintuan.util;

import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.swetake.util.Qrcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;


public class ImgUtils {
	
	/**创建二维码**/
	public static BufferedImage createQRCode(String qrData ) throws UnsupportedEncodingException {  
		       Qrcode qrcode = new Qrcode();  
		       qrcode.setQrcodeErrorCorrect('M');//纠错等级（分为L、M、H三个等级）  
		       qrcode.setQrcodeEncodeMode('B');//N代表数字，A代表a-Z，B代表其它字符  
		       qrcode.setQrcodeVersion(7);//版本  
		       //设置一下二维码的像素  
		       int width = 67+12*(7-1);  
		       int height = 67 + 12*(7-1);  
		       BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
		       //绘图  
		       Graphics2D gs = bufferedImage.createGraphics();  
		       gs.setBackground(Color.WHITE);  
		       gs.setColor(Color.BLACK);  
		       gs.clearRect(0, 0, width, height);//清除下画板内容  
		         
		       //设置下偏移量,如果不加偏移量，有时会导致出错。  
		       int pixoff = 2;  
		         
		       byte[] d = qrData.getBytes("gb2312");  
		       if(d.length > 0 && d.length <120){  
		           boolean[][] s = qrcode.calQrcode(d);  
		           for(int i=0;i<s.length;i++){  
		               for(int j=0;j<s.length;j++){  
		                   if(s[j][i]){  
		                       gs.fillRect(j*3+pixoff, i*3+pixoff, 3, 3);  
		                   }  
		               }  
		           }  
		       }  
		       gs.dispose();  
		       bufferedImage.flush();  
		       
		       BufferedImage tag = new BufferedImage(310, 310, BufferedImage.TYPE_INT_RGB);
		       //绘制改变尺寸后的图
		       tag.getGraphics().drawImage(bufferedImage, 0, 0,310, 310, null);
		       return tag;
		       //return bufferedImage;
		   }  
	
	//创建二维码图片
	public static void cerateQRCodePic(String desPic,String qrData,String usrUrl) {
		try {
			Pic tt = new Pic();
			BufferedImage b = ImgUtils.createQRCode(qrData);
			
			//b = CircleImg.getCircleImg(usrUrl);

			tt.writeImageLocal(desPic, b);
		}catch(Exception e) {
			throw new CoreException(ErrCode.CREATE_SHARE_IMG_ERR);
		}
	}
	
	public static void ceratePic(String localPic,String desPic,String qrData) throws UnsupportedEncodingException{
		    Pic tt = new Pic();  
		  
	        BufferedImage d = tt.loadImageLocal(localPic);  
	        BufferedImage b = ImgUtils.createQRCode(qrData);// tt.loadImageLocal("F:\\code\\qrcode.png");  
//	      BufferedImage b = tt  
//	              .loadImageLocal("E:\\文件(word,excel,pdf,ppt.txt)\\zte-logo.png");  
	         /*tt.writeImageLocal("F:\\test2.jpg",tt.modifyImage(d,"曹原",90,90)  
	        //往图片上写文件  
	         );  */
	        d = tt.modifyImage(d,"曹原1",439,1027,Color.gray,new Font("华文彩云", Font.PLAIN, 40));
	        //d = tt.modifyImage(d,"曹原2",10,10);
	        d = tt.modifyImagetogeter(d, b,68,68);
	        b = CircleImg.getCircleImg("D:\\temp\\1.jpg");
	        tt.writeImageLocal(desPic, tt.modifyImagetogeter(d, b,68,68));  
	        //将多张图片合在一起  
	        System.out.println("success");  
	}
	/**
	 * 
	 * @param localPic
	 * @param desPic
	 * @param qrData
	 * @param proTyp
	 * @param chidNum
	 * @param amt
	 * @param name
	 * @param usrUrl
	 * @throws UnsupportedEncodingException
	 */
	public static void ceratePic(String localPic,String desPic,String qrData,String proTyp,String proNum,String chidNum,String amt,String name,String usrUrl) {
		try {
	    Pic tt = new Pic();  
	    int x = (amt.length()-1)*33;
        BufferedImage d = tt.loadImageLocal(localPic);  
        BufferedImage b = ImgUtils.createQRCode(qrData); 
        d = tt.modifyImage(d,name,436,1024,Color.gray,new Font("微软雅黑", Font.PLAIN, 35));
        d = tt.modifyImage(d,proTyp,150,1410,Color.gray,new Font("微软雅黑", Font.PLAIN, 35));
        d = tt.modifyImage(d,proNum,250,1318,Color.red,new Font("微软雅黑", Font.PLAIN, 50));
        d = tt.modifyImage(d,chidNum,530,1318,Color.red,new Font("微软雅黑", Font.PLAIN, 50));
        d = tt.modifyImage(d,amt,870-x,1318,Color.red,new Font("微软雅黑", Font.PLAIN, 52)); //773   870
        //写二维码
        d = tt.modifyImagetogeter(d, b,68,1556);  
        b = CircleImg.getCircleImg(usrUrl);
        d = tt.modifyImagetogeter(d, b,127,1060);  //图片可以
        tt.writeImageLocal(desPic, d);  
		}catch(Exception e) {
			throw new CoreException(ErrCode.CREATE_SHARE_IMG_ERR);
		}
}


	public static void ceratePic(String localPic,String desPic,String qrData,String name,String usrUrl) {
		try {
			Pic tt = new Pic();
			BufferedImage d = tt.loadImageLocal(localPic);
			BufferedImage b = ImgUtils.createQRCode(qrData);
			d = tt.modifyImage(d,name,436,1024,Color.gray,new Font("微软雅黑", Font.PLAIN, 35));
			//写二维码
			d = tt.modifyImagetogeter(d, b,68,1556);
			b = CircleImg.getCircleImg(usrUrl);
			d = tt.modifyImagetogeter(d, b,127,1060);  //图片可以
			tt.writeImageLocal(desPic, d);
		}catch(Exception e) {
			throw new CoreException(ErrCode.CREATE_SHARE_IMG_ERR);
		}
	}
	
	

	public static void main(String[] args) throws UnsupportedEncodingException {
		
	
		String localPic = "D:\\temp\\share1.jpg";
		String qrData = "www.baidu.com";
		String desPic = "D:\\temp\\des4.jpg";
		//ImgUtils.ceratePic(localPic,desPic,qrData, "教育类商品","1","2","10.00","小明","https://1ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1219655081,3087581486&fm=200&gp=0.jpg");
		//System.out.println("OK");
		
		ImgUtils.cerateQRCodePic("D:/test/1.jpg","http://admin.iwjup.com/jfinal-admin-1.0/pintuan/app/page/userRegister?p_id=0005E5C69E9F15EBDD69225B883839CB","");
		
		 //读取图片
		/*BufferedInputStream in = new BufferedInputStream(new FileInputStream("D:\\temp\\share.jpg"));
		//字节流转图片对象
		Image bi =ImageIO.read(in);
		//获取图像的高度，宽度
		int height=bi.getHeight(null);
		int width =bi.getWidth(null);
		//构建图片流
		BufferedImage tag = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		//绘制改变尺寸后的图
		tag.getGraphics().drawImage(bi, 0, 0,100, 100, null);  
		//输出流
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\temp\\share2.jpg"));
		//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		//encoder.encode(tag);
		ImageIO.write(tag, "PNG",out);
		in.close();
		out.close();*/
		
	}

}
