package com.yd.api.crawer;

import java.util.List;

import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;

public interface ItemProcess {
	List<CrawerSpecNameWithSpecVal> execute(CrawerItemResult item);
	
	boolean isValid(String site);
}
