package com.yd.service.impl.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSpecNameWithSpecVal;
import com.yd.api.crawer.result.CrawerSpecVal;
import com.yd.api.result.item.*;
import com.yd.api.service.item.YdItemService;
import com.yd.core.utils.*;
import com.yd.service.bean.item.*;
import com.yd.service.crawer.JDCrawerProcess;
import com.yd.service.dao.item.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:平台商品Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-10-23 12:28:35
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdItemServiceImpl implements YdItemService {

	private static final Logger logger = LoggerFactory.getLogger(YdItemServiceImpl.class);

	@Resource
	private YdItemDao ydItemDao;

	@Resource
	private YdItemSkuDao	ydItemSkuDao;

	@Resource
	private YdItemImageDao ydItemImageDao;

	@Resource
	private YdItemSpecNameDao	ydItemSpecNameDao;

	@Resource
	private YdItemSpecValueDao	ydItemSpecValueDao;

	@Resource
	private YdItemContentDao	ydItemContentDao;

	@Resource
	private CrawerService crawerService;

	@Override
	public YdItemResult getYdItemById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdItemResult ydItemResult = null;
		YdItem ydItem = this.ydItemDao.getYdItemById(id);
		if (ydItem != null) {
			ydItemResult = new YdItemResult();
			BeanUtilExt.copyProperties(ydItemResult, ydItem);
		}
		return ydItemResult;
	}


	@Override
	public void sysItemImage() {
		// String url = "http://detail.zol.com.cn/cell_phone/index1293875.shtml?t=2&v=25322";
		System.out.println("====come in sysItemImage");
		// 1. 查询所有商品
		YdItem ydItem = new YdItem();
		List<YdItem> ydItemList = ydItemDao.getAll(ydItem);
		Integer count = 0;
		Integer total = ydItemList.size();
		for (YdItem item : ydItemList) {
			logger.info("====一共" + total + ", 现在正在同步第" + count++ + "条");
			// if (item.getId() > 179) continue;
			try {
				if (StringUtils.isEmpty(item.getItemCover())) {
					List<YdItemImage> itemImageList = ydItemImageDao.findImageListByItemId(item.getId());
					if (CollectionUtils.isNotEmpty(itemImageList)) continue;

					CrawerItemResult crawerItem = crawerService.getDetail(item.getCrawerId());
					List<String> imageList = JDCrawerProcess.getItemImage(crawerItem.getUrl());

					if (CollectionUtils.isEmpty(imageList)) continue;

					item.setItemCover(imageList.get(0));
					ydItemDao.updateYdItem(item);
					imageList.forEach(image -> {
						YdItemImage ydItemImage = new YdItemImage();
						ydItemImage.setCreateTime(new Date());
						ydItemImage.setItemId(item.getId());
						ydItemImage.setImageUrl(image);
						ydItemImageDao.insertYdItemImage(ydItemImage);
					});
				}
			} catch (Exception e) {
				logger.info("商品id=" + item.getId() + "的商品同步失败" + e.getLocalizedMessage());
			}
		}
		System.out.println("======同步完成");
	}

	@Override
	public Page<YdItemResult> findItemListByPage(YdItemResult params, PagerInfo pagerInfo) {
		Page<YdItemResult> resultPageData = new Page<>(pagerInfo.getPageIndex(), pagerInfo.getPageSize());
		YdItem ydItem = new YdItem();
		BeanUtilExt.copyProperties(ydItem, params);

		int amount = ydItemDao.getItemCount(ydItem);
		if (amount > 0) {
			List<YdItem> dataList = this.ydItemDao.findItemListByPage(ydItem, pagerInfo.getStart(), pagerInfo.getPageSize());
			List<YdItemResult> resultList = DTOUtils.convertList(dataList, YdItemResult.class);
			resultPageData.setData(resultList);
		}
		resultPageData.setTotalRecord(amount);
		return resultPageData;
	}

	@Override
	public List<YdItemResult> getAll(YdItemResult ydItemResult) {
		YdItem ydItem = null;
		if (ydItemResult != null) {
			ydItem = new YdItem();
			BeanUtilExt.copyProperties(ydItem, ydItemResult);
		}
		List<YdItem> dataList = this.ydItemDao.getAll(ydItem);
		return DTOUtils.convertList(dataList, YdItemResult.class);
	}

	@Override
	public void insertYdItem(YdItemResult ydItemResult) {
		if (null != ydItemResult) {
			ydItemResult.setCreateTime(new Date());
			ydItemResult.setUpdateTime(new Date());
			YdItem ydItem = new YdItem();
			BeanUtilExt.copyProperties(ydItem, ydItemResult);
			this.ydItemDao.insertYdItem(ydItem);
		}
	}
	
	
	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void updateYdItem(YdItemResult ydItemResult) {
		Integer itemId = ydItemResult.getId();

		ValidateBusinessUtils.assertFalse(itemId == null || itemId <= 0,
				"err_item_id", "非法的商品id");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydItemResult.getTitle()),
				"err_is_title", "商品标题不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydItemResult.getIsEnable()),
				"err_is_enable_empty", "商品上下架状态不可以为空");

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(ydItemResult.getImageList()),
				"err_image_list_empty", "商品图片不可以为空");

		ValidateBusinessUtils.assertFalse(StringUtils.isEmpty(ydItemResult.getContent()),
				"err_content_empty", "商品描述不可以为空");

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(ydItemResult.getSkuList()),
				"err_sku_list_empty", "商品规格不可以为空");

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertFalse(ydItem == null, "err_item_not_exist", "商品不存在");

		// 修改商品属性
		ydItem.setUpdateTime(new Date());
		ydItem.setTitle(ydItemResult.getTitle());
		ydItem.setIsEnable(ydItemResult.getIsEnable());
		this.ydItemDao.updateYdItem(ydItem);

		// 修改商品规格是否禁用
		ydItemResult.getSkuList().forEach(ydItemSkuResult -> {
			YdItemSku ydItemSku = ydItemSkuDao.getYdItemSkuById(ydItemSkuResult.getId());
			ValidateBusinessUtils.assertFalse(ydItemSku == null,
					"err_sku_is_empty", "规格不存在， 规格id为" + ydItemSkuResult.getId());
			ydItemSku.setIsEnable(ydItemSkuResult.getIsEnable());
			ydItemSku.setSalePrice(ydItemSkuResult.getSalePrice());
			ydItemSkuDao.updateYdItemSku(ydItemSku);
		});

		// 先删除图片，在添加图片
		ydItemImageDao.deleteYdItemImage(itemId);
		ydItemResult.getImageList().forEach(ydItemImageResult -> {
			YdItemImage ydItemImage = new YdItemImage();
			ydItemImage.setCreateTime(new Date());
			ydItemImage.setItemId(itemId);
			ydItemImage.setImageUrl(ydItemImageResult.getImageUrl());
			ydItemImageDao.insertYdItemImage(ydItemImage);
		});

		// 修改富文本内容
		YdItemContent ydItemContent = ydItemContentDao.getYdItemContentByItemId(itemId);
		if (ydItemContent == null) {
			ydItemContent = new YdItemContent();
			ydItemContent.setCreateTime(new Date());
			ydItemContent.setItemId(itemId);
			ydItemContent.setContent(ydItemResult.getContent());
			ydItemContentDao.insertYdItemContent(ydItemContent);
		} else {
			ydItemContent.setUpdateTime(new Date());
			ydItemContent.setContent(ydItemResult.getContent());
			ydItemContentDao.updateYdItemContent(ydItemContent);
		}
	}

	@Override
	@Transactional
	public void initItemFromCrawer(CrawerItemResult item, List<CrawerSpecNameWithSpecVal> specNameWithSpecValList) {
		System.out.println("-----------------initItemFromCrawer");
		YdItem ydItem=new YdItem();
		ydItem.setCreateTime(new Date());
		ydItem.setTitle(item.getTitle());
		ydItem.setSubTitle(item.getTitle());
		ydItem.setIsEnable("N");
		ydItem.setSpecNum(specNameWithSpecValList.size());
		ydItem.setCrawerId(item.getId());
		ydItem.setBrand(item.getBrandName());
		ydItemDao.insertYdItem(ydItem);
		System.out.println("--------插入item: "+JSONObject.toJSONString(ydItem));
		int sort=0;
		for(CrawerSpecNameWithSpecVal specNameWithSpecVal:specNameWithSpecValList) {
			YdItemSpecName ydItemSpecName=new YdItemSpecName();
			ydItemSpecName.setCreateTime(new Date());
			ydItemSpecName.setSort(sort++);
			ydItemSpecName.setItemId(ydItem.getId());
			ydItemSpecName.setSpecName(specNameWithSpecVal.getSpecName());
			ydItemSpecNameDao.insertYdItemSpecName(ydItemSpecName);
			System.out.println("插入  specName:"+JSONObject.toJSONString(ydItemSpecName));
			int sort2=0;
			for(CrawerSpecVal val:specNameWithSpecVal.getSpecValList()) {
				YdItemSpecValue specVal=new YdItemSpecValue();
				specVal.setCreateTime(new Date());
				specVal.setItemId(ydItem.getId());
				specVal.setSort(sort2++);
				specVal.setSpecId(ydItemSpecName.getId());
				specVal.setSpecValue(val.getValue());
				specVal.setPropId(Integer.parseInt(val.getPropId()));
				ydItemSpecValueDao.insertYdItemSpecValue(specVal);
				System.out.println("插入 specVal:"+JSONObject.toJSONString(specVal));
			}
		}
		//开始生成sku
		reCreateSku(ydItem);
	}
	
	private static List<String> execute(List<List<String>> valListList) {
		if (valListList.size() == 0) {
			return null;
		}
		if (valListList.size() == 1) {
			return valListList.get(0);
		}

		List<String> firstList = valListList.get(0);
		List<String> secondList = valListList.get(1);
		List<String> combineList = new ArrayList<String>();
		for (String str1 : firstList) {
			for (String str2 : secondList) {
				combineList.add(str1 + "," + str2);
			}
		}
		valListList.remove(firstList);
		valListList.remove(secondList);
		valListList.add(combineList);
		return execute(valListList);
	}
	
	private void reCreateSku(YdItem item) {
		System.out.println("------------------生成sku begin-----------------");
		List<YdItemSpecName> mmSpecNameList = ydItemSpecNameDao.getYdItemSpecNameByItemId(item.getId());
		Map<Integer,YdItemSpecName> specNameMap=specNameListToMap(mmSpecNameList);
		List<YdItemSpecValue> mmSpecValueList = ydItemSpecValueDao.getYdItemSpecValueByItemId(item.getId());
		Map<Integer,YdItemSpecValue> specValMap=specValListToMap(mmSpecValueList);
		
		List<List<String>> valListList=new ArrayList<List<String>>();
		if(mmSpecNameList!=null && mmSpecNameList.size()>0) {
			for(YdItemSpecName spec:mmSpecNameList) {
				List<Integer> idList=ydItemSpecValueDao.getYdItemSpecValueIdBySpecId(spec.getId());
				List<String> idStrList=intListToStrList(idList);
				valListList.add(idStrList);
			}
		}
		
		List<String> specValsList=execute(valListList);
		System.out.println("-----sku 列表  ："+JSONObject.toJSONString(specValsList));
		for(String str:specValsList) {
			System.out.println("--------sku item:"+str);
			YdItemSku sku=new YdItemSku();
			sku.setTitle(item.getTitle());
			sku.setSubTitle(item.getSubTitle());
			sku.setItemId(item.getId());
			sku.setIsEnable("Y");
			String[] arr=str.split(",");
			List<Integer> specValIdList=arrToIntList(arr);
			sku.setSpecValueIdPath(listToStr(specValIdList));
			
			Map<String,String> specNameValueMap=getSpecNameValueMap(specNameMap, specValMap, specValIdList);
			sku.setSpecNameValueJson(JSONObject.toJSONString(specNameValueMap));
			sku.setCreateTime(new Date());
			ydItemSkuDao.insertYdItemSku(sku);
			System.out.println("--sku:"+JSONObject.toJSONString(sku));
		}
	}
	
	private Map<String,String> getSpecNameValueMap(Map<Integer,YdItemSpecName> specNameMap,Map<Integer,YdItemSpecValue> specValMap,List<Integer> specValIdList){
		Map<String,String> map=new HashMap<String,String>();
		for(Integer valId:specValIdList) {
			YdItemSpecValue valObj=specValMap.get(valId);
			YdItemSpecName nameObj=specNameMap.get(valObj.getSpecId());
			map.put(nameObj.getSpecName(), valObj.getSpecValue());
		}
		return map;
	}
	
	private String listToStr(List<Integer> specValIdList) {
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<specValIdList.size();i++) {
			Integer id=specValIdList.get(i);
			sb.append(id);
			if(i!=specValIdList.size()-1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	private Map<Integer,YdItemSpecName> specNameListToMap(List<YdItemSpecName> mmSpecNameList){
		Map<Integer,YdItemSpecName> map=new HashMap<Integer,YdItemSpecName>();
		for(YdItemSpecName item:mmSpecNameList) {
			map.put(item.getId(), item);
		}
		return map;
	}
	private Map<Integer,YdItemSpecValue> specValListToMap(List<YdItemSpecValue> mmSpecValueList){
		Map<Integer,YdItemSpecValue> map=new HashMap<Integer,YdItemSpecValue>();
		for(YdItemSpecValue item:mmSpecValueList) {
			map.put(item.getId(), item);
		}
		return map;
	}
	private List<Integer> arrToIntList(String[] arr){
		if(arr==null) {
			return null;
		}
		List<Integer> list=new ArrayList<Integer>();
		for(String str:arr) {
			list.add(Integer.parseInt(str));
		}
		Collections.sort(list);
		return list;
	}
	private List<String> intListToStrList(List<Integer> idList) {
		if(idList==null) {
			return null;
		}
		List<String> list=new ArrayList<String>();
		for(Integer id:idList) {
			list.add(id+"");
		}
	
		return list;
	}

	@Override
	public void updatePubTime(Integer id, String pubTime) {

		ydItemDao.updatePubTime(id,pubTime);
	}

	@Override
	public void addOrUpdateContent(int itemId, String content) {
		YdItemContent item=ydItemContentDao.getYdItemContentByItemId(itemId);
		if(item==null) {
			item=new YdItemContent();
			item.setCreateTime(new Date());
			item.setContent(content);
			item.setItemId(itemId);
			ydItemContentDao.insertYdItemContent(item);
		}else {
			item.setContent(content);
			item.setUpdateTime(new Date());
			ydItemContentDao.updateYdItemContent(item);
		}
	}

	@Override
	public void upOrDownItem(Integer itemId, String isEnable) {
		ValidateBusinessUtils.assertIdNotNull(itemId,"err_item_id", "非法的商品id");
		ValidateBusinessUtils.assertStringNotBlank(isEnable, "err_enable_empty", "上下架状态不可以为空");

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertNonNull(ydItem, "err_item_not_exist", "商品不存在");

		ydItem.setIsEnable(isEnable);
		ydItemDao.updateYdItem(ydItem);

//		if ("up".equalsIgnoreCase(type)) {
//			ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydItem.getIsEnable()),
//					"err_item_handel_status", "商品已经上架，请勿重复操作");
//			ydItem.setIsEnable("Y");
//		} else if ("down".equalsIgnoreCase(type)) {
//			ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydItem.getIsEnable()),
//					"err_item_handel_status", "商品已经下架，请勿重复操作");
//			ydItem.setIsEnable("N");
//		}
//		ydItemDao.updateYdItem(ydItem);
	}

	@Override
	public void deleteItem(Integer itemId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(itemId, "err_item_id", "非法的商品id");

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertNonNull(ydItem, "err_item_not_exist", "商品不存在");

		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydItem.getIsEnable()),
				"err_item_enable", "上架中的商品无法删除");

		// 删除平台商品
		ydItemDao.deleteItemById(itemId);
		// 删除平台商品sku
		ydItemSkuDao.deleteItemSkuByItemId(itemId);
	}

	@Override
	public YdItemResult getItemDetail(Integer itemId) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(itemId, "err_item_id", "非法的商品id");

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertNonNull(ydItem, "err_item_not_exist", "商品不存在");

		// 设置商品基本属性
		YdItemResult ydItemResult = new YdItemResult();
		BeanUtilExt.copyProperties(ydItemResult, ydItem);

		// 设置商品规格
		List<YdItemSku> skuList = ydItemSkuDao.findItemSkuListByItemId(ydItem.getId());
		ydItemResult.setSkuList(DTOUtils.convertList(skuList, YdItemSkuResult.class));

		// 查询商品图片
		List<YdItemImage> imageList = ydItemImageDao.findImageListByItemId(itemId);
		ydItemResult.setImageList(DTOUtils.convertList(imageList, YdItemImageResult.class));

		// 查询商品图文详情
		YdItemContent ydItemContent = ydItemContentDao.getYdItemContentByItemId(itemId);
		if (ydItemContent != null) {
			ydItemResult.setContent(ydItemContent.getContent());
		}

		// 查询商品规格名
		List<YdItemSpecName> specNameList = ydItemSpecNameDao.findSpecNameListByItemId(itemId);
		ydItemResult.setSpecNameList(DTOUtils.convertList(specNameList, YdItemSpecNameResult.class));

		// 查询商品规格值
		List<YdItemSpecValue> specValueList = ydItemSpecValueDao.findSpecValueListByItemId(itemId);
		ydItemResult.setSpecValueList(DTOUtils.convertList(specValueList, YdItemSpecValueResult.class));
		return ydItemResult;
	}

	/**
	 * 比较页头部图片编辑
	 * @param itemId
	 * @param imageUrl
	 * @throws BusinessException
	 */
	@Override
	public void updateIsHeadImage(Integer itemId, String imageUrl) throws BusinessException {
		ValidateBusinessUtils.assertIdNotNull(itemId, "err_itemId_empty", "itemId不可以为空");

		YdItem ydItem = ydItemDao.getYdItemById(itemId);
		ValidateBusinessUtils.assertNonNull(ydItem, "err_itemId_empty", "itemId不存在");

		ValidateBusinessUtils.assertStringNotBlank(imageUrl, "err_imageUrl_empty", "imageUrl不可以为空");
		ydItem.setHeadImageUrl(imageUrl);
		ydItem.setIsHeadImage("Y");
		ydItemDao.updateYdItem(ydItem);
	}

}

