package com.pintuan.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.PosterBoard;
import com.pintuan.model.ProductCfg;
import com.pintuan.model.Scheduling;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.extend.UuidUtils;

public class SchedulingService extends BaseService {

	
	/****/
	public Scheduling find(String amtTyp,String state) {
		String sqlWhere =  "where amt_typ=? and state=? order by sdl_id desc";
		return Scheduling.dao.findFirstByWhere(sqlWhere, amtTyp,state);
	}
	
	/****/
	public Scheduling find(String amtTyp) {
		String sqlWhere =  "where amt_typ=? order by sdl_id desc";
		return Scheduling.dao.findFirstByWhere(sqlWhere, amtTyp);
	}
	
	public List<ProductCfg> findProCfgBySdlId(String sdlId) {
		String sqlWhere =  "where sdl_id=?";
		return ProductCfg.dao.findByWhere(sqlWhere, sdlId);
	}
	
	public Scheduling add(int grid,int tur_num,BigDecimal amt,String amt_typ,int tar_num) {
		Scheduling scheduling =  new Scheduling();
		//scheduling.set(Fields.SDL_ID, UuidUtils.getUUID2());
		scheduling.set(Fields.GRID, grid);
		scheduling.set(Fields.TUR_NUM, tur_num);
		scheduling.set(Fields.BUY_CNT_SUM, 0);
		scheduling.set(Fields.AMT, amt);
		scheduling.set(Fields.AMT_TYP, amt_typ);
		scheduling.set(Fields.TAR_NUM, tar_num);
		scheduling.set(Fields.CREATE_TIME, new Date());
		scheduling.set(Fields.STATE, Constants.SDL_STATE_DOING);
		scheduling.save();
		return scheduling;
	}
	
	/**
	 * 商品购买数量加一
	 */
	public void addOne(String amt_typ) {
		String sql = "update pt_scheduling set buy_cnt_sum=buy_cnt_sum+1 where amt_typ=? and state=?";
		Db.update(sql,amt_typ,Constants.SDL_STATE_DOING);
	}
	
	/**
	 * 更新状态
	 */
	public void updateState(String sdl_id,String state) {
		String sql = "update pt_scheduling set state=?,end_tme=? where sdl_id=? ";
		Db.update(sql,state,new Date(),sdl_id);
	}
	
	
	/**
	 * 查询用户下单的排单
	 */
	public long findUserCanGainBonus(int sdl_id,String amt_typ) {
		String sql = "select count(1) from pt_scheduling where amt_typ=? and crt_tme >= (select crt_tme from pt_scheduling where sdl_id=?)";
		long count = Db.queryLong(sql,amt_typ,sdl_id);
		System.out.println("*****count="+count);
		return count;
	}

}
