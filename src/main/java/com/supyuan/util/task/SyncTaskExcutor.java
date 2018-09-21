/**
 * Copyright 2015-2025 FLY的狐狸(email:jflyfox@sina.com qq:369191470).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.supyuan.util.task;

import org.springframework.core.task.SyncTaskExecutor;

/**
 * 同步任务执行器
 * 
 * zjh
 */
public class SyncTaskExcutor {
	private static SyncTaskExcutor instance = new SyncTaskExcutor();
	private SyncTaskExecutor syncTaskExecutor = null;

	public static SyncTaskExcutor getInstance() {
		return instance;
	}

	private SyncTaskExcutor() {
		syncTaskExecutor = new SyncTaskExecutor();
	}

	private void postTask(Task t) {
		syncTaskExecutor.execute(t);
	}

	private void postTask(Task t, long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		syncTaskExecutor.execute(t);
	}

	/**
	 * 自定义单次执行方法
	 * 
	 * @param t
	 */
	public static void exc(Task t) {
		SyncTaskExcutor.getInstance().postTask(t);
	}

	/**
	 * 自定义延迟单次执行方法
	 * 
	 * @param t
	 */
	public static void exc(Task t, long delay) {
		SyncTaskExcutor.getInstance().postTask(t, delay);
	}

}
