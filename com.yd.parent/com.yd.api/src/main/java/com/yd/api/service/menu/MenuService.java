package com.yd.api.service.menu;

import java.util.List;

import com.yd.api.result.menu.YdMenuResult;

public interface MenuService {
	public List<YdMenuResult> getMenuList(Integer merchantId);
}
