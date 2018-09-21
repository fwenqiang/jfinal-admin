package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;
/**
 * 红包表
 * @author Administrator
 *
 */
@ModelBind(table = "pt_red_packet",key = "pac_id")
public class RedPacket extends BaseProjectModel<RedPacket> {

	private static final long serialVersionUID = 1L;
	public static final RedPacket dao = new RedPacket();

}
