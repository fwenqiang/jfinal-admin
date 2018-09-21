package com.pintuan.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.pintuan.common.Constants;
import com.pintuan.model.PosterBoard;
import com.pintuan.model.Product;
import com.pintuan.model.ProductCfg;
import com.pintuan.model.Scheduling;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

public class ProductService extends BaseService {

	
	public List<Product> find(String amtTyp,String proTyp,int page,int size) {
		String sqlWhere =  "where amt_typ=? and pro_typ=? and state=? order by crt_tme desc limit ?,? ";
		return Product.dao.findByWhere(sqlWhere, amtTyp,proTyp,Constants.ACCESS_STATE,page*size,size);
	}
	
	public Product find(String amtTyp,String proTyp) {
		String sqlWhere =  "where amt_typ=? and pro_typ=? and state=? order by crt_tme desc ";
		return Product.dao.findFirstByWhere(sqlWhere, amtTyp,proTyp,Constants.ACCESS_STATE);
	}
	
	
	public Product findById(String id) {
		return Product.dao.findById(id);
	}
	
	public List<Product> findByNameAndType(String pro_nme, String pro_typ,int page,int size) {
		String whereSql = " where state!='2' ";
		if(StrUtils.isNotEmpty(pro_nme)) {
			whereSql = whereSql + " and pro_nme like concat('%','"+pro_nme+"', '%')";
		}
		if(StrUtils.isNotEmpty(pro_typ)) { 
			whereSql = whereSql + " and pro_typ like concat('%','"+pro_typ+"', '%')";
		}
		whereSql +=" order by crt_tme desc limit "+(page * size)+","+size;
		return Product.dao.findByWhere(whereSql);
	}
	
	/**
	 * 查询条数
	 */
	public long findByNameAndTypeSize(String pro_nme, String pro_typ) {
		String whereSql = "select count(1) from pt_product where state!='2' ";
		if(StrUtils.isNotEmpty(pro_nme)) {
			whereSql = whereSql + " and pro_nme like concat('%','"+pro_nme+"', '%')";
		}
		if(StrUtils.isNotEmpty(pro_typ)) { 
			whereSql = whereSql + " and pro_typ like concat('%','"+pro_typ+"', '%')";
		}
		return Db.queryLong(whereSql);
	}
	
	
	/**
	 * 查询总数
	 */
	public long findSum(String amt_typ, String pro_typ) {
		String whereSql = "select count(1) from pt_product where state!='2' ";
		if(StrUtils.isNotEmpty(amt_typ)) {
			whereSql = whereSql + " and amt_typ='"+amt_typ+"'";
		}
		if(StrUtils.isNotEmpty(pro_typ)) { 
			whereSql = whereSql + " and pro_typ='"+pro_typ+"'";
		}
		return Db.queryLong(whereSql);
	}
	/**
	 * 添加商品
	 */
	public void add(Map<String,Object> srcMap) {
		Product product  = new Product();
		product.set("pro_id", UuidUtils.getUUID2());
		product.set("pro_nme", srcMap.get("pro_nme"));
		product.set("pro_typ", srcMap.get("pro_typ"));
		product.set("amt_typ", srcMap.get("amt_typ"));
		product.set("pro_amt", srcMap.get("pro_amt"));
		if(srcMap.get("def_url")!=null&&StrUtils.isNotEmpty((String)srcMap.get("def_url"))) {product.set("def_url", srcMap.get("def_url"));}
		product.set("buy_cnt_sum", 0);
		product.set("crt_tme", new Date());
		product.set("frt_typ_lab", srcMap.get("frt_typ_lab"));
		product.set("snd_typ_lab", srcMap.get("snd_typ_lab"));
		product.set("state", Constants.ACCESS_STATE);
		product.save();
	}
	
	/**
	 * 更新商品
	 */
	public void update(Map<String,Object> srcMap) {
		Product product  = new Product();
		product.set("pro_id", srcMap.get("pro_id"));
		product.set("pro_nme", srcMap.get("pro_nme"));
		product.set("pro_typ", srcMap.get("pro_typ"));
		product.set("amt_typ", srcMap.get("amt_typ"));
		product.set("pro_amt", srcMap.get("pro_amt"));
		if(srcMap.get("def_url")!=null&&StrUtils.isNotEmpty((String)srcMap.get("def_url"))) {product.set("def_url", srcMap.get("def_url"));}

		product.set("frt_typ_lab", srcMap.get("frt_typ_lab"));
		product.set("snd_typ_lab", srcMap.get("snd_typ_lab"));
		product.set("state", srcMap.get("state"));
		product.update();
	}
	
	/**
	 * 删除商品
	 */
	public void delete(String pro_id) {
		String sql = "update pt_product set state=? where pro_id=?";
		Db.update(sql,Constants.DELETE_STATE,pro_id);
	}
	
	/**
	 * 保存默认图片
	 */
	public void saveDefaultImage(String pro_id,String img_url) {
		String sql = "update pt_product set def_url=? where pro_id=?";
		Db.update(sql,img_url,pro_id);
	}
	
	public void saveVideo(String pro_id,String video_url) {
		String sql = "update pt_product set video_url=? where pro_id=?";
		Db.update(sql,video_url,pro_id);
	}
	
	/**
	 * 商品购买数量加一
	 */
	public void addOne(String pro_id) {
		String sql = "update pt_product set buy_cnt_sum=buy_cnt_sum+1 where pro_id=?";
		Db.update(sql,pro_id);
	}
	
	/**
	 * （商品配置表）排单商品购买数量加一
	 */
	public void addOneInProCfg(String amt_typ,String pro_typ) {
		String sql = "update pt_product_cfg set buy_cnt=buy_cnt+1 where amt_typ=? and pro_typ=? and state=?";
		Db.update(sql,amt_typ,pro_typ,Constants.PROCFG_STATE_DONG);//状态为进行中
	}
	
	/**
	 * 更新商品配置表状态
	 */
	public void updateProCfgState(String sdl_id,String state) {
		String sql = "update pt_product_cfg set state=?,end_tme=? where sdl_id=? ";
		Db.update(sql,state,new Date(),sdl_id);
	}
	
	/**
	 * 添加商品
	 */
	public ProductCfg addProductCfg(Scheduling scheduling,Product product) {
		if(product==null) return null;
		ProductCfg productCfg  = new ProductCfg();
		productCfg.set("pro_cfg_id", UuidUtils.getUUID2());
		//productCfg.set("pro_id", product.get("pro_id"));
		productCfg.set("sdl_id", scheduling.get("sdl_id"));
		productCfg.set("buy_cnt", 0);
		productCfg.set("pro_amt", product.get("pro_amt"));
		productCfg.set("pro_nme", product.get("pro_nme"));
		productCfg.set("pro_typ", product.get("pro_typ"));
		productCfg.set("amt_typ", product.get("amt_typ"));
		productCfg.set("def_url", product.get("def_url"));
		productCfg.set("tur_num", scheduling.get("tur_num"));
		productCfg.set("grid", scheduling.get("grid"));
		productCfg.set("crt_tme", new Date());
		productCfg.set("state", Constants.SDL_STATE_DOING);
		productCfg.save();
		return productCfg;
	}
	

}
