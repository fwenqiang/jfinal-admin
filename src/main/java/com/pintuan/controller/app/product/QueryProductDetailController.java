package com.pintuan.controller.app.product;

import java.util.List;

import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.Img;
import com.pintuan.model.Product;
import com.pintuan.service.ImageService;
import com.pintuan.service.ProductService;
import com.pintuan.util.DBModelUtils;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询商品详情
 * 
 * @author zjh 2018-4-26
 */
@ControllerBind(controllerKey = "/pintuan/app/queryProductDetail")
public class QueryProductDetailController extends BaseProjectController {
	private ProductService productService = new ProductService();
	private ImageService imageService = new ImageService();

	public void index() throws CoreException {
		String pro_id = isNotNullAndGet(Fields.PRO_ID, ErrCode.PRO_ID_IS_NULL).toString();

		Product product = productService.findById(pro_id);
		if(product==null) {
			return ;
		}
		setRespMap(product.getAttrs());
		List<Img> img_url_list=imageService.findByThdId(pro_id, "1",Constants.ACCESS_STATE);
		List<Img> detail_img_list=imageService.findByThdId(pro_id, "2",Constants.ACCESS_STATE);
		setResp(Fields.IMG_URL_LIST, DBModelUtils.toMaps(img_url_list));
		setResp(Fields.DETAIL_IMG_LIST, DBModelUtils.toMaps(detail_img_list));
		returnJson();
	}

}
