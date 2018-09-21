package com.pintuan.controller.app.bonus;

import java.math.BigDecimal;

import com.jfinal.aop.Before;
import com.pintuan.base.CoreException;
import com.pintuan.common.Constants;
import com.pintuan.common.ErrCode;
import com.pintuan.common.Fields;
import com.pintuan.interceptor.CheckUserKeyInterceptor;
import com.pintuan.model.RedPacket;
import com.pintuan.service.BonusService;
import com.pintuan.service.RedPacketService;
import com.pintuan.util.Assert;
import com.supyuan.component.base.BaseProjectController;
import com.supyuan.jfinal.component.annotation.ControllerBind;

/**
 * 拆红包
 * 
 * @author zjh 2018-4-20
 */
@ControllerBind(controllerKey = "/pintuan/app/dismantRedPacket")
@Before(CheckUserKeyInterceptor.class)
public class DismantRedPacketController extends BaseProjectController {
	private BonusService bonusService = new BonusService();
	private RedPacketService redPacketService = new RedPacketService();

	/*public void index() throws CoreException {
		String usr_ide_key = isNotNullAndGet(Fields.USER_IDENTIFY_KEY, ErrCode.PAMAS_ERROR).toString();
		String pac_id = isNotNullAndGet(Fields.PAC_ID, ErrCode.PAMAS_ERROR).toString();

		BigDecimal fee_05 = new BigDecimal("0.05");
		BigDecimal fee_9 = new BigDecimal("0.9");

		RedPacket redPacket = redPacketService.findRedPacket(pac_id);
		Assert.notEmpty(redPacket, ErrCode.RED_PACKET_UN_EXIST);
		Assert.isTrue(Constants.REDPACKET_STATE_INIT.equals(redPacket.getStr(Fields.STATE)), ErrCode.RED_PACKET_UN_EXIST);

		BigDecimal fee = redPacket.getBigDecimal(Fields.AMT);
		// 余额加钱
		bonusService.updatePaket(fee_9.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_1);
		bonusService.updatePaket(fee_05.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_2);
		bonusService.updatePaket(fee_05.multiply(fee), usr_ide_key, Constants.BONUS_CFG_TYP_3);
		redPacket.set(Fields.STATE, Constants.REDPACKET_STATE_PICKED);
		redPacketService.updateRedPacket(redPacket);
		returnJson();
	}*/

}
