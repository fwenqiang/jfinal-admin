package com.pintuan.controller.console.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.BonusCfg;
import com.pintuan.model.User;
import com.pintuan.service.BonusService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.pintuan.util.Excel;
import com.pintuan.util.ImgUtil;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 用户管理
 * 
 * 未测试
 * 
 * @author zjh 2018-5-16
 */
@ControllerBind(controllerKey = "/pintuan/console/userManager")
public class UserManagerController extends BaseProjectController {

	private UserService userService = new UserService();
	private BonusService bonusService = new BonusService();

	public void index() throws CoreException {
		queryUserList();
	}

	// 查询用户列表
	public void queryUserList() throws CoreException {
		String thd_id = getData(Fields.THD_ID);
		String usr_nme = getData(Fields.USER_NAME);
		String pho_no = getData(Fields.PHONE_NO);
		String order_amount = getData(Fields.ORDER_AMOUNT);
		
		int page = getNPage();
		int size = getNSize();
		List<Record> userList = userService.findUserList(thd_id, usr_nme, pho_no, order_amount, page, size);
		long total = userService.findSize(thd_id, usr_nme, pho_no,order_amount);
		setResp(Fields.USR_LIST, DBModelUtils.toMaps(userList, Record.class));
		setResp(Fields.TOTAL, total);
		returnJson();
	}

	/**
	 * 查询用户转账列表  by  eric 2018.09.18 22:09
	 * @throws CoreException
	 */
	public void queryUserTransferList() throws CoreException {
		String usr_nme = getData(Fields.USER_NAME);
		String pho_no = getData(Fields.PHONE_NO);

		int page = getNPage();
		int size = getNSize();
		List<Record> userList = userService.queryUserTransferList(usr_nme, pho_no, page, size);
		long total = userService.queryUserTransferListSize(usr_nme, pho_no);
		setResp(Fields.TRANSFER_LIST, DBModelUtils.toMaps(userList, Record.class));
		setResp(Fields.TOTAL, total);
		returnJson();
	}
	
	public void querySameAccountList() throws CoreException {
		
		int page = getNPage();
		int size = getNSize();
		
		String sameNum = getData(Fields.HAS_ACCOUNT_NUM);
		if(StringUtils.isBlank(sameNum)){
			sameNum = "8";
		}
		
		List<Map<String,Object>> userList = userService.findSameUser(Integer.parseInt(sameNum),page, size);
		long total = userService.countSameUser(Integer.parseInt(sameNum));
		setResp(Fields.USR_LIST, userList);
		setResp(Fields.TOTAL, total);
		returnJson();
	}

	// 查询用户详情
	public void queryUserDetail() throws CoreException {
		String usr_id = isNotNullAndGet(Fields.USER_ID, ErrCode.PAMAS_ERROR).toString();
		User user = userService.find(usr_id);
		Map<String, Object> result = new HashMap<String, Object>();
		if (user != null) {
			result = user.getAttrs();
		}
		if (user != null && StrUtils.isNotEmpty(user.getStr(Fields.P_ID))) {
			User parent = userService.find(user.getStr(Fields.P_ID));
			if (parent != null) {
				result.put(Fields.P_THD_ID, parent.get(Fields.THD_ID));
				result.put(Fields.P_USR_NME, parent.get(Fields.USER_NAME));
			}
		}
		setResp("user", result);
		returnJson();
	}
	
	// 查询用户关系
	public void queryUserRelate() throws CoreException {
		String usr_id = isNotNullAndGet(Fields.USER_ID, ErrCode.PAMAS_ERROR).toString();
		
		User user = userService.find(usr_id);
		Map<String, Object> result = userService.findRelateChildrenUserMap(user);
		
		
		setResp("data", result);
		returnJson();
	}

