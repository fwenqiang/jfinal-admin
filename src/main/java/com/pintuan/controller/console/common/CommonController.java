package com.pintuan.controller.console.common;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.Config;
import com.pintuan.model.Dict;
import com.pintuan.service.ConfigService;
import com.pintuan.service.DictService;
import com.pintuan.service.UserService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公用
 * 
 * @author zjh 2018-3-24
 */
@ControllerBind(controllerKey = "/pintuan/console")
public class CommonController extends BaseProjectController {
	private UserService userService = new UserService();
	private DictService dictService = new DictService();
	private ConfigService configService =new ConfigService();
	public void index() throws CoreException {
		login();
	}
	
	
	public void login() throws CoreException {
        
        String userName = getData(Fields.CONSOLE_USER_NAME);
        String password = getData(Fields.CONSOLE_PASSWORD);
        Record user = userService.findUser(userName, password);
        if(null==user) {
        	throw new CoreException(ErrCode.LOGIN_ERROR);
        }
        //setData(Fields.REFLASH_USER_KEY,"1234343243");
        //setResp("CONSOLE_USER_KEY", "222222");
        setResp(Fields.CONSOLE_USER_KEY,user.get(Fields.USER_ID));
        returnJson();
	}
	
	public void user() throws CoreException{
		System.out.println(">>"+getRequest().getMethod());
		String userKey = getData(Fields.CONSOLE_USER_KEY);
		System.out.println("userKey:["+userKey+"]");
		System.out.println("StrUtils.isEmpty(userKey):["+StrUtils.isEmpty(userKey)+"]");
		if(StrUtils.isEmpty(userKey)) {
			setResp("success", false);			
		}else {
			setResp("success", true);
		}
        setResp("message", "Ok");
        Map<String,Object> user = new HashMap<String,Object>();
        Map<String,Object> permissions = new HashMap<String,Object>();
        String[] visit = new String[]{"1","2","7","21","3","4"};
        permissions.put("role", "admin");
        permissions.put("visit", visit);
        user.put("permissions", permissions);
        setResp("user",user);
        returnJson();
	}
	
	public void dashboard() throws CoreException{
		
        returnJson();
	}
	
    public void activeUser() throws CoreException{
		
    	System.out.println(">>"+getRequest().getMethod());
		String userKey = getData(Fields.CONSOLE_USER_KEY);
		System.out.println("userKey:["+userKey+"]");
		System.out.println("StrUtils.isEmpty(userKey):["+StrUtils.isEmpty(userKey)+"]");
		if(StrUtils.isEmpty(userKey)) {
			setResp("isSuccess", false);
		}else {
			setResp("isSuccess", true);
		}
		setResp("message", "Ok");
		Map<String,Object> user = new HashMap<String,Object>();
        Map<String,Object> permissions = new HashMap<String,Object>();
        List<String> visit = new ArrayList<String>();//{"1","2","7","21","3","4"};
        visit.add("1");
        visit.add("2");
        visit.add("7");
        visit.add("21");
        visit.add("3");
        visit.add("4");
        permissions.put("role", "admin");
        permissions.put("visit", visit);
        user.put("permissions", permissions);
        setResp("user",user);
        returnJson();
	}
    
    public void logout() {
    	setData(Fields.REFLASH_USER_KEY,"");
    	returnJson();
    }
    
    public static void main(String[] args) {
    	Map<String,Object> user = new HashMap<String,Object>();
        Map<String,Object> permissions = new HashMap<String,Object>();
        List<String> visit = new ArrayList<String>();//{"1","2","7","21","3","4"};
        visit.add("1");
        visit.add("2");
        visit.add("7");
        visit.add("21");
        visit.add("3");
        visit.add("4");
        permissions.put("role", "admin");
        permissions.put("visit", visit);
        user.put("permissions", permissions);
        System.out.println(JSONUtils.toJSONString(user));
    }
    
    
    public void uploadAPK() throws CoreException {
		try {
			UploadFile file = getFile();
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	private String resetFileName(String fileName) throws IOException {
		String path = "/home/root/images/";//"D:\\temp\\";//
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return path + "newVersion." + lastName;
	}
	
	public void queryVersion() throws CoreException {
		String app_typ_android = "android";
		String app_typ_ios = "ios";
		Dict android = dictService.findByKey(app_typ_android+"_version");
		Dict ios = dictService.findByKey(app_typ_ios+"_version");
		if(android!=null){
			setResp("androidVersion", android.getValMap().get("version"));
		}else {
			setResp("androidVersion", "");
		}
		if(ios!=null){
			setResp("iosVersion", ios.getValMap().get("version"));
		}else {
			setResp("iosVersion", "");
		}
		returnJson();
	}

	public void updateVersion() throws CoreException {
		String version = getData("version");
		String app_typ = getData("app_typ");
		Dict dict = dictService.findByKey(app_typ+"_version");
		if(dict==null) {
			dict = dictService.add(app_typ+"_version", "{}");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("version", version);
		dict.setValMap(map);
		dictService.update(dict);
		returnJson();
	}

	public void isShowPacket() throws CoreException {
		String isShow = getData(Fields.IS_PAKET_LIST_SHOW);
		Config config = configService.findConfig(Fields.CFG_ID);
		if("true".equals(isShow)){
			config.set("show_packet",1);
		}else {
			config.set("show_packet",0);
		}
		configService.update(config);
		returnJson();
	}

	public void isShowRedPacket() throws CoreException {
		String isShow = getData(Fields.IS_RED_PAKET_LIST_SHOW);
		Config config = configService.findConfig(Fields.CFG_ID);
		if("true".equals(isShow)){
			config.set("show_red_packet",1);
		}else {
			config.set("show_red_packet",0);
		}
		configService.update(config);
		returnJson();
	}

}
