package com.pintuan.service;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.pintuan.common.Constants;
import com.pintuan.common.Fields;
import com.pintuan.model.Invoice;
import com.pintuan.model.Order;
import com.pintuan.model.Product;
import com.supyuan.jfinal.base.BaseService;
import com.supyuan.util.StrUtils;
import com.supyuan.util.extend.UuidUtils;

public class OrderService extends BaseService {

	public Order findOne(String ord_id) {
		return Order.dao.findById(ord_id);
	}

	// 添加订单
	public Order add(String usr_id, Product product) {
		Order order = new Order();
		order.set(Fields.ORD_ID, UuidUtils.randomNumStr(15));
		order.set(Fields.USER_ID, usr_id);
		order.set(Fields.CREATE_TIME, new Date());
		order.set(Fields.STATE, Constants.ORDER_STATE_BEGIN);
		order.set(Fields.PRO_ID, product.get(Fields.PRO_ID));
		order.set(Fields.PRO_NME, product.get(Fields.PRO_NME));
		order.set(Fields.PRO_AMT, product.get(Fields.PRO_AMT));
		order.set(Fields.UPDATE_TIME, new Date());
		order.save();
		return order;
	}

	// 查询用户已下过的订单
	public Order findByUserId(String usr_id, String amt_typ) {
		String sql = "select o.* from pt_order o left join  pt_product p on o.pro_id=p.pro_id where o.usr_id=? and o.state in ('S','T','E') and p.amt_typ=?";
		return Order.dao.findFirst(sql, usr_id, amt_typ);
	}

	//
	public Order findByState(String usr_id, String amt_typ, String state) {
		String sql = "select o.* from pt_order o left join  pt_product p on o.pro_id=p.pro_id where o.usr_id=? and o.state=? and p.amt_typ=?";
		return Order.dao.findFirst(sql, usr_id, state, amt_typ);
	}

	// 取消订单
	public Order abordOrder(Order order) {
		order.set(Fields.CREATE_TIME, new Date());
		order.set(Fields.STATE, Constants.ORDER_STATE_ABORT);
		order.set(Fields.UPDATE_TIME, new Date());
		order.update();
		return order;
	}

	/**
	 * 查询订单列表
	 */
	public List<Record> findOrderList(String usr_id, String state, int page, int size) {
		if (StrUtils.empty(state)) {
			String sql = "select o.ord_id, o.pro_id,p.pro_nme,o.pro_amt,p.def_url,o.state,p.frt_typ_lab,p.snd_typ_lab from pt_order o left join pt_product p on o.pro_id=p.pro_id where o.usr_id=? and o.state in ('I','S','T','E') order by o.crt_tme desc limit ?,?";
			return Db.find(sql, usr_id, page * size, size);

		} else {
			String sql = "select o.ord_id, o.pro_id,p.pro_nme,o.pro_amt,p.def_url,o.state,p.frt_typ_lab,p.snd_typ_lab from pt_order o left join pt_product p on o.pro_id=p.pro_id where o.usr_id=? and o.state=? order by o.crt_tme desc limit ?,?";
			return Db.find(sql, usr_id, state, page * size, size);
		}
	}

	/**
	 * 查询订单列表
	 */
	public List<Record> find(String usr_id, Product product) {
		String sqlWhere = "select o.ord_id from pt_order o left join pt_product p on o.pro_id=p.pro_id where o.usr_id=? and o.state in ('S','T','E') and p.amt_typ=?";
		return Db.find(sqlWhere, usr_id, product.get(Fields.AMT_TYP));
	}

	/**
	 * 查询订单详情
	 */
	public Record findOrderDetail(String ord_id) {
		String sql = "select o.ord_id, o.rec_pho,o.rec_nme,o.rec_adr,o.pro_id,p.pro_nme,o.pro_amt,p.def_url,o.state,p.frt_typ_lab,p.snd_typ_lab,o.crt_tme from pt_order o left join pt_product p on o.pro_id=p.pro_id where o.ord_id=?";
		return Db.findFirst(sql, ord_id);

	}

	/**
	 * 更新订单状态
	 */
	public void updateOrderState(String ord_id, String state) {
		String sql = "update pt_order set state=? where ord_id=?";
		Db.update(sql, state, ord_id);

	}

