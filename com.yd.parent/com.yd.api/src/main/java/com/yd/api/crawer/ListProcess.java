package com.yd.api.crawer;

import java.util.List;

import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;

public interface ListProcess {
	List<CrawerItemResult> execute(CrawerBrandResult brand);
	boolean isValid(String site);
}
