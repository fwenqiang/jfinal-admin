package com.pintuan.controller.console.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Before;
import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Img;
import com.pintuan.model.Order;
import com.pintuan.model.PosterBoard;
import com.pintuan.model.Product;
import com.pintuan.service.ImageService;
import com.pintuan.service.OrderService;
import com.pintuan.service.PosterBoardService;
import com.pintuan.service.ProductService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.pintuan.util.ImgUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 商品管理
 * 
 * 未测试
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/console/product")
public class ProductController extends BaseProjectController {

	private ProductService productService = new ProductService();
	private ImageService imageService = new ImageService();

	public void index() throws CoreException {
		queryProduct();
	}

	// 商品查询
	public void queryProduct() throws CoreException {
		String pro_nme = getData(Fields.PRO_NME);
		String pro_typ = getData(Fields.PRO_TYP);
		int page = getNPage();
		int size = getNSize();
		List<Product> productList = productService.findByNameAndType(pro_nme, pro_typ, page, size);
		long total = productService.findByNameAndTypeSize(pro_nme, pro_typ);
		setResp("list", DBModelUtils.toMaps(productList));
		setResp("total", total);
		returnJson();
	}

	// 删除商品
	public void deleteProduct() throws CoreException {
		String pro_id = isNotNullAndGet(Fields.PRO_ID, ErrCode.PRO_ID_IS_NULL).toString();
		productService.delete(pro_id);
		returnJson();
	}

	// 默认图片上传
	public void uploadDefaultImage() throws CoreException {
		String host =  Constants.HOST_URL+"/product/"; //"http://127.0.0.1:8002/product/"; //
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String pro_id = getPara(Fields.PRO_ID);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			if (StrUtils.isNotEmpty(pro_id)) {
				productService.saveDefaultImage(pro_id, img_url);
			}
			System.out.println("上传文件成功");
			setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}
	
	// 视频上传
	public void uploadVideo() throws CoreException {
		String host =  Constants.HOST_URL+"/product/"; //"http://127.0.0.1:8002/product/"; //
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String pro_id = getPara(Fields.PRO_ID);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			img_url = Constants.HOST_URL+"/"+newFileName.split("/")[newFileName.split("/").length - 1];
			if (StrUtils.isNotEmpty(pro_id)) {
				productService.saveVideo(pro_id, img_url);
			}
			System.out.println("上传文件成功");
			setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	private String resetFileName(String fileName) throws IOException {
		String path =   "/home/root/images/product/";  //"G:/home/images/product/";
		//path = "/home/tomcat/apache-tomcat-api/webapps/jfinal-admin-1.0/";
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return path + UuidUtils.getUUID2() + "." + lastName;
	}

	// 更新商品
	public void updateProduct() throws CoreException {
		Map<String, Object> product = getContext();
		productService.update(product);
		returnJson();
	}

	// 添加商品
	public void addProduct() throws CoreException {
		Map<String, Object> product = getContext();
		productService.add(product);
		returnJson();
	}

	// 轮播图片和详情图片
	public void uploadImage() throws CoreException {
		String host =  Constants.HOST_URL+"/product/";//"http://127.0.0.1:8002/product/"; 
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String size = ImgUtil.getSize(delfile);
			String picel = ImgUtil.getWidthAndHeight(delfile);
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String pro_id = getPara(Fields.PRO_ID);
			String img_typ = getPara(Fields.IMG_TYP);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			imageService.add(pro_id, img_url, img_typ, size, picel);
			System.out.println("上传文件成功");
			// setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	// 删除商品图片
	public void deleteProductImage() throws CoreException {
		String img_id = getData(Fields.IMG_ID);
		imageService.delete(img_id);
		returnJson();
	}

	/**
	 * 查询商品图片列表
	 **/
	public void queryProductImgList() throws CoreException {
		String pro_id = isNotNullAndGet(Fields.PRO_ID, ErrCode.PRO_ID_IS_NULL).toString();
		String img_typ = isNotNullAndGet(Fields.IMG_TYP, ErrCode.IMG_TYP_IS_NULL).toString();
		List<Img> imgList = imageService.findByThdId(pro_id, img_typ, Constants.ACCESS_STATE);
		setResp(Fields.IMG_URL_LIST, DBModelUtils.toMapsInc(imgList, Fields.IMG_ID, Fields.IMG_URL, Fields.IMG_SRT));
		returnJson();
	}

}
