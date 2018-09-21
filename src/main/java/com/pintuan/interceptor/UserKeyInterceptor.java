package com.pintuan.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.pintuan.base.CoreException;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.util.StrUtils;
import com.supyuan.util.encrypt.RSAUtils;

/**
 * 控制台用户截器
 * 
 * @author zjh 2018-5-1
 */
public class UserKeyInterceptor implements Interceptor {


	public void intercept(Invocation ai) {
		dealUserKey(ai);
		ai.invoke();
		reflashUserKey( ai); 
		
		

	}
	
	private void dealUserKey(Invocation ai) throws CoreException {
		BaseProjectController controller = (BaseProjectController)ai.getController();
		String key = controller.getData(Fields.CONSOLE_USER_KEY);
		System.out.println("key="+key);
		if(null==key||StrUtils.isEmpty(key)) return;
		try{
		    String keyMessage = RSAUtils.decrypt(key);
		    controller.setData(Fields.KEY_MESSAGE, keyMessage);
		    
		}catch(Exception e) {
			e.printStackTrace();
			throw new CoreException(ErrCode.WITHOUT_LOGIN);
		}
	}
	
	private void reflashUserKey(Invocation ai) {
		BaseProjectController controller = (BaseProjectController)ai.getController();
		Object key = controller.getData(Fields.REFLASH_USER_KEY);
		if (key!=null) {
			if("".equals(key.toString())) {
				((BaseProjectController)ai.getController()).setResp(Fields.CONSOLE_USER_KEY, "");
				return ;
			}
			encrypt(ai,key.toString());
		}else {
			((BaseProjectController)ai.getController()).setResp(Fields.CONSOLE_USER_KEY, controller.getData(Fields.CONSOLE_USER_KEY));			
		}
	}
	
	private void encrypt(Invocation ai,String key) {
		try {
			((BaseProjectController)ai.getController()).setResp(Fields.CONSOLE_USER_KEY, RSAUtils.encrypt(key));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
