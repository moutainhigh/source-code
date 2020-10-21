package com.yd.api.crawer;

import com.yd.api.crawer.result.CrawerSiteBrandResult;

public interface BijiaProcess {
	public boolean isSite(String site);

	public void getExecuteSku(CrawerSiteBrandResult brand);
}
