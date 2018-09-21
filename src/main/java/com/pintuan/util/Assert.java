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

package com.pintuan.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pintuan.base.CoreException;
import com.supyuan.util.StrUtils;

/**
 * 断言
 * 
 * @author zjh 2018-4-6
 */
public class Assert {


	@SuppressWarnings("rawtypes")
	public static void notEmpty(Object val,String errCode) {
		if(val==null) {
			throw new CoreException(errCode);
		}
		if(val instanceof String) {
			if(StrUtils.empty(val.toString().trim())){
				throw new CoreException(errCode);
			}
		}
		
		if(val instanceof Map) {
			if(((Map)val).isEmpty()) {
				throw new CoreException(errCode);
			}
		}
		
		if(val instanceof List) {
			if(((List)val).isEmpty()) {
				throw new CoreException(errCode);
			}
		}
		
		if(val instanceof Set) {
			if(((Set)val).isEmpty()) {
				throw new CoreException(errCode);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void isEmpty(Object val,String errCode) {
		if(val==null) {
			return ;
		}
		if(val instanceof String) {
			if(StrUtils.empty(val.toString().trim())){
				return ;
			}
		}
		
		if(val instanceof Map) {
			if(((Map)val).isEmpty()) {
				return ;
			}
		}
		
		if(val instanceof List) {
			if(((List)val).isEmpty()) {
				return ;
			}
		}
		
		if(val instanceof Set) {
			if(((Set)val).isEmpty()) {
				return ;
			}
		}
		throw new CoreException(errCode);
	}
	
	public static void isTrue(boolean val,String errCode) {
		if(!val) {
			throw new CoreException(errCode);
		}
	}
	
	public static void isFalse(boolean val,String errCode) {
		if(val) {
			throw new CoreException(errCode);
		}
	}
		
	public static void main(String[] args) {
		Object val = "    ";
		System.out.println(StrUtils.empty(val));
		System.out.println(StrUtils.empty(val.toString().trim()));
		System.out.println(StrUtils.empty(null));
		
		Map<String,String> map = new HashMap<String,String>();
		Assert.isEmpty(map, "sdfsf");
	}

}
