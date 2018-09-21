package com.pintuan.controller.app.image;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.IdentifyCode;
import com.pintuan.model.Img;
import com.pintuan.model.PosterBoard;
import com.pintuan.service.CommonService;
import com.pintuan.service.ImageService;
import com.pintuan.service.PosterBoardService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询商品展示图片列表
 *                                      
 *                                             
 * @author zjh 2018-4-26
 */
@ControllerBind(controllerKey = "/pintuan/app/queryProDisplayImgList")
public class QueryProDisplayImgListController extends BaseProjectController {
	private ImageService imageService = new ImageService();
	public void index() throws CoreException {
		String pro_id = isNotNullAndGet(Fields.PRO_ID,ErrCode.PRO_ID_IS_NULL).toString();
        List<Img> imgList = imageService.findByThdId(pro_id, "1",Constants.ACCESS_STATE);
        setResp(Fields.IMG_URL_LIST, DBModelUtils.toMapsInc(imgList,Fields.IMG_URL,Fields.IMG_SRT));
        returnJson();
	}
	
	
}
