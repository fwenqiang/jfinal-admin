package com.pintuan.controller.console.business;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.jfinal.upload.UploadFile;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.PosterBoard;
import com.pintuan.service.PosterBoardService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

/**
 * 广告板模块
 * 
 * @author zjh 2018-3-24
 */
@ControllerBind(controllerKey = "/pintuan/console/poster")
public class PosterBoardController extends BaseProjectController {
	PosterBoardService posterBoardService = new PosterBoardService();

	public void index() throws CoreException {
		queryPoster();
	}

	// 广告查询
	public void queryPoster() throws CoreException {
		String title = getData(Fields.TITLE);
		String bor_typ = getData(Fields.BOR_TYP);
		int page = getNPage();
		int size = getNSize();
		List<PosterBoard> posterBoardList = posterBoardService.findByTitleAndType(title, bor_typ, page, size);
		long total = posterBoardService.findByTitleAndTypeSize(title, bor_typ);
		setResp("list", DBModelUtils.toMaps(posterBoardList));
		setResp("total", total);
		returnJson();
	}

	// 广告删除
	public void deletePoster() throws CoreException {
		String bor_id = isNotNullAndGet(Fields.BOR_ID, ErrCode.BOR_ID_IS_NULL).toString();
		posterBoardService.delete(bor_id);
		returnJson();
	}

	// 广告图片上传
	public void uploadImg() throws CoreException {
		String host = Constants.HOST_URL+"/poster/"; //
		try {
			UploadFile file = getFile(); // 要先获取文件之后才能获取参数
			File delfile = file.getFile();
			String newFileName = resetFileName(delfile.getName());
			FileUtils.copyFile(delfile, new File(newFileName));
			delfile.delete();
			String bor_id = getPara(Fields.BOR_ID);
			String img_url = host + newFileName.split("/")[newFileName.split("/").length - 1];
			if (StrUtils.isNotEmpty(bor_id)) {
				posterBoardService.saveImg(bor_id, img_url);
			}
			System.out.println("上传文件成功");
			setResp("filePath", img_url);
		} catch (Exception e) {
			System.out.println("上传文件失败");
		}
		returnJson();
	}

	private String resetFileName(String fileName) throws IOException {
		String path = "/home/root/images/poster/"; // /home/root/images/poster/
		if (StrUtils.isEmpty(fileName))
			return "nullNmefile";
		String lastName = fileName.split("\\.")[fileName.split("\\.").length - 1];
		return path + UuidUtils.getUUID2() + "." + lastName;
	}

	// 更新广告
	public void updatePoster() throws CoreException {
		Map<String, Object> poster = getContext();
		posterBoardService.update(poster);
		returnJson();
	}

	// 更新广告
	public void addPoster() throws CoreException {
		Map<String, Object> poster = getContext();
		posterBoardService.add(poster);
		returnJson();
	}

}
