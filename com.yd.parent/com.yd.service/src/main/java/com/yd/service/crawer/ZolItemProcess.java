package com.yd.service.crawer;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.yd.api.crawer.ItemProcess;
import com.yd.api.crawer.enums.EnumCrawerSite;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;
import com.yd.api.crawer.result.CrawerSpecVal;
import com.yd.service.crawer.util.MockViewHelper;

@Service
public class ZolItemProcess implements ItemProcess {

	@Override
	public List<CrawerSpecNameWithSpecVal> execute(CrawerItemResult item) {
		List<CrawerSpecNameWithSpecVal> result=new ArrayList<CrawerSpecNameWithSpecVal>();
		Document doc=MockViewHelper.views(item.getUrl()).getDocument();
		Elements specElList=doc.getElementsByAttributeValue("class", "price-item _j_choose_product");
		if(specElList.size()==0) {
			return null;
		}
		for(int i=0;i<specElList.size();i++) {
			CrawerSpecNameWithSpecVal specNameWithVal=new CrawerSpecNameWithSpecVal();
			Element specEl=specElList.get(i);
			//System.out.println(specEl.html());
			String specName=specEl.getElementsByAttributeValue("class", "dt").get(0).text().replaceAll("：", "");
			specNameWithVal.setSpecName(specName);
			Elements itemList=specEl.getElementsByAttributeValue("class", "dd").get(0).getElementsByAttributeValueContaining("class", "item");
			if(itemList.size()==0) {
				return null;
			}
			List<CrawerSpecVal> specValList=new ArrayList<CrawerSpecVal>();
			for(int j=0;j<itemList.size();j++) {
				String title=itemList.get(j).attr("title").trim();
				String propId=itemList.get(j).attr("data-value").trim();
				CrawerSpecVal specVal=new CrawerSpecVal();
				specVal.setPropId(propId);
				specVal.setValue(title);
				specValList.add(specVal);
			}
			specNameWithVal.setSpecValList(specValList);
			
			result.add(specNameWithVal);
		}
		return result;
	}

	@Override
	public boolean isValid(String site) {
		return EnumCrawerSite.ZOL.getCode().equals(site);
	}
	
//	public static void main(String[] args) {
//		ZolItemProcess i=new ZolItemProcess();
//		CrawerItemResult item=new CrawerItemResult();
//		item.setUrl("http://detail.zol.com.cn/cell_phone/index1212233.shtml?t=2&v=23639");
//		List<CrawerSpecNameWithSpecVal> result=i.execute(item);
//		System.out.println(JSONObject.toJSONString(result));
//	}
	
	

}
