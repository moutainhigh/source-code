package com.yd.service.impl.cart;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.yd.api.req.MerchantGiftReq;
import com.yd.api.req.MerchantItmSkuReq;
import com.yd.api.result.cart.SettlementDetailResult;
import com.yd.api.result.cart.YdUserCartDetailResult;
import com.yd.api.result.cart.YdUserCartResult;
import com.yd.api.result.coupon.YdMerchantCouponResult;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.item.YdItemSpecNameResult;
import com.yd.api.result.item.YdItemSpecValueResult;
import com.yd.api.result.item.YdMerchantItemSkuResult;
import com.yd.api.service.cart.YdUserCartService;
import com.yd.api.service.coupon.YdMerchantCouponService;
import com.yd.api.service.coupon.YdUserCouponService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.item.YdMerchantItemService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.user.YdUserService;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdItemSpecName;
import com.yd.service.bean.item.YdItemSpecValue;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.dao.item.YdItemSpecNameDao;
import com.yd.service.dao.item.YdItemSpecValueDao;
import com.yd.service.dao.item.YdMerchantItemDao;
import com.yd.service.dao.item.YdMerchantItemSkuDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.cart.YdUserCartDao;
import com.yd.service.bean.cart.YdUserCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Title:用户购物车Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-08 10:08:32
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdUserCartServiceImpl implements YdUserCartService {

	private static final Logger logger = LoggerFactory.getLogger(YdUserCartServiceImpl.class);

	@Resource
	private YdUserCartDao ydUserCartDao;

	@Resource
	private YdItemSpecNameDao ydItemSpecNameDao;

	@Resource
	private YdItemSpecValueDao ydItemSpecValueDao;

	@Resource
	private YdMerchantItemDao ydMerchantItemDao;

	@Resource
	private YdUserCouponService ydUserCouponService;

	@Resource
	private YdMerchantItemSkuDao ydMerchantItemSkuDao;

	@Resource
	private YdUserService ydUserService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Resource
	private YdMerchantItemService ydMerchantItemService;

	@Resource
	private YdMerchantGiftService ydMerchantGiftService;

	@Resource
	private YdMerchantCouponService ydMerchantCouponService;

	@Override
	public YdUserCartResult getYdUserCartById(Integer id) {
		if (id == null || id <= 0) return null;
		YdUserCartResult ydUserCartResult = null;
		YdUserCart ydUserCart = this.ydUserCartDao.getYdUserCartById(id);
		if (ydUserCart != null) {
			ydUserCartResult = new YdUserCartResult();
			BeanUtilExt.copyProperties(ydUserCartResult, ydUserCart);
		}
		return ydUserCartResult;
	}

	@Override
	public List<YdUserCartResult> findYdUserCartByIds(List<Integer> carIdList) {
		if (CollectionUtils.isEmpty(carIdList)) return null;
		List<YdUserCart> dataList = this.ydUserCartDao.findYdUserCartByIds(carIdList);
		return DTOUtils.convertList(dataList, YdUserCartResult.class);
	}

	@Override
	public List<YdUserCartResult> getAll(YdUserCartResult ydUserCartResult) {
		YdUserCart ydUserCart = null;
		if (ydUserCartResult != null) {
			ydUserCart = new YdUserCart();
			BeanUtilExt.copyProperties(ydUserCart, ydUserCartResult);
		}
		List<YdUserCart> dataList = this.ydUserCartDao.getAll(ydUserCart);
		return DTOUtils.convertList(dataList, YdUserCartResult.class);
	}

	/**
	 * 添加商品到购物车
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param merchantItemSkuId 商户商品skuId
	 * @param num		添加的数量
	 * @param type		shop(页面加入购物车) cart(购物车继续添加修改)
	 */
	@Override
	public void addCart(Integer userId, Integer merchantId, Integer merchantItemSkuId, Integer num, String type) throws BusinessException  {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		ValidateBusinessUtils.assertStringNotBlank(type, "err_empty_type", "参数type不可以为空");

		ValidateBusinessUtils.assertIdNotNull(merchantItemSkuId, "err_sku_id", "错误的商品skuId");
		YdMerchantItemSku ydMerchantItemSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(merchantItemSkuId);
		ValidateBusinessUtils.assertNonNull(ydMerchantItemSku, "err_not_exist_item", "商品sku不存在");
		ValidateBusinessUtils.assertFalse(ydMerchantItemSku.getIsEnable().equalsIgnoreCase("N"),
				"err_sku_enable", ydMerchantItemSku.getTitle() + "已经下架");

		YdMerchantItem ydMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantItemSku.getMerchantItemId());
		ValidateBusinessUtils.assertNonNull(ydMerchantItem, "err_item_id", "商品不存在");
		ValidateBusinessUtils.assertFalse(ydMerchantItem.getIsEnable().equalsIgnoreCase("N"),
				"err_item_enable", ydMerchantItem.getTitle() + "已经下架");

		// 判断是新增还是修改
		YdUserCart ydUserCart = new YdUserCart();
		ydUserCart.setUserId(userId);
		ydUserCart.setMerchantId(merchantId);
		ydUserCart.setMerchantSkuId(merchantItemSkuId);
		List<YdUserCart> dataList = this.ydUserCartDao.getAll(ydUserCart);
		if (CollectionUtils.isEmpty(dataList)) {
			ydUserCart.setNum(num);
			ydUserCart.setCreateTime(new Date());
			this.ydUserCartDao.insertYdUserCart(ydUserCart);
		} else {
			ydUserCart = dataList.get(0);
			if ("shop".equals(type)) {
				ydUserCart.setNum(ydUserCart.getNum() + num);
			} else {
				ydUserCart.setNum(num);
			}
			this.ydUserCartDao.updateYdUserCart(ydUserCart);
		}
	}

	/**
	 * 购物车商品更换规格
	 * @param userId
	 * @param merchantId
	 * @param skuId
	 * @param num
	 * @param cartId
	 */
	@Override
	public void updateCart(Integer userId, Integer merchantId, Integer skuId, Integer num, Integer cartId) {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		ValidateBusinessUtils.assertIdNotNull(skuId, "err_sku_id", "错误的商品skuId");
		ValidateBusinessUtils.assertIdNotNull(cartId, "err_empty_cart_id", "购物车id不可以为空");

		YdMerchantItemSku ydMerchantItemSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(skuId);
		ValidateBusinessUtils.assertNonNull(ydMerchantItemSku, "err_not_exist_item", "商品sku不存在");
		ValidateBusinessUtils.assertFalse(ydMerchantItemSku.getIsEnable().equalsIgnoreCase("N"),
				"err_sku_enable", ydMerchantItemSku.getTitle() + "已经下架");

		YdMerchantItem ydMerchantItem = this.ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantItemSku.getMerchantItemId());
		ValidateBusinessUtils.assertNonNull(ydMerchantItem, "err_item_id", "商品不存在");
		ValidateBusinessUtils.assertFalse(ydMerchantItem.getIsEnable().equalsIgnoreCase("N"),
				"err_item_enable", ydMerchantItem.getTitle() + "已经下架");

		YdUserCart userCart = this.ydUserCartDao.getYdUserCartById(cartId);
		ValidateBusinessUtils.assertNonNull(userCart, "err_empty_cart_id", "购物车id不存在");

		YdUserCart ydUserCart = new YdUserCart();
		ydUserCart.setUserId(userId);
		ydUserCart.setMerchantId(merchantId);
		ydUserCart.setMerchantSkuId(skuId);
		List<YdUserCart> dataList = this.ydUserCartDao.getAll(ydUserCart);

		// 如果更换的sku已经存在，删除本次更换的购物车id，保留以前的
		if (CollectionUtils.isNotEmpty(dataList)) {
			List<Integer> cartIdList = new ArrayList<>();
			cartIdList.add(cartId);
			this.ydUserCartDao.deleteCartByIdList(userId, merchantId, cartIdList);
		} else {
			userCart.setMerchantSkuId(skuId);
			userCart.setNum(num);
			this.ydUserCartDao.updateYdUserCart(userCart);
		}
	}

	/**
	 * 删除购物车商品
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param carIdList	 购物车ids
	 */
	@Override
	public void deleteCart(Integer userId, Integer merchantId, List<Integer> carIdList) throws BusinessException {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(carIdList),
				"err_empty_car_id_list", "购物车id不可以为空");
		this.ydUserCartDao.deleteCartByIdList(userId, merchantId, carIdList);
	}

	/**
	 * 清空购物车
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 */
	@Override
	public void clearCart(Integer userId, Integer merchantId) throws BusinessException  {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		this.ydUserCartDao.clearCart(userId, merchantId);
	}

	/**
	 * 查询购物车列表
	 * @param userId
	 * @param merchantId
	 * @return
	 */
	@Override
	public Map<String, List<YdUserCartDetailResult>> findCartList(Integer userId, Integer merchantId) throws BusinessException {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		YdUserCart ydUserCart = new YdUserCart();
		ydUserCart.setUserId(userId);
		ydUserCart.setMerchantId(merchantId);
		List<YdUserCart> carList = this.ydUserCartDao.getAll(ydUserCart);
		if (CollectionUtils.isEmpty(carList)) return null;

		List<YdUserCartDetailResult> canUseList = new ArrayList<>();
		List<YdUserCartDetailResult> cannotUseList = new ArrayList<>();

		for (YdUserCart userCart : carList) {
			// 查询商品的信息
			YdUserCartDetailResult ydUserCartDetailResult = new YdUserCartDetailResult();
			ydUserCartDetailResult.setId(userCart.getId());
			ydUserCartDetailResult.setNum(userCart.getNum());

			YdMerchantItemSku ydMerchantItemSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(userCart.getMerchantSkuId());
			if (ydMerchantItemSku != null) {
				YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(ydMerchantItemSku.getMerchantItemId());
				String title = StringUtils.isEmpty(ydMerchantItemSku.getTitle()) ? ydMerchantItem.getTitle() : ydMerchantItemSku.getTitle();
				String itemCover = StringUtils.isEmpty(ydMerchantItemSku.getSkuCover()) ? ydMerchantItem.getCover() : ydMerchantItemSku.getSkuCover();
				ydUserCartDetailResult.setTitle(title);
				ydUserCartDetailResult.setItemCover(itemCover);
				ydUserCartDetailResult.setSalePrice(ydMerchantItemSku.getSalePrice());
				ydUserCartDetailResult.setMerchantSkuId(ydMerchantItemSku.getId());
				ydUserCartDetailResult.setFirstCategoryId(ydMerchantItemSku.getFirstCategoryId());
				ydUserCartDetailResult.setSecondCategoryId(ydMerchantItemSku.getSecondCategoryId());
				ydUserCartDetailResult.setSpecNameValueJson(ydMerchantItemSku.getSpecNameValueJson());
				ydUserCartDetailResult.setSpecValueIdPath(ydMerchantItemSku.getSpecValueIdPath());

				// 查询商品规格
				List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findSkuListByMerchantItemId(ydMerchantItemSku.getMerchantItemId(), "Y");
				List<YdMerchantItemSkuResult> skuResultList = DTOUtils.convertList(skuList, YdMerchantItemSkuResult.class);

				// 查询商品规格名
				List<YdItemSpecName> specNameList = ydItemSpecNameDao.findSpecNameListByItemId(ydMerchantItemSku.getItemId());
				List<YdItemSpecNameResult> itemSpecNameResultList = DTOUtils.convertList(specNameList, YdItemSpecNameResult.class);

				// 查询商品规格值
				List<YdItemSpecValue> specValueList = ydItemSpecValueDao.findSpecValueListByItemId(ydMerchantItemSku.getItemId());
				List<YdItemSpecValueResult> specValueResultList = DTOUtils.convertList(specValueList, YdItemSpecValueResult.class);

				// 将规格值放入规格对象之中
				itemSpecNameResultList.forEach(name -> name.setItemSpecValues(specValueResultList
						.stream()
						.filter(value -> value.getSpecId().equals(name.getId()))
						.collect(Collectors.toList())));

				// 查询商品可领取优惠券列表
				List<YdMerchantCouponResult> merchantCouponList = ydMerchantCouponService.findFrontMerchantCouponList(
						ydMerchantItemSku.getMerchantItemId(), userId, merchantId);
				ydUserCartDetailResult.setMerchantCouponList(merchantCouponList);

				ydUserCartDetailResult.setSkuList(skuResultList);
				ydUserCartDetailResult.setSpecNameList(itemSpecNameResultList);

				ydUserCartDetailResult.setSpecValueList(specValueResultList);
				setSkuSpecIdJson(skuResultList, specValueResultList);

				if ("N".equalsIgnoreCase(ydMerchantItem.getIsEnable())) {
					cannotUseList.add(ydUserCartDetailResult);
					continue;
				}
				if ("N".equalsIgnoreCase(ydMerchantItemSku.getIsEnable())) {
					cannotUseList.add(ydUserCartDetailResult);
					continue;
				}
				canUseList.add(ydUserCartDetailResult);
			}
		}
		Map<String, List<YdUserCartDetailResult>> resultMap = new HashMap<>();
		resultMap.put("canUseList", canUseList);
		resultMap.put("cannotUseList", cannotUseList);
		logger.info("====用户购物车返回的数据=" + JSON.toJSONString(resultMap, true));
		return resultMap;
	}

	/**
	 * 购物车选中计算优惠券明细
	 * @param userId	 用户id
	 * @param merchantId 商户id
	 * @param carIdList	 购物车idList
	 * @return
	 * 		totalMonty		总金额
	 * 		discountMoney	优惠券金额
	 * 		payMoney		优惠后金额
	 * 		giftMoney		可选礼品金额
	 */
	@Override
	public Map<String, Object> calcCartCheckedMonty(Integer userId, Integer merchantId, List<Integer> carIdList) throws BusinessException {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		ValidateBusinessUtils.assertCollectionNotEmpty(carIdList, "err_empty_car_id_list", "购物车id不可以为空");

		List<MerchantItmSkuReq> itemSkuReqList = new ArrayList<>();

		BigDecimal totalMonty = new BigDecimal(0.00), discountMoney = new BigDecimal(0.00);

		// 查询选中的购物车商品
		List<YdUserCart> cartList = this.ydUserCartDao.findCartListByIdList(userId, merchantId, carIdList);

		// 查询商品sku详情, 存入map
		List<Integer> merchantSkuIdList = cartList.stream().map(YdUserCart :: getMerchantSkuId).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(merchantSkuIdList)) {
			return null;
		}

		List<YdMerchantItemSku> skuList = ydMerchantItemSkuDao.findYdMerchantItemSkuListByIds(merchantSkuIdList);
		List<YdMerchantItemSkuResult> skuResultList = DTOUtils.convertList(skuList, YdMerchantItemSkuResult.class);
		Map<Integer, YdMerchantItemSkuResult> merchantSkuMap = new HashMap<>();
		skuResultList.forEach(ydMerchantItemSku -> {
			merchantSkuMap.put(ydMerchantItemSku.getId(), ydMerchantItemSku);
		});

		// 计算商品总价钱
		for (YdUserCart ydUserCart : cartList) {
			YdMerchantItemSkuResult ydMerchantItemSku = merchantSkuMap.get(ydUserCart.getMerchantSkuId());
			// （售价 * 数量 ） + 其余计算 过的总和
			totalMonty = new BigDecimal(ydMerchantItemSku.getSalePrice() + "")
					.multiply(new BigDecimal(ydUserCart.getNum()))
					.add(totalMonty)
					.setScale(2, BigDecimal.ROUND_UP);

			ydMerchantItemSku.setNum(ydUserCart.getNum());
			// 设置查询礼品参数
			MerchantItmSkuReq itmSkuReq = new MerchantItmSkuReq();
			itmSkuReq.setNum(ydUserCart.getNum());
			itmSkuReq.setMerchantItemSkuId(ydUserCart.getMerchantSkuId());
			itemSkuReqList.add(itmSkuReq);
		}

		// 计算优惠券金额,根据商品去查询可用优惠券，去重，自动筛选面值最大的优惠券
		List<Integer> merchantItemIdList = skuList.stream().map(YdMerchantItemSku::getMerchantItemId).collect(Collectors.toList());
		List<YdUserCouponResult> couponList = ydUserCouponService.findUserCanUseCouponListByItemIds(userId, merchantId, merchantItemIdList, skuResultList);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("totalMonty", totalMonty.doubleValue());
		if (CollectionUtils.isNotEmpty(couponList)) {
			resultMap.put("discountMoney", couponList.get(0).getCouponPrice());
			resultMap.put("payMoney", MathUtils.subtract(totalMonty.doubleValue(), couponList.get(0).getCouponPrice(), 2));
		} else {
			resultMap.put("discountMoney", 0.00);
			resultMap.put("payMoney", totalMonty.doubleValue());
		}

		logger.info("====购物车选中计算优惠券明细=" + JSON.toJSONString(resultMap));
		return resultMap;
	}

	/**
	 * 订单结算详情页面数据
	 * @param userId	   用户id
	 * @param merchantId   商户id
	 * @param skuId		   商户商品skuId
	 * @param num		   商品购买数量
	 * @param type		   car | item
	 * @param carIds	   购物车carIds
	 * @param giftList	   礼品idList id and num
	 * @return
	 */
	@Override
	public SettlementDetailResult settlementDetail(Integer userId, Integer merchantId, Integer skuId, Integer num,
											String type, String carIds, List<MerchantGiftReq> giftList) throws BusinessException {
		// 校验用户信息, 商户信息
		ydUserService.checkUserInfo(userId);
		ydMerchantService.checkMerchantInfo(merchantId);

		Map<Integer, Integer> giftMap = new HashMap<>();
		giftList.forEach(ydMerchantGiftResult -> {
			ValidateBusinessUtils.assertNonNull(ydMerchantGiftResult.getId(), "err_empty_gift_id", "礼品id不可以为空");
			ValidateBusinessUtils.assertNonNull(ydMerchantGiftResult.getNum(), "err_empty_gift_num", "礼品数量不可以为空");
			giftMap.put(ydMerchantGiftResult.getId(), ydMerchantGiftResult.getNum());
		});

		// 根据是购物车数据还是商品详情提交查询商品规格list
		List<YdMerchantItemSkuResult> skuList = ydMerchantItemService.findMerchantItemListByCarIdList(
				userId, merchantId, skuId, num, carIds, type);

		// 查询优惠券列表
		List<Integer> itemIdList = skuList.stream().map(YdMerchantItemSkuResult :: getMerchantItemId).collect(Collectors.toList());
		List<YdUserCouponResult> couponList = ydUserCouponService.findUserCanUseCouponListByItemIds(userId, merchantId, itemIdList, skuList);

		// 查询礼品列表
		List<YdMerchantGiftResult> giftResultList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(giftList)) {
			List<Integer> giftIdList = giftList.stream().map(MerchantGiftReq :: getId).collect(Collectors.toList());
			giftResultList = ydMerchantGiftService.findYdMerchantGiftByIdList(merchantId, giftIdList);
			giftResultList.forEach(ydMerchantGiftResult -> {
				ydMerchantGiftResult.setNum(giftMap.get(ydMerchantGiftResult.getId()));
			});
		}

		SettlementDetailResult settlementDetailResult = new SettlementDetailResult();
		settlementDetailResult.setSkuList(skuList);
		settlementDetailResult.setGiftList(giftResultList);
		settlementDetailResult.setCouponList(couponList);
		settlementDetailResult.setCarIds(carIds);
		settlementDetailResult.setSkuId(skuId);
		settlementDetailResult.setType(type);
		settlementDetailResult.setNum(num);
		logger.info("====订单结算详情页面数据=" + JSON.toJSONString(settlementDetailResult));
		return settlementDetailResult;
	}

	// -----------------------     private method     ------------------------

	private void setSkuSpecIdJson(List<YdMerchantItemSkuResult> skuResultList, List<YdItemSpecValueResult> specValueResultList) {
		if (!CollectionUtils.isEmpty(specValueResultList)) {
			Map<Integer, Integer> specNameMap = new HashMap<>();
			specValueResultList.forEach((specValue) -> {
				specNameMap.put(specValue.getId(), specValue.getSpecId());
			});

			skuResultList.forEach((sku) -> {
				Map<Integer, Integer> nameValueMap = new HashMap<>();
				Stream.of(sku.getSpecValueIdPath().split(","))
						.collect(Collectors.toList())
						.forEach((valueId) -> {
							nameValueMap.put(specNameMap.get(Integer.valueOf(valueId)), Integer.valueOf(valueId));
						});
				sku.setSpecNameValueIdJson(JSON.toJSONString(nameValueMap));
			});
		}
	}

}

