package com.yd.service.impl.gift;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import com.yd.api.result.gift.YdGiftResult;
import com.yd.api.result.gift.YdMerchantGiftCartResult;
import com.yd.api.result.gift.YdMerchantGiftResult;
import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderResult;
import com.yd.api.service.gift.YdMerchantGiftCartService;
import com.yd.api.service.gift.YdMerchantGiftService;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantGiftTransService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdGiftOrderDetailService;
import com.yd.api.service.order.YdGiftOrderService;
import com.yd.core.enums.YdMerchantTransSourceEnum;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdGift;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.merchant.YdMerchantGiftAccount;
import com.yd.service.dao.gift.YdGiftDao;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.merchant.YdMerchantGiftAccountDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import com.yd.service.dao.gift.YdMerchantGiftCartDao;
import com.yd.service.bean.gift.YdMerchantGiftCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Title:商户礼品购物车Service实现
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-01 14:29:29
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdMerchantGiftCartServiceImpl implements YdMerchantGiftCartService {

	private static final Logger logger = LoggerFactory.getLogger(YdMerchantGiftCartServiceImpl.class);

	@Resource
	private YdGiftDao ydGiftDao;

	@Resource
	private YdMerchantGiftDao ydMerchantGiftDao;

	@Resource
	private YdMerchantGiftService ydMerchantGiftService;

	@Resource
	private YdMerchantGiftCartDao ydMerchantGiftCartDao;

	@Resource
	private YdMerchantGiftAccountDao ydMerchantGiftAccountDao;

	@Resource
	private YdGiftOrderService ydGiftOrderService;

	@Resource
	private YdGiftOrderDetailService ydGiftOrderDetailService;

	@Resource
	private YdMerchantGiftTransService ydMerchantGiftTransService;

	@Resource
	private YdMerchantGiftAccountService ydMerchantGiftAccountService;

	@Resource
	private YdMerchantService ydMerchantService;

	@Override
	public YdMerchantGiftCartResult getYdMerchantGiftCartById(Integer id) {
		if (id == null || id <= 0) {
			return null;
		}
		YdMerchantGiftCartResult ydMerchantGiftCartResult = null;
		YdMerchantGiftCart ydMerchantGiftCart = this.ydMerchantGiftCartDao.getYdMerchantGiftCartById(id);
		if (ydMerchantGiftCart != null) {
			ydMerchantGiftCartResult = new YdMerchantGiftCartResult();
			BeanUtilExt.copyProperties(ydMerchantGiftCartResult, ydMerchantGiftCart);
		}
		
		return ydMerchantGiftCartResult;
	}

	@Override
	public List<YdMerchantGiftCartResult> getAll(YdMerchantGiftCartResult ydMerchantGiftCartResult) {
		YdMerchantGiftCart ydMerchantGiftCart = null;
		if (ydMerchantGiftCartResult != null) {
			ydMerchantGiftCart = new YdMerchantGiftCart();
			BeanUtilExt.copyProperties(ydMerchantGiftCart, ydMerchantGiftCartResult);
		}
		List<YdMerchantGiftCart> dataList = this.ydMerchantGiftCartDao.getAll(ydMerchantGiftCart);
		List<YdMerchantGiftCartResult> resultList = DTOUtils.convertList(dataList, YdMerchantGiftCartResult.class);
		return resultList;
	}

	/**
	 * 添加礼品到购物车
	 * @param merchantId 商户ID
	 * @param giftId	礼品id
	 * @param num		数量
	 * @param type shop(页面加入购物车) cart(购物车继续添加修改)
	 * @throws BusinessException
	 */
	@Override
	public void addMerchantGiftCart(Integer merchantId, Integer giftId, Integer num, String type) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();
		ValidateBusinessUtils.assertFalse(num == null || num <= 0,
				"err_gift_num", "商品数量必须大于0");
		ValidateBusinessUtils.assertFalse(giftId == null || giftId<= 0,
				"err_gift__id", "错误的礼品id");

		YdGift ydGift = ydGiftDao.getYdGiftById(giftId);
		ValidateBusinessUtils.assertFalse(ydGift == null,
				"err_not_exist_gift", "礼品不存在");
		ValidateBusinessUtils.assertFalse("N".equalsIgnoreCase(ydGift.getIsEnable()),
				"err_not_enable_gift", "礼品暂未上架");
		ValidateBusinessUtils.assertFalse("Y".equalsIgnoreCase(ydGift.getIsFlag()),
				"err_is_delete_gift", "礼品已被删除");

		// 判断是新增还是修改
		YdMerchantGiftCart ydMerchantGiftCart = new YdMerchantGiftCart();
		ydMerchantGiftCart.setGiftId(giftId);
		ydMerchantGiftCart.setMerchantId(merchantId);
		List<YdMerchantGiftCart> cartList = ydMerchantGiftCartDao.getAll(ydMerchantGiftCart);
		if (CollectionUtils.isEmpty(cartList)) {
			ydMerchantGiftCart.setCreateTime(new Date());
			ydMerchantGiftCart.setUpdateTime(new Date());
			ydMerchantGiftCart.setNum(num);
			ydMerchantGiftCart.setSupplierId(ydGift.getSupplierId());
			this.ydMerchantGiftCartDao.insertYdMerchantGiftCart(ydMerchantGiftCart);
		} else {
			ydMerchantGiftCart = cartList.get(0);
			if ("shop".equals(type)) {
				ydMerchantGiftCart.setNum(ydMerchantGiftCart.getNum() + 1);
			} else {
				ydMerchantGiftCart.setNum(num);
			}
			ydMerchantGiftCart.setUpdateTime(new Date());
			this.ydMerchantGiftCartDao.updateYdMerchantGiftCart(ydMerchantGiftCart);
		}
	}
	
	@Override
	public void updateYdMerchantGiftCart(YdMerchantGiftCartResult ydMerchantGiftCartResult) {
		if (null != ydMerchantGiftCartResult) {
			ydMerchantGiftCartResult.setUpdateTime(new Date());
			YdMerchantGiftCart ydMerchantGiftCart = new YdMerchantGiftCart();
			BeanUtilExt.copyProperties(ydMerchantGiftCart, ydMerchantGiftCartResult);
			this.ydMerchantGiftCartDao.updateYdMerchantGiftCart(ydMerchantGiftCart);
		}
	}

	/**
	 * 查询购物车商品数量
	 * @param merchantId
	 * @return
	 */
	@Override
	public Integer getMerchantGiftCartCount(Integer merchantId) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		YdMerchantGiftCart ydMerchantGiftCart = new YdMerchantGiftCart();
		ydMerchantGiftCart.setMerchantId(merchantId);
		return this.ydMerchantGiftCartDao.getYdMerchantGiftCartCount(ydMerchantGiftCart);
	}

	/**
	 * 商户清空礼品购物车
	 * @param merchantId
	 * @throws BusinessException
	 */
	@Override
	public void clearMerchantGiftCart(Integer merchantId) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		this.ydMerchantGiftCartDao.deleteMerchantGiftCart(merchantId);
	}

	/**
	 * 查询商户购物车数据
	 * @param merchantId
	 * @return
	 */
	@Override
	public List<YdGiftResult> getMerchantGiftCart(Integer merchantId) {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		List<YdMerchantGiftCart> cartList = this.ydMerchantGiftCartDao.getYdMerchantGiftCartByMerchantId(merchantId);
		if (CollectionUtils.isEmpty(cartList)) return null;
		List<Integer> giftIdList = cartList.stream().map(YdMerchantGiftCart::getGiftId).collect(Collectors.toList());

		List<YdGift> giftList = ydGiftDao.getYdGiftByIdList(giftIdList);
		Map<Integer, YdMerchantGiftCart> giftCartMap = new HashMap<>();
		cartList.forEach(ydMerchantGiftCart -> {
			giftCartMap.put(ydMerchantGiftCart.getGiftId(), ydMerchantGiftCart);
		});

		List<YdGiftResult> ydGiftResultList = DTOUtils.convertList(giftList, YdGiftResult.class);
		ydGiftResultList.forEach(ydGiftResult -> {
			ydGiftResult.setNum(giftCartMap.get(ydGiftResult.getId()).getNum());
			ydGiftResult.setCarId(giftCartMap.get(ydGiftResult.getId()).getId());
		});
		return ydGiftResultList;
	}

	/**
	 * 商户礼品购物车提交订单
	 * @param merchantId	商户id
	 * @param carIds		购物车ids
	 * @return payPrice balance
	 * @throws BusinessException
	 */
	@Override
	public Map<String, Double> submitMerchantGiftCart(Integer merchantId, String carIds) throws BusinessException{
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(carIdList),
				"err_empty_gift_car_ids", "购物车id不可以为空");

		List<YdMerchantGiftCart> giftCarList = ydMerchantGiftCartDao.getYdMerchantGiftCartByIdList(carIdList);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(giftCarList),
				"err_gift_car_ids", "商户礼品购物车不存在");

		// 根据礼品购物车查询平台礼品,
		List<Integer> giftIdList = giftCarList.stream().map(YdMerchantGiftCart::getGiftId).collect(Collectors.toList());
		List<YdGift> giftList = ydGiftDao.getYdGiftByIdList(giftIdList);
		Map<Integer, YdGift> giftMap = getPlatGiftMap(giftList);

		// 查询礼品账户余额
		YdMerchantGiftAccountResult ydMerchantGiftAccount = ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(merchantId);
		Double totalSalePrice = getGiftCartTotalSalePrice(giftCarList, giftMap);

		Map<String, Double> result = new HashMap<>(4);
		result.put("balance", ydMerchantGiftAccount.getBalance());
		result.put("payPrice", totalSalePrice);
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean toPay(Integer merchantId, String password, String carIds) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(password),
				"err_empty_pay_password", "请输入支付密码");

		ValidateBusinessUtils.assertFalse(StringUtil.isEmpty(storeInfo.getPayPassword()),
				"err_set_pay_password", "请先设置门店支付密码");

		ValidateBusinessUtils.assertFalse(!storeInfo.getPayPassword().equalsIgnoreCase(PasswordUtil.encryptPassword(password)),
				"err_pay_password", "支付密码错误");

		List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());

		List<YdMerchantGiftCart> giftCarList = ydMerchantGiftCartDao.getYdMerchantGiftCartByIdList(carIdList);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(giftCarList),
				"err_gift_car_ids", "商户礼品购物车不存在");

		// 根据礼品购物车查询平台礼品,
		List<Integer> giftIdList = giftCarList.stream().map(YdMerchantGiftCart::getGiftId).collect(Collectors.toList());
		List<YdGift> giftList = ydGiftDao.getYdGiftByIdList(giftIdList);

		Map<Integer, YdGift> giftMap = getPlatGiftMap(giftList);

		// 计算所需总金额
		Double totalSalePrice = getGiftCartTotalSalePrice(giftCarList, giftMap);
		Double totalMarketPrice = getGiftCartTotalMarketPrice(giftCarList, giftMap);

		// 查询账户余额
		YdMerchantGiftAccount ydMerchantGiftAccount = ydMerchantGiftAccountDao.getYdMerchantGiftAccountByMerchantId(merchantId);
		ValidateBusinessUtils.assertFalse(totalSalePrice > ydMerchantGiftAccount.getBalance(),
				"err_gift_account_balance", "商户礼品余额不足");

		// 扣减账户余额
		int result = ydMerchantGiftAccountDao.reduceGiftAccountBalance(merchantId, totalSalePrice);
		if (result > 0) {
			// 创建商户礼品订单, 创建商户礼品订单详情
			int giftSum = giftCarList.stream().mapToInt(YdMerchantGiftCart::getNum).sum();
			logger.info("====商户后台礼品商城下单totalSalePrice=" + totalSalePrice + " ,totalMarketPrice=" + totalMarketPrice + " ,giftSum=" + giftSum);
			YdGiftOrderResult giftOrderResult = ydGiftOrderService.addMerchantGiftOrder(merchantId, giftSum, giftCarList.size(), totalSalePrice, totalMarketPrice);
			createMerchantGiftOrderDetail(giftOrderResult.getId(), giftCarList, giftMap);

			// 将从购物车购买的礼品加入到商户礼品库
			addMerchantGift(merchantId, giftCarList, giftMap);

			// 增加账户流水
			Double afterBalance = new BigDecimal(ydMerchantGiftAccount.getBalance() + "")
					.add(new BigDecimal(totalSalePrice + ""))
					.setScale(2, BigDecimal.ROUND_UP).doubleValue();
			ydMerchantGiftTransService.addMerchantGiftTrans(merchantId, giftOrderResult.getId() + "",
					YdMerchantTransSourceEnum.PURCHASE_GIFT.getCode(),"OUT", YdMerchantTransSourceEnum.PURCHASE_GIFT.getDescription(),
					"PAY_SUCCESS", totalSalePrice, ydMerchantGiftAccount.getBalance(), afterBalance);

			// 清空购买的购物车id
			ydMerchantGiftCartDao.deleteMerchantGiftCartByIdList(merchantId, carIdList);
		} else {
			ValidateBusinessUtils.assertFalse(true, "err_gift_account_balance", "账户余额变动，请重试");
		}
		return true;
	}

	/**
	 * 删除商户购物车礼品
	 * @param merchantId
	 * @param carIds
	 * @throws BusinessException
	 */
	@Override
	public void deleteMerchantGiftCart(Integer merchantId, String carIds) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(carIdList),
				"err_gift_car_ids", "请选择正确的礼品，不可以不选择");
		this.ydMerchantGiftCartDao.deleteMerchantGiftCartByIdList(merchantId, carIdList);
	}

	@Override
	public Double getGiftCartTotalPrice(Integer merchantId, String carIds) throws BusinessException {
		YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
		merchantId = storeInfo.getId();

		List<Integer> carIdList = Arrays.stream(StringUtils.split(carIds, ","))
				.map(Integer::valueOf).collect(Collectors.toList());

		List<YdMerchantGiftCart> giftCarList = ydMerchantGiftCartDao.getYdMerchantGiftCartByIdList(carIdList);
		ValidateBusinessUtils.assertFalse(CollectionUtils.isEmpty(giftCarList),
				"err_gift_car_ids", "商户礼品购物车不存在");

		// 根据礼品购物车查询平台礼品,
		List<Integer> giftIdList = giftCarList.stream().map(YdMerchantGiftCart::getGiftId).collect(Collectors.toList());
		List<YdGift> giftList = ydGiftDao.getYdGiftByIdList(giftIdList);

		Map<Integer, YdGift> giftMap = getPlatGiftMap(giftList);

		// 计算所需总金额
		Double totalSalePrice = getGiftCartTotalSalePrice(giftCarList, giftMap);
		return totalSalePrice;
	}

	//   ---------------------------------------       private method      ----------------------------------------------------

	/**
	 * 获取礼品库礼品，存入map
	 * @param giftList	礼品集合
	 * @return	Map<Integer,YdGift>
	 */
	private Map<Integer, YdGift> getPlatGiftMap(List<YdGift> giftList) {
		Map<Integer, YdGift> giftMap = new HashMap<>(12);
		giftList.forEach(ydGift -> {
			giftMap.put(ydGift.getId(), ydGift);
		});
		return giftMap;
	}

	/**
	 * 计算选中礼品购物车的总金额
	 * @param giftCarList
	 * @param giftMap
	 * @return
	 */
	private Double getGiftCartTotalSalePrice(List<YdMerchantGiftCart> giftCarList, Map<Integer, YdGift> giftMap) {
		Double totalSalePrice = 0.00;
		for (YdMerchantGiftCart ydMerchantGiftCart : giftCarList) {
			YdGift ydGift = giftMap.get(ydMerchantGiftCart.getGiftId());
			ValidateBusinessUtils.assertFalse("N".equals(ydGift.getIsEnable()),
					"err_gift_enable", ydGift.getTitle() + "已下架，请移除购物车");
			ValidateBusinessUtils.assertFalse("Y".equals(ydGift.getIsFlag()),
					"err_gift_enable", ydGift.getTitle() + "已删除，请移除购物车");

			// 计算阶梯价格
			Double salePrice = 0.0;
			int giftNum = ydMerchantGiftCart.getNum();
			if (ydGift.getLadderNumber() == null || ydGift.getLadderPrice() == null) {
				salePrice = ydGift.getSalePrice();
			} else {
				salePrice = giftNum < ydGift.getLadderNumber() ? ydGift.getSalePrice() : ydGift.getLadderPrice();
			}
			totalSalePrice += MathUtils.multiply(salePrice, giftNum, 2);
		}
		return totalSalePrice;
	}

	/**
	 * 获取订单市场价总金额
	 * @param giftCarList
	 * @param giftMap
	 * @return
	 */
	private Double getGiftCartTotalMarketPrice(List<YdMerchantGiftCart> giftCarList, Map<Integer, YdGift> giftMap) {
		Double totalMarketPrice = 0.00;
		for (YdMerchantGiftCart ydMerchantGiftCart : giftCarList) {
			YdGift ydGift = giftMap.get(ydMerchantGiftCart.getGiftId());
			// 计算阶梯价格
			Double marketPrice = ydGift.getMarketPrice();
			int giftNum = ydMerchantGiftCart.getNum();
			totalMarketPrice += new BigDecimal(marketPrice + "")
					.multiply(new BigDecimal(giftNum + ""))
					.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		}
		return totalMarketPrice;
	}

	/**
	 * 创建礼品订单详情
	 * @param giftOrderId
	 * @param giftCarList
	 * @param giftMap
	 */
	private void createMerchantGiftOrderDetail(Integer giftOrderId, List<YdMerchantGiftCart> giftCarList, Map<Integer, YdGift> giftMap) {
		giftCarList.forEach(ydMerchantGiftCart -> {
			YdGift ydGift = giftMap.get(ydMerchantGiftCart.getGiftId());

			// 计算阶梯价格
			int giftNum = ydMerchantGiftCart.getNum();
			Double marketPrice = ydGift.getMarketPrice();
			Double purchasePrice = ydGift.getPurchasePrice();

			// 计算阶梯价格
			Double salePrice = 0.0;
			if (ydGift.getLadderNumber() == null || ydGift.getLadderPrice() == null) {
				salePrice = ydGift.getSalePrice();
			} else {
				salePrice = giftNum < ydGift.getLadderNumber() ? ydGift.getSalePrice() : ydGift.getLadderPrice();
			}
			ydGiftOrderDetailService.createMerchantGiftOrderDetail(giftOrderId, ydMerchantGiftCart.getSupplierId(),
					null, ydMerchantGiftCart.getGiftId(), giftNum, marketPrice, salePrice, purchasePrice);
		});
	}

	/**
	 * 将从购物车购买的礼品加入到商户礼品库
	 * @param merchantId
	 * @param giftCarList
	 * @param giftMap
	 */
	private void addMerchantGift(Integer merchantId, List<YdMerchantGiftCart> giftCarList, Map<Integer,YdGift> giftMap) {
		Date nowDate = new Date();
		// 这个不累计原来的
		/*giftCarList.forEach(ydMerchantGiftCart -> {
			YdGift ydGift = giftMap.get(ydMerchantGiftCart.getGiftId());
			YdMerchantGiftResult merchantGiftResult = new YdMerchantGiftResult();
			merchantGiftResult.setCreateTime(nowDate);
			merchantGiftResult.setGiftType("merchant");
			merchantGiftResult.setMerchantId(merchantId);
			merchantGiftResult.setSupplierId(ydMerchantGiftCart.getSupplierId());
			merchantGiftResult.setGiftId(ydMerchantGiftCart.getGiftId());
			merchantGiftResult.setTitle(ydGift.getTitle());
			merchantGiftResult.setSubTitle(ydGift.getSubTitle());
			merchantGiftResult.setSalePrice(ydGift.getSalePrice());
			merchantGiftResult.setMarketPrice(ydGift.getMarketPrice());
			merchantGiftResult.setImageUrl(ydGift.getImageUrl());
			merchantGiftResult.setGiftDesc(ydGift.getGiftDesc());
			merchantGiftResult.setIsEnable("N");
			ydMerchantGiftService.insertYdMerchantGift(merchantGiftResult);
		});*/

		// 累计原来买过的
		giftCarList.forEach(ydMerchantGiftCart -> {
			YdGift ydGift = giftMap.get(ydMerchantGiftCart.getGiftId());

			// 先查询商户礼品库是否购买过
			YdMerchantGift ydMerchantGift = new YdMerchantGift();
			ydMerchantGift.setMerchantId(merchantId);
			ydMerchantGift.setGiftId(ydGift.getId());
			ydMerchantGift.setGiftType("merchant");
			List<YdMerchantGift> allList = ydMerchantGiftDao.getAll(ydMerchantGift);
			if (CollectionUtils.isEmpty(allList)) {
				// 等于空是第一次购买
				ydMerchantGift = new YdMerchantGift();
				YdMerchantGiftResult merchantGiftResult = new YdMerchantGiftResult();
				merchantGiftResult.setCreateTime(nowDate);
				merchantGiftResult.setGiftType("merchant");
				merchantGiftResult.setMerchantId(merchantId);
				merchantGiftResult.setSupplierId(null);
				merchantGiftResult.setGiftId(ydMerchantGiftCart.getGiftId());
				merchantGiftResult.setTitle(ydGift.getTitle());
				merchantGiftResult.setSubTitle(ydGift.getSubTitle());
				merchantGiftResult.setSalePrice(ydGift.getSalePrice());
				merchantGiftResult.setMarketPrice(ydGift.getMarketPrice());
				merchantGiftResult.setImageUrl(ydGift.getImageUrl());
				merchantGiftResult.setGiftDesc(ydGift.getGiftDesc());
				merchantGiftResult.setIsEnable("N");
				ydMerchantGiftService.insertYdMerchantGift(merchantGiftResult);
			} else {
				// 不等于空是累加购买，现在没有库存概念
				ydMerchantGift = allList.get(0);
				ydMerchantGift.setUpdateTime(new Date());
				ydMerchantGift.setGiftType("merchant");
				ydMerchantGift.setMerchantId(merchantId);
				ydMerchantGift.setSupplierId(null);
				ydMerchantGift.setGiftId(ydMerchantGiftCart.getGiftId());
				ydMerchantGift.setTitle(ydGift.getTitle());
				ydMerchantGift.setSubTitle(ydGift.getSubTitle());
				ydMerchantGift.setSalePrice(ydGift.getSalePrice());
				ydMerchantGift.setMarketPrice(ydGift.getMarketPrice());
				ydMerchantGift.setImageUrl(ydGift.getImageUrl());
				ydMerchantGift.setGiftDesc(ydGift.getGiftDesc());
				this.ydMerchantGiftDao.updateYdMerchantGift(ydMerchantGift);
			}
		});

	}

}

