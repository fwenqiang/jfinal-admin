package com.pintuan.controller.app.user;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.User;
import com.pintuan.util.Assert;
import com.pintuan.util.ImgUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.extend.UuidUtils;

/**
 * 获得推荐人图片
 * 
 * @author dengbo 20180629
 */
@ControllerBind(controllerKey = "/pintuan/app/getRecommendImg")
@Before(CheckUserKeyInterceptor.class)    
public class GetRecommendImgController extends BaseProjectController {

	public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY,ErrCode.PAMAS_ERROR).toString();
		User user = (User)getAttribute(Fields.ATTR_USER_ENTITY);
		String usr_nme = user.getStr(Fields.USER_NAME);
		String tit_url = user.getStr(Fields.TIT_URL);
		Assert.notEmpty(user, ErrCode.PAMAS_ERROR);
		
		String qrData = "http://admin.iwjup.com/jfinal-admin-1.0/pintuan/app/page/userRegister?p_id="+usr_ide_key;
		//qrData = "http://116.62.189.213:9999/jfinal-admin-1.0/pintuan/app/page/userRegister?p_id="+usr_ide_key;
		
		String fileName = UuidUtils.getUUID(10)+".jpg";
		String desPic = "/home/root/images/usr/"+fileName;//"D:\\temp\\"+fileName;//
		//desPic = "/home/tomcat/apache-tomcat-api/webapps/jfinal-admin-1.0/"+fileName;
		ImgUtils.cerateQRCodePic(desPic,qrData,tit_url);
		String host = Constants.HOST_URL+"/usr/";
        setResp(Fields.FILE_PATH, host+fileName); 
        
       
        returnJson();
	}	

}
