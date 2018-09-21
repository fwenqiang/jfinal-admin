package com.pintuan.model;

import com.supyuan.component.base.BaseProjectModel;
import com.supyuan.jfinal.component.annotation.ModelBind;

@ModelBind(table = "pt_poster_board",key = "bor_id")
public class PosterBoard extends BaseProjectModel<PosterBoard> {

	private static final long serialVersionUID = 1L;
	public static final PosterBoard dao = new PosterBoard();

}
