package com.pintuan.controller.app.user;

import java.awt.GraphicsEnvironment;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.pintuan.model.User;
import com.pintuan.service.OrderService;
import com.pintuan.service.ProductService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.ImgUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 * 获得分享图片 
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/getShareImg")
@Before(CheckUserKeyInterceptor.class)    
public class GetShareImgController extends BaseProjectController {
	private OrderService orderService = new OrderService(); 
	private UserService userService = new UserService(); 
	private ProductService productService = new ProductService(); 
	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		// Order order = orderService.findByUserId(usr_ide_key,"1990"); 
		String proTyp = "商品";
		int proNum = 0;
		long count = userService.findChildrenUserCount(usr_ide_key);
		BigDecimal sum = userService.queryBonusCfgSum(usr_ide_key);
		if(sum==null) {
			sum = BigDecimal.ZERO;
		}
		List<Order> orderList = orderService.findByUserId(usr_ide_key);
		if(orderList!=null) {
			proNum = orderList.size();
			if(orderList.size()==1) {
				Product product = productService.findById(orderList.get(0).getStr(Fields.PRO_ID));
				proTyp = product.getStr(Fields.PRO_TYP) + proTyp;
			}else {
				proTyp = "   " + proTyp;
			}
		}
		String usr_nme = user.getStr(Fields.USER_NAME);
		String tit_url = user.getStr(Fields.TIT_URL);
		Assert.notEmpty(user, ErrCode.PAMAS_ERROR);
		String localPic = "/home/root/images/usr/share1.jpg";//"D:\\temp\\share1.jpg";//
		String qrData = "http://admin.iwjup.com/jfinal-admin-1.0/pintuan/app/page/userRegister?p_id="+usr_ide_key;
		//qrData = "http://116.62.189.213:9999/jfinal-admin-1.0/pintuan/app/page/userRegister?p_id="+usr_ide_key;
		String fileName = UuidUtils.getUUID(10)+".jpg";
		String desPic = "/home/root/images/usr/"+fileName;//"D:\\temp\\"+fileName;//
		ImgUtils.ceratePic(localPic,desPic,qrData, proTyp,proNum+"",count+"",sum.intValue()+"",usr_nme,tit_url);
		String host = Constants.HOST_URL+"/usr/";
        setResp(Fields.FILE_PATH, host+fileName); 
        
        //***********************************************
        Map<String,Object> para = new HashMap<String,Object>();
        String[] fontNames=GraphicsEnvironment.getLocalGraphicsEnvironment().  
                getAvailableFontFamilyNames();  
                for(String fontName:fontNames){  
                    System.out.println(fontName);  
                    para.put(fontName, fontName);
                }  
        setRespMap(para);
        returnJson();
	}	
	
	public static void main(String[] args) {
		BigDecimal amg = new BigDecimal("33.33");
		int a = amg.intValue();
		System.out.println(a);
	}
	
}
