package com.pintuan.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;
import com.supyuan.component.base.BaseProjectModel;

/**
 *数据库bean工具
 */
public class DBModelUtils {

	
    /**
     * 只适用于 BaseProjectModel
     * @param list BaseProjectModel
     * @return List<Map<String,Object>>
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String,Object>> toMaps(List list) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(list==null||list.isEmpty()) {
			return result;
		}
		
		for(Object model :list) {
			Map<String,Object> map = ((BaseProjectModel)model).getAttrs();
			result.add(map);
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String,Object>> toMaps(List list,Class cla) {
		if(list==null||list.isEmpty()) {
			return null;
		}
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		if(BaseProjectModel.class.getName().equals(cla.getName())) {
		for(Object model :list) {
			Map<String,Object> map = ((BaseProjectModel)model).getAttrs();
			result.add(map);
		}
		}else if(Record.class.getName().equals(cla.getName())) {
			for(Object model :list) {
				Map<String,Object> map = ((Record)model).getColumns();
				result.add(map);
			}
		}else {  //一般的用get,set处理
			
		}
		return result;
	}
	
	/**
	 * 只包含args的属性在内
	 * **/
	@SuppressWarnings({ "rawtypes" })
	public static List<Map<String,Object>> toMapsInc(List list,String... args) {
		if(list==null||list.isEmpty()) {
			return null;
		}
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Object model :list) {
			Map<String,Object> map = new HashMap<String,Object>();
			for(String arg:args) {
				map.put(arg, ((BaseProjectModel)model).get(arg));
			}
			result.add(map);
		}
		return result;
	}
	
	/**
	 * 排除掉args属性
	 * **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String,Object>> toMapsExc(List list,String... args) {
		if(list==null||list.isEmpty()) {
			return null;
		}
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		for(Object model :list) {
			Map<String,Object> map = ((BaseProjectModel)model).getAttrs();
			for(String arg:args) {
				map.remove(arg);
			}
			result.add(map);
		}
		return result;
	}
	
	/**
	 * 获得查询的whereSql
	 * **/
	public static String getWhereSql(Map<String,Object> parm) {
		String where = "where 1=1 ";
		if(parm==null||parm.isEmpty()) return where;
		for (Map.Entry<String, Object> entry : parm.entrySet()) { 
		  if(entry.getValue()!=null) {
			  where +=entry.getKey()+"=? ";
		  }
		}
		return where;
	}
	
	/**
	 * 获得查询的参数
	 * **/
	public static List<Object> getParms(Map<String,Object> parm) {
		if(parm==null||parm.isEmpty()) return null;
		List<Object> Parms = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : parm.entrySet()) { 
		  if(entry.getValue()!=null) {
			  Parms.add(entry.getValue());
		  }
		}
		return Parms;
	}
	
	/**
	 * 判断是否为空
	 * @param <E>
	 * **/
	public static <E> boolean isEmpty(Collection<E> collection) {
		if(collection==null||collection.isEmpty()) return true;
		return false;
	}
	
	public static void main(String[] args) {
		Class cla = BaseProjectModel.class;
		System.out.println(cla.getName());
		System.out.println(BaseProjectModel.class.getName().equals(cla.getName()));
	}
	
	
}
