package com.yd.service.impl.crawer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BrandProcess;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.ItemProcess;
import com.yd.api.crawer.ListProcess;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;
import com.yd.api.result.item.YdItemResult;
import com.yd.api.service.crawer.YdCrawerService;
import com.yd.api.service.item.YdItemService;
import com.yd.service.bean.item.YdItem;
import com.yd.service.crawer.JDCrawerProcess;
import com.yd.service.crawer.util.MockViewHelper;
import com.yd.service.dao.item.YdItemDao;
import com.yd.service.dao.item.YdItemImageDao;
import com.yd.service.dao.item.YdItemSkuDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Title:优度抓取商品
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdCrawerServiceImpl implements YdCrawerService {

	private static final Logger logger = LoggerFactory.getLogger(YdCrawerServiceImpl.class);

	@Resource
	private YdItemDao ydItemDao;

	@Resource
	private YdItemSkuDao ydItemSkuDao;

	@Resource
	private YdItemService ydItemService;

	@Resource
	private CrawerService crawerService;

	@Resource
	private List<ListProcess> listProcessList;

	@Resource
	private List<ItemProcess> itemProcessList;

	@Resource
	private List<BrandProcess> brandProcessList;

	@Override
	public void synAll() {
		System.out.println("====下面开始抓取品牌");
		// 1. 抓取品牌
		initBrand();

		// 2. 抓取商品
		System.out.println("====下面开始抓取商品");
		initItem();

		// 3. 抓取规格
		System.out.println("====下面开始抓取规格");
		initShopItemWithSku();

		// 4. 抓取发布时间
		System.out.println("====下面开始抓取发布时间");
		initPubTime();

		// 5. 抓取图片
		System.out.println("====下面开始抓取图片");
		ydItemService.sysItemImage();

		// 6. 抓取商品详情
		// initItemContent();
	}

	@Override
	public void synCrawerBrand() {
		initBrand();
	}

	@Override
	public void synCrawerItem() {
		System.out.println("====下面开始抓取爬虫商品");
		initItem();
	}

	@Override
	public void synYdItemAndSku() {
		System.out.println("====下面开始同步优度商品规格");
		initShopItemWithSku();
	}

	@Override
	public void synItemPublicTime() {
		System.out.println("====下面开始抓取发布时间");
		initPubTime();
	}

	@Override
	public void synYdImage() {
		System.out.println("====下面开始抓取图片");
		ydItemService.sysItemImage();
	}

	@Override
	public void synYdContent() {
//		 6. 抓取商品详情
		try {
			initItemContent();
		} catch (Exception e) {

		}
	}

	@Override
	public void deleteRepeatItem() {
		// 分组查询商品
		List<YdItem> itemList = ydItemDao.findItemListGroupByTitle();
		itemList.forEach(item -> {
			YdItem ydItem = new YdItem();
			ydItem.setId(item.getId());
			ydItem.setTitle(item.getTitle());
			List<YdItem> deleteItemList = ydItemDao.getAOtherItem(ydItem);
			if (CollectionUtils.isNotEmpty(deleteItemList)) {
				int count = 1;
				for (YdItem deleteItem : deleteItemList) {
					System.out.println("======正在删除第count=" + count + "个商品，YdItemId=" + deleteItem.getId());
					ydItemDao.deleteItemById(deleteItem.getId());
					ydItemSkuDao.deleteItemSkuByItemId(deleteItem.getId());
				}
			}
		});
	}

	// 1. 抓取品牌
	private void initBrand() {
		for(BrandProcess process : brandProcessList) {
			List<CrawerBrandResult> list=process.getBrandList();
			if(list!=null && list.size()>0) {
				for(CrawerBrandResult brand:list) {
					crawerService.addBrand(brand);
				}
			}
		}
	}

	// 2. 抓取商品
	private void initItem() {
		List<CrawerBrandResult> brandList=crawerService.getBrandList();
		for(CrawerBrandResult brand:brandList) {
			for(ListProcess process:listProcessList) {
				System.out.println("====处理品牌:"+JSONObject.toJSONString(brand));
				if(process.isValid(brand.getSite())) {
					List<CrawerItemResult> itemList=process.execute(brand);
					if(CollectionUtils.isNotEmpty(itemList)) {
						for(CrawerItemResult item:itemList) {
							System.out.println(JSONObject.toJSONString("====item===="+item));
							crawerService.addItem(item);
						}
					}
				}
			}
		}
	}

	// 3. 抓取规格
	private void initShopItemWithSku() {
		List<CrawerItemResult> itemList=crawerService.getItemList();
		for(CrawerItemResult item:itemList) {
			try {
				System.out.println("---------------------------------");
				System.out.println("===========craw item:"+JSONObject.toJSONString(item));
				for(ItemProcess process:itemProcessList) {
					if(process.isValid(item.getSite())) {
						List<CrawerSpecNameWithSpecVal> specNameWithSpecValList = process.execute(item);
						System.out.println("====抓取规格crawId=" + item.getId() + "的结果spacValList=" + JSONObject.toJSONString(specNameWithSpecValList));
						if(CollectionUtils.isNotEmpty(specNameWithSpecValList)) {
							System.out.println("================初始化商品： "+item.getId());
							ydItemService.initItemFromCrawer(item,specNameWithSpecValList);
						}
					}
				}
				// 更新状态
				crawerService.updateExecuteStatus(item.getId());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 4. 抓取发布时间
	private void initPubTime() {
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

	// 6. 抓取图片详情
	public void initItemContent() throws IOException {
		List<String> lines=FileUtils.readLines(new File("d:suning.txt"));
		int count = 0;
		List<String> hasItemIdList = new ArrayList<>();
		List<String> noItemIdList = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(lines)) {
			for(String line : lines) {
				String itemId = line.split(",")[0].trim();
				String itemUrl = line.split(",")[1].trim();
				try {
					if(!itemUrl.contains("jd.com")) {
						System.out.println("====开始上传itemId=" + itemId + "的商品");
						String content=JDCrawerProcess.getSuningItemContent(itemUrl);
						if (StringUtils.isNotEmpty(content)) {
							hasItemIdList.add(itemId);
							count++;
						} else {
							noItemIdList.add(itemId);
						}
						ydItemService.addOrUpdateContent(Integer.parseInt(itemId), content);
					}
				} catch (Exception e) {
					System.out.println("====上传itemId=" + itemId + "的商品失败");
					noItemIdList.add(itemId);
				}

			}
		}
		System.out.println("====抓取到=" + count);
		if (CollectionUtils.isNotEmpty(noItemIdList)) {
			System.out.println("====没抓取到的itemId=" + JSON.toJSONString(noItemIdList));
		}
		System.out.println("====已经结束");
	}


}

