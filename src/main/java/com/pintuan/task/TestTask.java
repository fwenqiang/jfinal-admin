package com.pintuan.task;

import java.util.List;

import com.pintuan.common.Constants;
import com.pintuan.model.Img;
import com.pintuan.service.ImageService;
import com.supyuan.util.task.Task;


public class TestTask extends Task {
	private int sum;
	public TestTask(String key,int i) {
		this.sum = i;
		setTaskName(key);
	}
	private ImageService imageService = new ImageService();
	@Override
	public void customRun() {
		List<Img> imgList = imageService.findByThdId("0001", "1",Constants.ACCESS_STATE);
		System.out.println("***********["+this.sum+"]imgList="+imgList);
	}


}
