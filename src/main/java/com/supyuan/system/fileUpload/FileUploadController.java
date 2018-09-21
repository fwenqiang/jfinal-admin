package com.supyuan.system.fileUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.jfinal.component.db.SQLUtils;
import com.supyuan.util.StrUtils;
import com.supyuan.util.entity.RespBody;

/**
 * 文件上传
 * 
 * @author zjh 2018-3-23
 */
@ControllerBind(controllerKey = "/fileUpload")
public class FileUploadController extends BaseProjectController {

	private static final String path = "/pages/system/department/department_";

	public void index() {
		list();
	}
	
	public void list() {
		System.out.println(">>>>>>");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("abcd", "sdkljf");
		map.put("name", "ccc");
		List<String> l = new ArrayList<String>();
		l.add("w");
		l.add("q");
		map.put("list", l);
		renderJson(JSONUtils.toJSONString(map));
	}
	
	public void upload(){
        RespBody resp = new RespBody(getResponse());
        try {
            UploadFile file = getFile();
            System.out.println(getContext());
           // File delfile = file.getFile();
            //System.out.println("--------file--------");
            //System.out.println(file.getUploadPath()+"\\"+file.getFileName());
            //System.out.println("=========="+delfile.getPath());
            resp.setResult(true);
            Map<String ,String> map = new HashMap<String, String>();
            //map.put("filePath", delfile.getPath());
            //map.put("fileSize", delfile.length()/1024+"");
            map.put("zjh", "this is zjh msg");
            resp.setData(map);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("文件上传失败");
            resp.setMsg("文件上传失败");
        }
        
        renderJson(resp);
    }

	

	

	

}
