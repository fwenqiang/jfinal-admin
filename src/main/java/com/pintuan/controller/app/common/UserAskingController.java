package com.pintuan.controller.app.common;

import com.pintuan.base.CoreException;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 用户问答
 * 
 * @author zjh 2018-5-1
 */
@ControllerBind(controllerKey = "/pintuan/app/userAsking")
public class UserAskingController extends BaseProjectController {
	public void index() throws CoreException {
		String question = getData("question");
		String answer = "";
        switch(question) {
        case "01": answer =answer1();break;
        case "02": answer =answer2();break;
        case "03": answer =answer3();break;
        case "04": answer =answer4();break;
        case "05": answer =answer5();break;
        case "06": answer =answer6();break;
        default:answer =answer();
        }
        
        setResp("answer", answer);
        returnJson();
	}	
	
	private String answer() {
		return "请输入以下问题序号提问：\n"
			  +"01:问题1\n"
			  + "02：问题2\n"
			  + "03：问题3\n"
			  + "04：问题4\n"
			  + "05：问题5\n"
			  + "06：问题6";
	}
	private String answer1() {
		return "这是问题1的答案";
	}
	private String answer2() {
		return "这是问题2的答案";
	}
	private String answer3() {
		return "这是问题3的答案";
	}
	private String answer4() {
		return "这是问题4的答案";
	}
	private String answer5() {
		return "这是问题5的答案";
	}
	private String answer6() {
		return "这是问题6的答案";
	}
	
	
}
