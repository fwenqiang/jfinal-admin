package com.pintuan.controller.app.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
 * 查询商品列表
 * 
 * @author zjh 2018-4-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryProductList")
public class QueryProductListController extends BaseProjectController {
	private ProductService productService = new ProductService();
	private ImageService imageService = new ImageService();

	public void index() throws CoreException {
		String amtTyp = isNotNullAndGet(Fields.AMT_TYP, ErrCode.AMT_TYP_IS_NULL).toString();
		String proTyp = isNotNullAndGet(Fields.PRO_TYP, ErrCode.PRO_TYP_IS_NULL).toString();
		int page = getNPage();
		int size = getNSize();

		List<Product> productList = productService.find(amtTyp, proTyp, page, size);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Product product:productList) {
			Map<String,Object> map = product.getAttrs();
			List<Img> imgList = imageService.findByThdId(product.getStr(Fields.PRO_ID), "1",Constants.ACCESS_STATE);
			map.put(Fields.IMG_URL_LIST, DBModelUtils.toMaps(imgList));
			result.add(map);
		}
		
		setResp(Fields.PRODUCT_LIST, result);
		returnJson();
	}

}
