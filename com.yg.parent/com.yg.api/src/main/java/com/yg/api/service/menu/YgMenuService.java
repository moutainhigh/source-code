package com.yg.api.service.menu;

import com.yg.api.result.menu.YgMenuResult;

import java.util.List;

public interface YgMenuService {
	public List<YgMenuResult> getMenuList(Integer merchantId);
}
