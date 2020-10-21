package com.yd.service.crawer;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BijiaProcess;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.service.crawer.bean.CrawerTmallItem;
import com.yd.service.crawer.dao.CrawerTmallItemDao;
import com.yd.service.crawer.util.MockViewHelper;

//@Service
public class SuNingBijiaProcess  implements BijiaProcess {
	@Resource
	private CrawerTmallItemDao	crawerTmallItemDao;

	@Override
	public boolean isSite(String site) {
		return "苏宁".equals(site);
	}

	public static void main(String[] args) {
	}
	
	@Override
	public void getExecuteSku(CrawerSiteBrandResult brand) {
		String url=brand.getIndexUrl();
		if(url.startsWith("https://shop.suning.com/")) {
			String shopId=url.replaceAll("https://shop.suning.com/", "");
			shopId=shopId.substring(0, shopId.indexOf("/"));
			System.out.println(shopId);
			for(int cp=0;cp<=10;cp++) {
				String newUrl="https://csearch.suning.com/emall/brandquery/brandstoreQuery.jsonp?btc="+shopId+"&cp="+cp;
				executePerPage(newUrl,brand);
			}
		}else {
			String newUrl=url+"/search.html?keyword=";
			
			
			
			
		}
	}

	public void executePerPage(String url,CrawerSiteBrandResult brand) {
		System.out.println("=====================================================");
		System.out.println("url==="+url);
		System.out.println("brand=="+JSONObject.toJSONString(brand));
		String body=MockViewHelper.views(url).getBody();
		System.out.println("body============"+body);
		int index1=body.indexOf("(");
		int index2=body.lastIndexOf(")");
		body=body.substring(index1+1, index2);
		
		JSONObject json=JSONObject.parseObject(body);
		JSONArray arr=json.getJSONArray("goodList");
		for(int i=0;i<arr.size();i++) {
			String partNumber=arr.getJSONObject(i).getString("partNumber");
			CrawerTmallItem param=new CrawerTmallItem();
			param.setBrand(brand.getBrand());
			param.setSiteName(brand.getSiteName());
			param.setItemId(partNumber);
			param.setCreateTime(new Date());
			try {
				crawerTmallItemDao.insert(param);
			}catch(Exception e) {
				
			}
		}
	}
}