	/**
	 * 更新订单sdl_id
	 */
	public void updateOrderSdlId(String ord_id, String sdl_id) {
		String sql = "update pt_order set sdl_id=? where ord_id=?";
		Db.update(sql, sdl_id, ord_id);

	}

	/**
	 * 更新订单
	 */
	public void update(Order order) {
		order.update();
	}

	/**
	 * 添加
	 */
	public void add(Order order) {
		order.save();
	}

	public List<Order> findByUserId(String usr_id) {
		String sql = "where usr_id=? and state in ('S','T','E')";
		return Order.dao.findByWhere(sql, usr_id);
	}
	
	public List<Order> findUserOrderByState(String usr_id,String state) {
		String sql = "where usr_id=? and state=?";
		return Order.dao.findByWhere(sql, usr_id,state);
	}
	//*************************************
	/**
	 * 查询订单列表
	 * @param ord_id
	 * @param usr_nme
	 * @param pro_nme
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Order> find(String ord_id,String usr_nme,String pro_nme,int page,int size) {
		String sql = "select *,o.state state,case o.state when 'I' then '待付款' when 'S' then '待发货' when 'T' then '已发货' when 'E' then '已签收' else '其他' end as cn_state from pt_order o,pt_user u where o.usr_id=u.usr_id ";
		if(StrUtils.isNotEmpty(ord_id)) {
			sql = sql + " and o.ord_id like concat('%','"+ord_id+"', '%')";
		}
		if(StrUtils.isNotEmpty(usr_nme)) { 
			sql = sql + " and u.usr_nme like concat('%','"+usr_nme+"', '%')";
		}
		if(StrUtils.isNotEmpty(pro_nme)) { 
			sql = sql + " and o.pro_nme like concat('%','"+pro_nme+"', '%')";
		}
		sql +=" order by o.crt_tme desc limit "+(page * size)+","+size;
		return Order.dao.find(sql);
	}
	
	/**
	 * 查询条数
	 * @param ord_id
	 * @param usr_nme
	 * @param pro_nme
	 * @return
	 */
	public long findSize(String ord_id,String usr_nme,String pro_nme) {
		String sql = "select count(1) from pt_order o,pt_user u where o.usr_id=u.usr_id ";
		if(StrUtils.isNotEmpty(ord_id)) {
			sql = sql + " and o.ord_id like concat('%','"+ord_id+"', '%')";
		}
		if(StrUtils.isNotEmpty(usr_nme)) { 
			sql = sql + " and u.usr_nme like concat('%','"+usr_nme+"', '%')";
		}
		if(StrUtils.isNotEmpty(pro_nme)) { 
			sql = sql + " and o.pro_nme like concat('%','"+pro_nme+"', '%')";
		}
		return Db.queryLong(sql);
	}
	
	public void deleteOrder(String ord_id) {
		Db.deleteById("pt_order", "ord_id",ord_id);
	}
	
	public Invoice findInvoiceByOrdId(String ord_id) {
		return Invoice.dao.findFirstByWhere("where ord_id=?", ord_id);
	}
	
	public Invoice findOneInvoice(String inv_id) {
		return Invoice.dao.findById(inv_id);
	}
	
	/**
	 * 查询发票列表
	 */
	public List<Record> findInvoiceList(int page, int size) {
		String sql = "select i.*,o.pro_nme,o.ord_id from pt_invoice i left join pt_order o on i.ord_id=o.ord_id where o.state in ('S','E','T') order by i.crt_tme desc limit ?,?";
			return Db.find(sql,page * size, size);
	}
	
	public List<Record> findExcelList(int page, int size) {
		String sql = "select *,o.state state,case o.state when 'I' then '待付款' when 'S' then '待发货' when 'T' then '已发货' when 'E' then '已签收' else '其他' end as cn_state from pt_order o,pt_user u where o.usr_id=u.usr_id ";
		sql +=" order by o.crt_tme desc limit "+(page * size)+","+size;
		return Db.find(sql);
	}
	
	/**
	 * 查询条数
	 * @return
	 */
	public long findSize() {
		String sql = "select count(1) from pt_invoice i left join pt_order o on i.ord_id=o.ord_id where o.state in ('S','E','T')";
		return Db.queryLong(sql);
	}

}
