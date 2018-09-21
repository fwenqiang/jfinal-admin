package com.pintuan.schedule;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.supyuan.util.task.Task;
import com.supyuan.util.task.AsyncTaskExcutor;

/**
 * schedule有以下四种形式：

schedule(task,time);
schedule(task,time,period);
schedule(task,delay);
schedule(task,delay,period);
其中，task是我们需要执行的任务，time是首次执行任务的时刻，period是执行一次task的时间间隔，delay是执行第一次任务前的延迟时间，单位均为毫秒。
 * 
 * @author zjh 2018-3-24
 */
public class SystemSchedule {


	public static void main(String[] args) {
		SystemSchedule s = new SystemSchedule();
		s.test();
	}
	
	public  void test() {
		try {
			
			//as.postTask(this.createTask(), 0, 2);
			Timer timer=new Timer();
	        
	        Calendar calendar =Calendar.getInstance();
	        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        System.out.println("当前时间是 "+sf.format(calendar.getTime()));
	        
	        timer.schedule(new TimerTask(){
	        	private int i = 0;
	            public void run(){
	                Calendar calendar =Calendar.getInstance();
	                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                System.out.println("执行此次任务的时间是 "+sf.format(calendar.getTime()));
	                AsyncTaskExcutor as = AsyncTaskExcutor.getInstance();
	                SystemSchedule s = new SystemSchedule();
	                //as.postTask(s.createTask(), 0, 2000); //循环执行
	                as.postTask(s.createTask());
	                i++;
	                if(i>4) {
	                	cancel(); //取消任务
	                	System.out.println("clean");
	                	System.exit(0);
	                	System.out.println("exit"); //退出线程
	                }
	            }
	        },0,2000);//延迟的时间
	        System.out.println("this is out");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	public Task createTask() {
		Task a = new Task() {
			@Override
			public void customRun() {
				System.out.println("执行此次任务的时间是 ");
				
			}};
		return a;
	}
}
