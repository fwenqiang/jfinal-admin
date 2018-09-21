package com.pintuan.controller.app.user;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 上传用户头像
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/uploadUserTitUrl")
public class UploadUserTitUrlController extends BaseProjectController {
	private UserService userService = new UserService();
	private String host = Constants.HOST_URL+"/usr/"; //
	public void index() throws CoreException {
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String usr_ide_key = getPara(Fields.USER_IDENTIFY_KEY);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			if (StrUtils.isNotEmpty(usr_ide_key)) {
				// userService.save();
			}
			System.out.println("upload success");
			setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("upload fail");
		}
        returnJson();
        
	}	
	
	
	private String resetFileName(String fileName) throws IOException {
		String path = "/home/root/images/usr/"; // /home/root/images/poster/
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return path + UuidUtils.getUUID2() + "." + lastName;
	}
}
