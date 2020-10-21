package com.yd.service.crawer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BijiaProcess;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.service.crawer.bean.CrawerTmallItem;
import com.yd.service.crawer.dao.CrawerTmallItemDao;
import com.yd.service.crawer.util.MockViewHelper;
//@Service
public class TmallBijiaProcess implements BijiaProcess{
	@Resource
	private CrawerTmallItemDao	crawerTmallItemDao;

	@Override
	public boolean isSite(String site) {
		return "天猫".equals(site);
	}
	public static String getUrl(String url, Map<String, String> params){
		StringBuilder str = new StringBuilder();
		str.append(url);
		boolean first = true;
		for (String key : params.keySet()) {
			if (first) {
				first = false;
				str.append("?");
			} else {
				str.append("&");
			}
			str.append(key).append("=").append(params.get(key));
		}
		return str.toString();
	}
	
	public static Map<String,String> getParam(String url){
		Map<String,String> param=new HashMap<String,String>();
		String p=url.substring(url.indexOf("?"));
		for(String str:p.split("&")) {
			try {
				String name=str.split("=")[0];
				String value=str.split("=")[1];
				param.put(name, value);
			}catch(Exception e) {
				
			}
		}
		return param;
	}
	public static String getPath(String url){
		String p=url.substring(0,url.indexOf("?"));
		return p;
	}
	
	@Override
	public void getExecuteSku(CrawerSiteBrandResult brand) {
		System.out.println("==============brand:"+JSONObject.toJSONString(brand));
		for(int pageNo=1;pageNo<=10;pageNo++) {
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String path=getPath(brand.getSearchUrl());
			Map<String,String> param=getParam(brand.getSearchUrl());
			param.put("pageNo", pageNo+"");
			
			String newUrl=getUrl(path, param);
			int count=viewPerPage(newUrl, brand.getIndexUrl(), brand);
			if(count<8) {
				break;
			}
		}
	}
	
	public int viewPerPage(String url,String referer,CrawerSiteBrandResult brand) {
		System.out.println("url==="+url);
		String body=MockViewHelper.views(url, referer,getCookies()).getBody();
		int index1=body.indexOf("(");
		int index2=body.lastIndexOf(")");
		body=body.substring(index1+2, index2).replaceAll("\\&quot;", "");
		body=body.replaceAll("\\&quot;", "").replaceAll("\\\"", "\"");
		System.out.println(body);
		Document doc=Jsoup.parse(body);
		if(doc.getElementsByAttributeValueContaining("class", "J_TItems").size()==0) {
			return 0;
		}
		Element container=doc.getElementsByAttributeValueContaining("class", "J_TItems").get(0);
		Elements itemList=container.getElementsByAttributeValueContaining("class", "item ");
		System.out.println("itemList size:"+itemList.size());
		int count=0;
		for(int i=0;i<itemList.size();i++) {
			String itemId=itemList.get(i).attr("data-id");
			if(StringUtils.isNotEmpty(itemId)) {
				count++;
				itemId=itemId.replaceAll("\\\"", "");
				System.out.println("itemId:"+itemId.substring(1,itemId.length()-1));
				
				CrawerTmallItem param=new CrawerTmallItem();
				param.setBrand(brand.getBrand());
				param.setSiteName(brand.getSiteName());
				param.setItemId(itemId.substring(1,itemId.length()-1));
				param.setCreateTime(new Date());
				try {
					crawerTmallItemDao.insert(param);
				}catch(Exception e) {
					
				}
			}
		}
		
		return count;
	}

	
	
	public static Map<String,String> getCookies(){
		Map<String,String> cookie=new HashMap<String,String>();
		String str="hng=CN%7Czh-CN%7CCNY%7C156; lid=%E5%85%A8%E5%8A%9B%E4%BB%A5%E8%B5%B4%E6%88%91%E4%BB%AC%E5%BF%83%E4%B8%AD%E7%9A%84%E6%A2%A6; enc=bIAyR%2FBDEqid2D2ubLsJLk8hXxaqqDyAuo%2FDu2faPxLFNHtGnpxzmBZEEGfpWkAAIsLYtmZgph0xb%2FhQY5kQZg%3D%3D; cna=lE40FmWCiVACATy6H9jH1mMh; _m_h5_tk=e0b812c93921688de848e50447a85625_1574932078803; _m_h5_tk_enc=7a68f07c7552f8d9235010abdc9d9187; cq=ccp%3D1; pnm_cku822=; t=6e6d142f9e1f83c2adbaf9fdd61b03ad; tracknick=%5Cu5168%5Cu529B%5Cu4EE5%5Cu8D74%5Cu6211%5Cu4EEC%5Cu5FC3%5Cu4E2D%5Cu7684%5Cu68A6; _tb_token_=f6fa6e335d733; cookie2=1fa9b48f9f05a8d909454a571757ddf0; x5sec=7b2273686f7073797374656d3b32223a22316234616464623530616431666565316138343133643066613838613835383743506a506b753846455053712b356e53694d756553686f4b4e544d314d7a55774f5463374d513d3d227d; l=dBrXwpZgqnw2yhcNKOCanurza77OSIRYYuPzaNbMi_5Q86Ts_mQOk3FpmF96VjW5TaTB4s6vWjp9-etkZV6vA-eKo_Zp7n_qcFsB4; isg=BJGRzeUIqES3TMQZg2yE5XG6oJ3rVg7uU2N-a3Mmjth3GrFsu01YQWl8vK5ZEp2o";
		String[] arr=str.split(";");
		for(String items:arr) {
			try {
				String name=items.split("=")[0];
				String value=items.split("=")[1];
				cookie.put(name.trim(), value.trim());
			}catch(Exception e) {
				
			}
		}
		
		return cookie;
	}
}
