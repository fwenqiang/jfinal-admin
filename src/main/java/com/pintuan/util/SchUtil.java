package com.pintuan.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import com.pintuan.common.Fields;
import com.pintuan.model.Scheduling;

/**
 * 排单工具
 * 
 * @author zjh 2018-5-6
 */
public class SchUtil {

	/**
	 * 
	 * 段位         grid     1-7
     * 轮数         tur_num  每7圈一轮：大轮
	 **/
	public static int getTarNum(int tur_num,int grid) {
		  int i = (int) Math.pow(2,(tur_num-1)%5);
			switch(grid) {
			case 1:return 55*i;
			case 2:return 115*i;
			case 3:return 188*i;
			case 4:return 368*i;
			case 5:return 757*i;
			case 6:return 1358*i;
			case 7:return 2788*i;
			default:return 55*i;
			}
	
	}
	
	/**
	 * 
	 * 段位         grid     1-7
     * 轮数         tur_num  每7圈一轮：大轮
	 **/
	public static int getNextTurNum(int tur_num,int grid) {
		  if(grid==7) {
			  return tur_num>=5?1:tur_num+1;
		  }else {
			  return tur_num;
		  }
	
	}
	
	/**
	 * 获得下次排单分红金额
	 * 
	 * @param amt_typ
	 * @return
	 */
	public static BigDecimal getNextAmt(String amt_typ) {
		if("800".equals(amt_typ)) {
			return new BigDecimal("1268");
		}else {
			return new BigDecimal("168");
		}
	}
	
	/**
	 * 
	 * 段位         grid     1-7
	 **/
	public static int getNextGrid(int grid) {
		  if(grid==7) {
			  return 1;
		  }else {
			  return grid+1;
		  }
	
	}
	
	/**
	 *收入类型     bon_typ      char        2      
	 *select/ 
	 *A1-A系统1段位红利；....;
	 *B1-B系统1段位红利；
	 *C1-存款收益；
	 *D1-提现；
	 *E1-手续费；
	 *F1-下级分红
	 **/
	public static String getBonTyp(Scheduling scheduling) {
		  if(scheduling==null) return "";
		  if("800".equals(scheduling.getStr(Fields.AMT_TYP))) {
			  String H = "A";
			  return H+scheduling.getStr(Fields.GRID);
		  }else {
			  String H = "B";
			  return H+scheduling.getStr(Fields.GRID);
		  }
	
	}
	/**
	 * 获得用户下次段位
	 * @param grid
	 * @param tre_typ
	 * @return
	 */
	public static String getNextUserGrid(String grid,String tre_typ) {
		if("1".equals(tre_typ)) {
		switch(grid) {
		case "石头":return "青铜";
		case "青铜":return "白银";
		case "白银":return "黄金1星";
		case "黄金1星":return "黄金2星";
		case "黄金2星":return "铂金1星";
		case "铂金1星":return "铂金2星";
		case "铂金2星":return "铂金3星";
		default:return "铂金3星";
		}
		}else {
			switch(grid) {
			case "石头":
			case "青铜":
			case "白银":
			case "黄金1星":
			case "黄金2星":
			case "铂金1星":
			case "铂金2星":
			case "铂金3星":return "钻石";
			case "钻石":return "黑耀";
			case "黑耀":return "星耀1星";
			case "星耀1星":return "星耀2星";
			case "星耀2星":return "王者1星";
			case "王者1星":return "王者2星";
			case "王者2星":return "荣耀王者";
			default:return "铂金3星";
			}
		}
	
	}
	/**
	 * 或得用户称谓
	 * @param chdNum
	 * @return
	 */
	public static String getUserTitle(int chdNum) {
			switch(chdNum) {
			case 0:return "平民";
			case 1:return "初级师傅";
			case 2:return "中级师傅";
			case 3:return "高级师傅";
			case 4:return "一代宗师";
			default:return "一代宗师";
			}
	
	}
	
    /**
     * 获取800产品分红段数
     * 
     * @param chdNum 分享的徒弟个数
     * @param schNum 排单是轮数
     * @return
     */
	public static int get800Bonus(int chdNum,int schNum) {
			switch(chdNum) {
			case 0:return getMin(0,schNum);
			case 1:return getMin(1,schNum);
			case 2:return getMin(2,schNum);
			case 3:return getMin(4,schNum);
			case 4:return getMin(7,schNum);
			default:return getMin(7,schNum);
			}
	
	}
	
	public static int getMin(int a,int b) {
		if(a<b) return a;
		else return b;
	}

	
	public static void main(String[] args) {
System.out.println(getTarNum(1,1));
System.out.println( getTarNum(2,1));
System.out.println( getTarNum(4,4));
System.out.println( getTarNum(5,3));
System.out.println( getTarNum(3,3));
System.out.println( getTarNum(1,3));

		
	}

}
