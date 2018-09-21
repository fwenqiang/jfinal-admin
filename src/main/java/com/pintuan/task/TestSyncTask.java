package com.pintuan.task;

import java.util.List;

import com.pintuan.common.Constants;
import com.pintuan.model.Img;
import com.pintuan.service.ImageService;
import com.supyuan.util.task.Task;


public class TestSyncTask extends Task {
	private int sum;
	public TestSyncTask(int i) {
		this.sum = i;
	}
	private ImageService imageService = new ImageService();
	@Override
	public void customRun() {
		try {
			
			if(sum==0) Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Img> imgList = imageService.findByThdId("0001", "1",Constants.ACCESS_STATE);
		System.out.println("***********["+this.sum+"]imgList="+imgList);
	}


}
