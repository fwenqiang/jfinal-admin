package com.pintuan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.common.Constants;
import com.pintuan.model.PosterBoard;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.BeanUtils;

public class PosterBoardService extends BaseService {

	
	public List<PosterBoard> find(String location,String state) {
		return PosterBoard.dao.findByWhere("where location=? and state=?", location,state);
	}
	
	public List<PosterBoard> findByTitleAndType(String title,String bor_typ,int page,int size) {
		String whereSql = " where 1=1 ";
		if(StrUtils.isNotEmpty(title)) {
			whereSql = whereSql + " and title like concat('%','"+title+"', '%')";
		}
		if(StrUtils.isNotEmpty(bor_typ)) { 
			whereSql = whereSql + " and bor_typ ='"+bor_typ+"'";
		}
		whereSql +=" order by bor_id limit "+(page * size)+","+size;
		return PosterBoard.dao.findByWhere(whereSql);
	}
	
	/**
	 * 查询条数
	 */
	public long findByTitleAndTypeSize(String title,String bor_typ) {
		String whereSql = "select count(1) from pt_poster_board where 1=1 ";
		if(StrUtils.isNotEmpty(title)) {
			whereSql = whereSql + " and title like concat('%','"+title+"', '%')";
		}
		if(StrUtils.isNotEmpty(bor_typ)) { 
			whereSql = whereSql + " and bor_typ ='"+bor_typ+"'";
		}
		return Db.queryLong(whereSql);
	}
	
	/**
	 * 删除广告
	 */
	public void delete(String bor_id) {
		String sql = "delete from pt_poster_board where bor_id=?";
		Db.update(sql,bor_id);
	}
	
	/**
	 * 保存图片
	 */
	public void saveImg(String bor_id,String img_url) {
		String sql = "update pt_poster_board set bor_url=? where bor_id=?";
		Db.update(sql,img_url,bor_id);
	}
	
	/**
	 * 更新
	 */
	public void update(Map<String,Object> srcMap) {
		PosterBoard posterBoard  = new PosterBoard();
		posterBoard.set("bor_id", srcMap.get("bor_id"));
		posterBoard.set("title", srcMap.get("title"));
		posterBoard.set("bor_typ", srcMap.get("bor_typ"));
		posterBoard.set("content", srcMap.get("content"));
		posterBoard.set("location", srcMap.get("location"));
		if(srcMap.get("bor_url")!=null&&StrUtils.isNotEmpty((String)srcMap.get("bor_url"))) {posterBoard.set("bor_url", srcMap.get("bor_url"));}
		posterBoard.set("bor_pam", srcMap.get("bor_pam"));
		posterBoard.set("state", srcMap.get("state"));
		posterBoard.update();
	}
	
	/**
	 * 添加
	 */
	public void add(Map<String,Object> srcMap) {
		PosterBoard posterBoard  = new PosterBoard();
		//posterBoard.set("bor_id", srcMap.get("bor_id"));
		posterBoard.set("title", srcMap.get("title"));
		posterBoard.set("bor_typ", srcMap.get("bor_typ"));
		posterBoard.set("content", srcMap.get("content"));
		posterBoard.set("location", srcMap.get("location"));
		if(srcMap.get("bor_url")!=null&&StrUtils.isNotEmpty((String)srcMap.get("bor_url"))) {posterBoard.set("bor_url", srcMap.get("bor_url"));}
		posterBoard.set("bor_pam", srcMap.get("bor_pam"));
		posterBoard.set("state", Constants.ACCESS_STATE);
		posterBoard.save();
	}
	
	public static void main(String[] args) {
		PosterBoard posterBoard  = new PosterBoard();
		Map<String,Object> srcMap = new HashMap<String,Object>();
		srcMap.put("bor_id", "zjh");
		srcMap.put("title", "zjh");
		BeanUtils.copy(srcMap, posterBoard);
		posterBoard.save();
	}

}