	// 用户头像上传
	public void uploadUserUrl() throws CoreException {
		String host = Constants.HOST_URL+"/usr/";// "http://120.78.68.118/product/";
													// //"http://127.0.0.1:8002/product/"; //
		String srvPath = "/home/root/images/usr/";// "/home/root/images/usr/";
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String newFileName = ImgUtil.resetFileName(srvPath, delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String usr_id = getPara(Fields.USER_ID);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			if (StrUtils.isNotEmpty(usr_id)) {
				User user = new User();
				user.set(Fields.USER_ID, usr_id);
				user.set(Fields.TIT_URL, img_url);
				userService.save(user);
			}
			System.out.println("头像保存成功");
			setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	// 更新用户
	public void updateUser() throws CoreException {
		String p_thd_id = getData(Fields.P_THD_ID);
		String p_id = null;
		if (StrUtils.isNotEmpty(p_thd_id)) {
			User parent = userService.findByThdId(p_thd_id);
			Assert.notEmpty(parent, ErrCode.P_THD_ID_ERROR);
			p_id = parent.getStr(Fields.USER_ID);
		}
		User user = new User();
		if (p_id != null) {
			Assert.isFalse(p_id.equals(getData(Fields.USER_ID)), ErrCode.P_THD_ID_CANT_BE_SAME_WITHSELF);
			user.set(Fields.P_ID, p_id);

		}
		user.set(Fields.USER_ID, getData(Fields.USER_ID));
		user.set(Fields.THD_ID, getData(Fields.THD_ID));
		user.set(Fields.USER_NAME, getData(Fields.USER_NAME));
		user.set(Fields.PHONE_NO, getData(Fields.PHONE_NO));
		user.set(Fields.TIT_URL, getData(Fields.TIT_URL));
		User user1 = userService.findByThdId(getData(Fields.THD_ID));
		if (user1 != null) {// 判断用户是否已存在
			Assert.isTrue(user1.getStr(Fields.USER_ID).equals(getData(Fields.USER_ID)), ErrCode.THD_ID_IS_EXIST);
		}
		userService.save(user);
		returnJson();
	}

	// 更改用户状态   by eric 2018.09.18 19:47
	public void updateUserState() throws CoreException {
		String usr_id = isNotNullAndGet(Fields.USER_ID, ErrCode.PAMAS_ERROR).toString();
		String status = isNotNullAndGet(Fields.STATE, ErrCode.PAMAS_ERROR).toString();
		User user = userService.find(usr_id);
		if("1".equals(status) || "0".equals(status)){
			user.set(Fields.STATE, status);
			userService.save(user);
		}
		returnJson();
	}

	// 创建新用户
	public void createUser() throws CoreException {
		String p_thd_id = getData(Fields.P_THD_ID);
		String tit_url = StrUtils.isEmpty(getData(Fields.TIT_URL)) ? Constants.DEFAULT_USER_TITLE_URL
				: getData(Fields.TIT_URL);
		String p_id = null;
		if (StrUtils.isNotEmpty(p_thd_id)) {
			User parent = userService.findByThdId(p_thd_id);
			Assert.notEmpty(parent, ErrCode.P_THD_ID_ERROR);
			p_id = parent.getStr(Fields.USER_ID);
		}
		User user = new User();
		if (p_id != null) {
			user.set(Fields.P_ID, p_id);
		}

		user.set(Fields.THD_ID, getData(Fields.THD_ID));
		user.set(Fields.USER_NAME, getData(Fields.USER_NAME));
		user.set(Fields.PHONE_NO, getData(Fields.PHONE_NO));
		user.set(Fields.TIT_URL, tit_url);
		user.set(Fields.CREATE_TIME, new Date());
		user.set(Fields.STATE, Constants.ACCESS_STATE);
		user.set(Fields.USER_TYPE, Constants.USER_TYPE_COMMON);
		Assert.isEmpty(userService.findByThdId(getData(Fields.THD_ID)), ErrCode.THD_ID_IS_EXIST);
		userService.save(user);
		// 添加钱包和基金
		BonusCfg myPaket = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_1);
		BonusCfg bossFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_2);
		BonusCfg helpFund = bonusService.addMyPaket(user.getStr(Fields.USER_ID), Constants.BONUS_CFG_TYP_3);

		returnJson();
	}

	public void deleteUser() throws CoreException {
		String usr_id = getData(Fields.USER_ID);
		User user = userService.find(usr_id);
		Assert.notEmpty(user, ErrCode.USER_UNEXIST);
		userService.deleteById(usr_id);
		returnJson();
	}

	public void uploadUserList() throws CoreException {
		try {
			UploadFile file = getFile();
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			setResp("fileName", newFileName);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	private String resetFileName(String fileName) throws IOException {
		String path = "/home/root/images/product/";
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return path + UuidUtils.getUUID2() + "." + lastName;
	}

	public void downloadUserList() throws IOException {
		try {
			String path = "/home/root/images/product/";
			String fileName = "userList.xls";
			writeExcel("0");
			File file = new File(path, fileName);
			// 本地的一张图片
			if (file.exists()) { // 如果文件存在
				renderFile(file);
			} else {
				renderFile(new File(path, "error.xls"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadConsumeUserList() throws IOException {
		try {
			String path = "/home/root/images/product/";
			String fileName = "userConsumeList.xls";
			writeExcel("1");
			File file = new File(path, fileName);
			// 本地的一张图片
			if (file.exists()) { // 如果文件存在
				renderFile(file);
			} else {
				renderFile(new File(path, "error.xls"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeExcel(String type) {
		int page = 0;
		int size = 200;
		String path = "/home/root/images/product/";
		String fileName = "userList.xls";
		if("1".equals(type)){
			fileName = "userConsumeList.xls";
		}
		Excel excel = new Excel(path, fileName);
		excel.createSheet(Constants.SHEET1);
		createExcelTitle(excel); 
		while(true) {
		  List<Record> userList = userService.findUserList(type,page, size);
		  if(userList==null||userList.isEmpty()) {break;}
		  int begin = page*size;
		  int end = begin+userList.size();
		  int row = begin+1;
		  for (int i = begin; i < end; i++) {
				excel.createRow(Constants.SHEET1, row);
			    createExcelItem(excel,row,userList.get(i-begin)); 
			    row++;
			}
		  page++;
		}
		excel.writeExcel();
	}
	
	private void createExcelTitle(Excel excel) {
		excel.createRow(Constants.SHEET1, 0);
		excel.createCell(Constants.SHEET1, 0, 0, "序号");
		excel.createCell(Constants.SHEET1, 0, 1, "上级推荐名称");
		excel.createCell(Constants.SHEET1, 0, 2, "上级推荐账号");
		excel.createCell(Constants.SHEET1, 0, 3, "用户名称");
		excel.createCell(Constants.SHEET1, 0, 4, "用户账号");
		excel.createCell(Constants.SHEET1, 0, 5, "绑定手机号");
		excel.createCell(Constants.SHEET1, 0, 6, "创建时间");
	}
	
	private void createExcelItem(Excel excel,int row,Record record) {
		excel.createCell(Constants.SHEET1, row, 0, row+"");
		excel.createCell(Constants.SHEET1, row, 1, record.getStr("p_usr_nme"));
		excel.createCell(Constants.SHEET1, row, 2, record.getStr("p_thd_id"));
		excel.createCell(Constants.SHEET1, row, 3, record.getStr("usr_nme"));
		excel.createCell(Constants.SHEET1, row, 4, record.getStr("thd_id"));
		excel.createCell(Constants.SHEET1, row, 5, record.getStr("pho_no"));
		excel.createCell(Constants.SHEET1, row, 6, record.get("crt_tme")+"");
	}
}
