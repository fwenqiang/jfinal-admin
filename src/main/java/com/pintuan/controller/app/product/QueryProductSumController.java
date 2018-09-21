package com.pintuan.controller.app.product;

import com.pintuan.base.CoreException;
import com.pintuan.common.Fields;
import com.pintuan.service.ProductService;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 查询商品总数
 * 
 * @author zjh 2018-5-6
 */
@ControllerBind(controllerKey = "/pintuan/app/queryProductSum")
public class QueryProductSumController extends BaseProjectController {
	private ProductService productService = new ProductService();

	public void index() throws CoreException {
		String amtTyp = getData(Fields.AMT_TYP);
		String proTyp = getData(Fields.PRO_TYP);

		long sum = productService.findSum(amtTyp, proTyp);
		setResp(Fields.SUM, sum);
		returnJson();
	}

}
