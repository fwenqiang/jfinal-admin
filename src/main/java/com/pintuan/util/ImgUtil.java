package com.pintuan.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 图片工具
 * 
 * @author zjh 2018-5-4
 */
public class ImgUtil {

	/**
	 * 获得图片的宽高，如：600*200
	 **/
	public static String getWidthAndHeight(File picture) {
		try {
			BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
			System.out.println(String.format("%.1f", picture.length() / 1024.0));// 源图大小
			return sourceImg.getWidth() + "*" + sourceImg.getHeight();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获得图片大小，如：372.0
	 **/
	public static String getSize(File picture) {
		try {
			return String.format("%.1f", picture.length() / 1024.0);
		} catch (Exception e) {
			return "";
		}

	}

	public static void main(String[] args) {

		System.out.println(
				"[" + ImgUtil.getWidthAndHeight(new File("G:\\img\\4af1b4df-88c1-4fbc-aa60-f0a61eeee963crop_photo.jpg"))
						+ "]");
	}
	/**
	 * 文件重命名
	 * @param srvPath  服务器路径
	 * @param fileName 文件名
	 * @return  新文件名
	 */
	public static String resetFileName(String srvPath,String fileName) {
		//String path =   "/home/root/images/product/";  
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return srvPath + UuidUtils.getUUID2() + "." + lastName;
	}

}
