package com.pintuan.controller.console.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.pintuan.base.CoreException;
import com.pintuan.interceptor.HeaderResponseInterceptor;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;
import com.supyuan.jfinal.component.db.SQLUtils;
import com.supyuan.system.role.SysRole;
import com.supyuan.util.StrUtils;
import com.supyuan.util.entity.RespBody;

/**
 * 公用
 * 
 * @author zjh 2018-3-24
 */
@ControllerBind(controllerKey = "/pintuan/test")
public class TestController extends BaseProjectController {

	public void index() throws CoreException {
		list();
	}
	
	
	public void list() {
		SysRole model = getModelByAttr(SysRole.class);

		SQLUtils sql = new SQLUtils(" from sys_role t where 1=1 ");
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			// 查询条件
			sql.whereLike("name", model.getStr("name"));
		}
		
		// 排序
		String orderBy = getBaseForm().getOrderBy();
		if (StrUtils.isEmpty(orderBy)) {
			sql.append(" order by sort,id desc");
		} else {
			sql.append(" order by ").append(orderBy);
		}

		String sqlSelect = "select t.* "
				+ ",(select group_concat(m.name) from sys_role_menu rm left JOIN  sys_menu m ON rm.menuid = m.id where rm.roleid = t.id ) as menus ";

		Page<SysRole> page = SysRole.dao.paginate(getPaginator(), sqlSelect, //
				sql.toString().toString());

		// 下拉框
		setAttr("page", page);
		setAttr("attr", model);
		System.out.println(page);
		System.out.println(model);
		renderJson("");
	}

	

}
