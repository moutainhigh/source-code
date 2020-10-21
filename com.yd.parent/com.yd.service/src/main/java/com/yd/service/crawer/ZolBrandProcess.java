package com.yd.service.crawer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.yd.api.crawer.BrandProcess;
import com.yd.api.crawer.enums.EnumCrawerSite;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.service.crawer.util.MockViewHelper;
import com.yd.service.crawer.util.MockViewHelper.MockViewRes;

@Service
public class ZolBrandProcess implements BrandProcess {
	private static final String ZOL_DOMAIN="http://detail.zol.com.cn";

	@Override
	public List<CrawerBrandResult> getBrandList() {
		String url=ZOL_DOMAIN+"/cell_phone_index/subcate57_list_1.html";
		
		MockViewRes res=MockViewHelper.views(url);
		Element brandAll=res.getDocument().getElementById("J_BrandAll");
		Elements brandLink=brandAll.getElementsByTag("a");
		
		List<CrawerBrandResult> brandList=new ArrayList<CrawerBrandResult>();
		for(int i=0;i<brandLink.size();i++) {
			Element node=brandLink.get(i);
			String text=node.text().trim();
			String  link=node.attr("href").trim();
			if(StringUtils.isNotEmpty(link) && link.startsWith("/")) {
				link=ZOL_DOMAIN+link;
			}
			if(StringUtils.isEmpty(text) || StringUtils.isEmpty(link)) {
				continue;
			}
			CrawerBrandResult brand=new CrawerBrandResult();
			brand.setName(text);
			brand.setUrl(link);
			brand.setSite(EnumCrawerSite.ZOL.getCode());
			brandList.add(brand);
		}
		return brandList;
	}

}
