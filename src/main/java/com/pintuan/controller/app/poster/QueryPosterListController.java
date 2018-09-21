package com.pintuan.controller.app.poster;

import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.IdentifyCode;
import com.pintuan.model.PosterBoard;
import com.pintuan.service.CommonService;
import com.pintuan.service.PosterBoardService;
import com.pintuan.service.UserService;
import com.pintuan.util.Assert;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询广告列表
 *                                      
 *                                             
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryPosterList")
public class QueryPosterListController extends BaseProjectController {
	private PosterBoardService posterBoardService = new PosterBoardService();
	public void index() throws CoreException {
		String location = isNotNullAndGet(Fields.LOCATION,ErrCode.LOCATION_IS_NULL).toString();
        List<PosterBoard> boardList = posterBoardService.find(location, Constants.ACCESS_STATE);
        setResp(Fields.POSTER_LIST, DBModelUtils.toMaps(boardList));
        returnJson();
	}
	
	
}
