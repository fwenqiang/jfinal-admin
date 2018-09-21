package com.pintuan.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.model.User;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.util.StrUtils;

/**
 * APP
 * 用户认证拦截器
 * 用户必须登录
 * 
 * @author zjh 2018-4-20
 */
public class CheckUserKeyInterceptor implements Interceptor {


	public void intercept(Invocation ai) {
		dealUserKey(ai);
		ai.invoke();
	}
	
	private void dealUserKey(Invocation ai) throws CoreException {
		BaseProjectController controller = (BaseProjectController)ai.getController();
		String key = controller.getData(Fields.USER_IDENTIFY_KEY);
		if(null==key||StrUtils.isEmpty(key)) {
			throw new CoreException(ErrCode.WITHOUT_LOGIN);
		}
		User user = User.dao.findById(key);
		Assert.notEmpty(user, ErrCode.USER_UNEXIST);
		controller.setAttribute(Fields.ATTR_USER_ENTITY, user);
	}
	
}
