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

@Service
public class JingDongBijiaProcess implements BijiaProcess {
	@Resource
	private CrawerSiteSkuDao crawerSiteSkuDao;

	@Override
	public boolean isSite(String site) {
		return "京东".equals(site);
	}

	@Override
	public void getExecuteSku(CrawerSiteBrandResult brand) {
		System.out.println("====处理：" + JSONObject.toJSONString(brand));

		String url = brand.getIndexUrl();
		if (url.startsWith("https://mall.jd.com/index-")) {
			String shopId = url.replaceAll("https://mall.jd.com/index-", "");
			shopId = shopId.substring(0, shopId.indexOf("."));
			System.out.println("====shopId:" + shopId);
			System.out.println("========================================");
			System.out.println("===============shopId=========================" + shopId);

			System.out.println("========================================");
			for (int pageNo = 1; pageNo <= 10; pageNo++) {

				String newUrl = "https://wqsou.jd.com/search/searchjson?datatype=1&page=" + pageNo
						+ "&pagesize=200&merge_sku=no&qp_disable=yes&key=ids,," + shopId + "&source=omz&_="
						+ System.currentTimeMillis() + "&sceneval=2&g_login_type=1&callback=jsonpCBKDDD&g_ty=ls";

				executeSku(brand, newUrl);
			}
		}
	}

	public void executeSku(CrawerSiteBrandResult brand, String newUrl) {
		String body = MockViewHelper.views(newUrl).getBody().trim().replaceAll("jsonpCBKDDD", "");
		body = body.substring(1, body.length() - 1);

		System.err.println(body);
		JSONArray arr = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("searchm")
				.getJSONArray("Paragraph");
		System.err.println("===========arr.length:" + arr.size());
		if (arr != null && arr.size() > 0) {
			for (int i = 0; i < arr.size(); i++) {
				JSONObject item = arr.getJSONObject(i);
				System.err.println(item.toJSONString());
				String name = item.getJSONObject("Content").getString("warename");
				String wareid = item.getString("wareid");
				String dredisprice = item.getString("dredisprice");

				CrawerSiteSku param = new CrawerSiteSku();
				param.setBrand(brand.getBrand());
				param.setCreateTime(new Date());
				param.setExtra(item.toJSONString());
				param.setLink("https://item.m.jd.com/product/" + wareid + ".html");
				param.setSiteName(brand.getSiteName());
				param.setTitle(name);
				String cover=item.getJSONObject("Content").getString("imageurl");
				if(cover.startsWith("jfs/")) {
					param.setCover("http://img13.360buyimg.com/mobilecms/s410x410_"+cover);
				}
				param.setPrice(Double.parseDouble(dredisprice));
				System.err.println("=====插入===" + JSONObject.toJSONString(param));
				try {
					CrawerSiteSku sku=crawerSiteSkuDao.findCrawerSiteSkuByLink(param.getLink());
					if(sku!=null) {
						sku.setCover(param.getCover());
						crawerSiteSkuDao.updateSkuByLink(sku);
					}else {
						crawerSiteSkuDao.insert(param);
					}
					
				}catch(Exception e) {
					
				}
			}
		}
	}

}
