package com.yd.service.crawer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.yd.api.crawer.ListProcess;
import com.yd.api.crawer.enums.EnumCrawerSite;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.service.crawer.util.MockViewHelper;

@Service
public class ZolListProcess implements ListProcess {
	private static final String ZOL_DOMAIN="http://detail.zol.com.cn";

	/**
	 * eg:
	 * http://detail.zol.com.cn/cell_phone_index/subcate57_613_list_1.html
	 */
	@Override
	public List<CrawerItemResult> execute(CrawerBrandResult brand) {
		List<CrawerItemResult> result = new ArrayList<CrawerItemResult>();
		for (int pageNo = 1; pageNo <= 1; pageNo++) {
		// for (int pageNo = 1; pageNo <= 10; pageNo++) {
			String[] arr=brand.getUrl().split("_list_1");
			String pageUrl = arr[0]+"_list_"+pageNo+".html";
			List<CrawerItemResult> itemList = executePage(brand,pageUrl);
			System.out.println("处理页码： pageNo:"+pageNo +" ,"+pageUrl +",抓取数量："+itemList.size());

			int i = 1;
			for (CrawerItemResult crawerItemResult : itemList) {
				if (i > 10) break;
				result.add(crawerItemResult);
				i++;
			}
			// result.addAll(itemList);
		}
		return result;
	}

	private List<CrawerItemResult> executePage(CrawerBrandResult brand,String pageUrl) {
		List<CrawerItemResult> list = new ArrayList<CrawerItemResult>();
		Document doc = MockViewHelper.views(pageUrl).getDocument();
		Element picMode = doc.getElementById("J_PicMode");
		if (picMode == null) {
			return list;
		}
		Elements lis = picMode.getElementsByTag("li");
		System.out.println("====li.size()==="+lis.size());
		if (lis.size() > 0) {
			for (int i = 0; i < lis.size(); i++) {
				try {
					Element li = lis.get(i);
					Elements as = li.getElementsByTag("a");
					if (as.size() <= 0) {
						continue;
					}
					String itemLink=as.get(0).attr("href");
					if(itemLink.startsWith("/")) {
						itemLink=ZOL_DOMAIN+itemLink;
					}
					//String cover=as.get(0).getElementsByTag("img").get(0).attr(".src");
					String title=li.getElementsByTag("h3").get(0).getElementsByTag("a").get(0).ownText().split("（")[0];
					//String price=li.getElementsByAttributeValue("class", "price-row").get(0).getElementsByAttributeValue("class", "price-type").get(0).text();
					
					CrawerItemResult crawerItem=new CrawerItemResult();
					crawerItem.setBrandId(brand.getId());
					crawerItem.setBrandName(brand.getName());
					crawerItem.setCreateTime(new Date());
					crawerItem.setTitle(title);
					crawerItem.setUrl(itemLink);
					crawerItem.setSite(EnumCrawerSite.ZOL.getCode());
					list.add(crawerItem);
				}catch(Exception e) {
					e.getSuppressed();
				}
			}
		}
		return list;
	}

	@Override
	public boolean isValid(String site) {
		return EnumCrawerSite.ZOL.getCode().equals(site);
	}

}
