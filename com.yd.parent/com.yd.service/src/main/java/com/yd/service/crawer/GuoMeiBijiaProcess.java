package com.yd.service.crawer;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BijiaProcess;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.service.crawer.bean.CrawerSiteSku;
import com.yd.service.crawer.dao.CrawerSiteSkuDao;
import com.yd.service.crawer.util.MockViewHelper;

//@Service
public class GuoMeiBijiaProcess implements BijiaProcess {
	@Resource
	private CrawerSiteSkuDao crawerSiteSkuDao;

	@Override
	public boolean isSite(String site) {
		return "国美".equals(site);
	}

	@Override
	public void getExecuteSku(CrawerSiteBrandResult brand) {
		System.out.println("处理：" + JSONObject.toJSONString(brand));
		String url = brand.getIndexUrl();
		if (url.startsWith("https://pinpai.gome.com.cn")) {
			String shopId = url.replaceAll("https://pinpai.gome.com.cn/", "");
			shopId = shopId.substring(0, shopId.indexOf("/"));
			for (int pageNo = 1; pageNo <= 10; pageNo++) {
				String newUrl = "https://apis.gome.com.cn/p/lshop/20/" + pageNo + "/0/0/0/" + shopId
						+ "/0/0?callback=jQuery171020877769470828_1575275097714&from=gomeShopList&_="
						+ System.currentTimeMillis();
				executeSku(brand, newUrl);
			}
		}
	}

	public void executeSku(CrawerSiteBrandResult brand, String newUrl) {
		System.err.println("==================================");
		String body = MockViewHelper.views(newUrl, brand.getIndexUrl(), null).getBody();
		System.err.println("body:"+body);
		int index1=body.indexOf("(");
		int index2=body.lastIndexOf(")");
		body=body.substring(index1+1, index2);
		System.err.println("body2:"+body);
		
		JSONArray arr = JSONObject.parseObject(body).getJSONObject("content").getJSONObject("prodInfo")
				.getJSONArray("products");
		if (arr != null && arr.size() > 0) {
			for (int j = 0; j < arr.size(); j++) {
				JSONArray skuList = arr.getJSONObject(j).getJSONArray("images");
				if (skuList != null && skuList.size() > 0) {
					for (int k = 0; k < skuList.size(); k++) {
						JSONObject sku = skuList.getJSONObject(k);
						String link = sku.getString("sUrl");
						String name = sku.getString("name");
						CrawerSiteSku param = new CrawerSiteSku();
						param.setLink(link);
						param.setTitle(name);
						param.setSiteName(brand.getSiteName());
						param.setCreateTime(new Date());
						param.setBrand(brand.getBrand());
						param.setExtra(sku.toJSONString());
						try {
							System.out.println(JSONObject.toJSONString(param));
							crawerSiteSkuDao.insert(param);
						} catch (Exception e) {

						}
					}
				}
			}
		}

	}
}
