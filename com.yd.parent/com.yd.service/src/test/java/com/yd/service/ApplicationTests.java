package com.yd.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.yd.service.bean.item.YdItemImage;
import com.yd.service.dao.item.YdItemDao;
import com.yd.service.dao.item.YdItemImageDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BrandProcess;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.ItemProcess;
import com.yd.api.crawer.ListProcess;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.service.item.YdItemService;
import com.yd.service.crawer.JDCrawerProcess;
import com.yd.service.crawer.util.MockViewHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
public class ApplicationTests {
	@Resource
	private List<BrandProcess> brandProcessList;
	@Resource
	private List<ListProcess> listProcessList;
	@Resource
	private List<ItemProcess> itemProcessList;
	
	@Resource
	private CrawerService	crawerService;
	@Resource
	private YdItemService	ydItemService;

	@Resource
	private YdItemImageDao	ydItemImageDao;

	@Resource
	private YdItemDao ydItemDao;

	@Test
	public void initItemContent() throws IOException {
		List<String> lines=FileUtils.readLines(new File("d:itemContent.txt"));
		List<String> errList = new ArrayList<>();
		if(lines!=null && lines.size()>0) {
			for(String line:lines) {
				String itemId = null;
				try {
					System.out.println("========"+line);
					itemId=line.split(",")[0].trim();
					System.out.println("========itemId="+itemId);
					String itemUrl=line.split(",")[1].trim();
					if(itemUrl.contains("jd.com")) {
						String content=JDCrawerProcess.getItemContent(itemUrl);
						ydItemService.addOrUpdateContent(Integer.parseInt(itemId), content);
					}
				} catch (Exception e) {
					System.out.println("====商品id=" + itemId  + "的出错");
					errList.add(itemId);
				}

			}
		}
	}

	@Test
	public void initItemImage() throws IOException {
		ydItemService.sysItemImage();
	}
	
	@Test
	public void initBrand() {
		for(BrandProcess process:brandProcessList) {
			List<CrawerBrandResult> list=process.getBrandList();
			if(list!=null && list.size()>0) {
				for(CrawerBrandResult brand:list) {
					crawerService.addBrand(brand);
				}
			}
		}
	}
	
	@Test
	public void initItem() {
		List<CrawerBrandResult> brandList=crawerService.getBrandList();
		for(CrawerBrandResult brand:brandList) {
			for(ListProcess process:listProcessList) {
				System.out.println("====处理品牌:"+JSONObject.toJSONString(brand));
				if(process.isValid(brand.getSite())) {
					List<CrawerItemResult> itemList=process.execute(brand);
					if(itemList!=null && itemList.size()>0) {
						for(CrawerItemResult item:itemList) {
							System.out.println(JSONObject.toJSONString("====item===="+item));
							crawerService.addItem(item);
						}
					}
				}
			}
		}
	}
	
	@Test
	public void initShopItemWithSku() {
		List<CrawerItemResult> itemList=crawerService.getItemList();
		for(CrawerItemResult item:itemList) {
			try {
				System.out.println("---------------------------------");
				System.out.println("===========craw item:"+JSONObject.toJSONString(item));
				for(ItemProcess process:itemProcessList) {
					if(process.isValid(item.getSite())) {
						List<CrawerSpecNameWithSpecVal> specNameWithSpecValList=process.execute(item);
						System.out.println("===========spacValList:"+JSONObject.toJSONString(specNameWithSpecValList));
						if(specNameWithSpecValList!=null && specNameWithSpecValList.size()>0) {
							System.out.println("================初始化商品： "+item.getId());
							ydItemService.initItemFromCrawer(item,specNameWithSpecValList);
						}
					}
				}
			}catch(Exception e) {
				
			}
			crawerService.updateExecuteStatus(item.getId());
		}
	}
	
	
	
	@Test
	public void initPubTime() {
		List<YdItemResult> itemList=ydItemService.getAll(new YdItemResult());
		if(itemList!=null && itemList.size()>0) {
			for(YdItemResult item:itemList) {
				System.out.println("------------------------------------------");
				if(StringUtils.isNotEmpty(item.getPubTime())) {
					continue;
				}
				
				CrawerItemResult crawItem=crawerService.getDetail(item.getCrawerId());
				String url=crawItem.getUrl();
				System.out.println("url:"+url);
				String paramUrl="http://detail.zol.com.cn"+MockViewHelper.views(url).getDocument().getElementsByAttributeValue("class", "section-more").get(0).attr("href");
				System.out.println(paramUrl);
				Elements trList=MockViewHelper.views(paramUrl).getDocument().getElementsByAttributeValue("class", "detailed-parameters").get(0).getElementsByTag("tr");
				for(int i=0;i<trList.size();i++) {
					String pmName=trList.get(i).getElementsByAttributeValueContaining("id", "newPmName_").text().trim();
					String pmValue=trList.get(i).getElementsByAttributeValueContaining("id", "newPmVal_").text().trim();
					System.out.println("pmName:"+pmName);
					System.out.println("pmValue:"+pmValue);
					if(StringUtils.isNotEmpty(pmName) && pmName.contains("上市日期")) {
						ydItemService.updatePubTime(item.getId(),pmValue);
						break;
					}else if(StringUtils.isNotEmpty(pmName) && pmName.contains("发布会时间")) {
						ydItemService.updatePubTime(item.getId(),pmValue);
						break;
					}
				}
			}
		}
	}
	
}
