package com.yd.service.impl.index;

import com.alibaba.fastjson.JSON;
import com.yd.api.result.coupon.YdUserCouponResult;
import com.yd.api.result.index.*;
import com.yd.api.result.merchant.YdMerchantAccountResult;
import com.yd.api.result.merchant.YdMerchantGiftAccountResult;
import com.yd.api.result.merchant.YdMerchantResult;
import com.yd.api.result.order.YdGiftOrderDetailResult;
import com.yd.api.result.order.YdUserOrderDetailResult;
import com.yd.api.service.index.AdminMerchantIndexService;
import com.yd.api.service.merchant.YdMerchantAccountService;
import com.yd.api.service.merchant.YdMerchantGiftAccountService;
import com.yd.api.service.merchant.YdMerchantService;
import com.yd.api.service.order.YdUserOrderDetailService;
import com.yd.core.enums.YdUserOrderStatusEnum;
import com.yd.core.utils.*;
import com.yd.service.bean.gift.YdMerchantGift;
import com.yd.service.bean.item.YdItem;
import com.yd.service.bean.item.YdMerchantItem;
import com.yd.service.bean.message.YdMerchantMessage;
import com.yd.service.bean.order.YdUserOrder;
import com.yd.service.bean.order.YdUserOrderDetail;
import com.yd.service.bean.visitor.YdVisitorLog;
import com.yd.service.dao.gift.YdMerchantGiftDao;
import com.yd.service.dao.item.YdItemDao;
import com.yd.service.dao.item.YdMerchantItemDao;
import com.yd.service.dao.message.YdMerchantMessageDao;
import com.yd.service.dao.order.YdUserOrderDao;
import com.yd.service.dao.order.YdUserOrderDetailDao;
import com.yd.service.dao.visitor.YdVisitorLogDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页数据统计实现
 * @author wuyc
 * created 2019/10/23 13:47
 **/
@Service(dynamic = true)
public class AdminMerchantIndexServiceImpl implements AdminMerchantIndexService {

    private static final Logger logger = LoggerFactory.getLogger(AdminMerchantIndexServiceImpl.class);

    @Resource
    private YdItemDao ydItemDao;

    @Resource
    private YdUserOrderDao ydShopOrderDao;

    @Resource
    private YdVisitorLogDao ydVisitorLogDao;

    @Resource
    private YdMerchantGiftDao ydMerchantGiftDao;

    @Resource
    private YdMerchantItemDao ydMerchantItemDao;

    @Resource
    private YdUserOrderDetailDao ydUserOrderDetailDao;

    @Resource
    private YdMerchantMessageDao ydMerchantMessageDao;

    @Resource
    private YdMerchantService ydMerchantService;

    @Resource
    private YdUserOrderDetailService ydUserOrderDetailService;

    @Resource
    private YdMerchantAccountService ydMerchantAccountService;

    @Resource
    private YdMerchantGiftAccountService ydMerchantGiftAccountService;


    /**
     * 后台首页 我的账户
     * @param merchantId merchantId
     * @return IndexMyAccountResult
     */
    @Override
    public IndexMyAccountResult getMyAccount(Integer merchantId) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        merchantId = storeInfo.getId();

        IndexMyAccountResult myAccountResult = new IndexMyAccountResult();

        // 查询设置账户余额
        YdMerchantAccountResult ydMerchantAccountResult = ydMerchantAccountService.getYdMerchantAccountByMerchantId(merchantId);

        // 查询设置交易额 (已经完成的订单)
        List<YdUserOrder> orderList = ydShopOrderDao.findOrderListByPage(merchantId, null, null,
                YdUserOrderStatusEnum.SUCCESS.getCode(), null, null, null,
                null, 0, Integer.MAX_VALUE);
        Double onlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("PS"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        Double offlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("ZT"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        Double totalPrice = MathUtils.add(onlinePrice, offlinePrice, 2);
        myAccountResult.setTransPrice(totalPrice);
        myAccountResult.setBalance(ydMerchantAccountResult.getBalance());
        return myAccountResult;
    }

    /**
     * 后台首页 待处理数据
     * @param merchantId 商户id
     * @return IndexWaitHandleResult
     */
    @Override
    public IndexWaitHandleResult getWaitHandleData(Integer merchantId) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        merchantId = storeInfo.getId();

        IndexWaitHandleResult waitHandleResult = new IndexWaitHandleResult();

        // 查询设置待处理订单数
        int waitHandelCount = ydShopOrderDao.findOrderListCount(merchantId, null, null,
                YdUserOrderStatusEnum.WAIT_HANDLE.getCode(), null, null, null, null);
        waitHandleResult.setOrderCount(waitHandelCount);

        // 查询设置待礼品数量
        YdMerchantGift ydMerchantGift = new YdMerchantGift();
        ydMerchantGift.setIsFlag("N");
        ydMerchantGift.setMerchantId(merchantId);
        ydMerchantGift.setGiftStatus("no_shelves");
        Integer giftCount = this.ydMerchantGiftDao.getYdMerchantGiftCount(ydMerchantGift);
        waitHandleResult.setGiftCount(giftCount);

        // 查询设置待上架商品数量
        YdMerchantItem ydMerchantItem = new YdMerchantItem();
        ydMerchantItem.setMerchantId(merchantId);
        ydMerchantItem.setIsEnable("N");
        Integer itemCount = ydMerchantItemDao.getMerchantItemCount(ydMerchantItem);
        waitHandleResult.setItemCount(itemCount);
        return waitHandleResult;
    }

    /**
     * 后台首页 数据统计
     * @param merchantId merchantId
     * @param dateType	day | week | month
     * @return
     */
    @Override
    public IndexDataStatisticsResult getIndexDataStatistics(Integer merchantId, String dateType) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        merchantId = storeInfo.getId();

        // 默认当天
        Date nowDate = new Date();
        String startTime = DateUtils.getDayStartDateStr(nowDate);
        String endTime = DateUtils.getDetailDateTime(nowDate);
        if (("week").equalsIgnoreCase(dateType)) {
            // 近一周
            startTime = DateUtils.getDetailDateTime(DateUtils.addDays(nowDate, -7));
        } else if (("month").equalsIgnoreCase(dateType)) {
            // 近一月
            startTime = DateUtils.getDetailDateTime(DateUtils.addDays(nowDate, -30));
        }

        logger.info("merchantId = {}, dateType = {}, startTime = {}, endTime = {}" , merchantId , dateType , startTime , endTime);

        IndexDataStatisticsResult dataStatisticsResult = new IndexDataStatisticsResult();

        // 商户访问量
        YdVisitorLog ydVisitorLog = new YdVisitorLog();
        ydVisitorLog.setMerchantId(merchantId);
        ydVisitorLog.setStartTime(startTime);
        ydVisitorLog.setEndTime(endTime);
        int userVisitorCount = ydVisitorLogDao.getUserVisitorCount(ydVisitorLog);
        dataStatisticsResult.setVisitCount(userVisitorCount);

        List<YdUserOrder> orderList = ydShopOrderDao.findOrderListByPage(merchantId, null, null,
                YdUserOrderStatusEnum.SUCCESS.getCode(), null, null, startTime, endTime, 0, Integer.MAX_VALUE);

        Double onlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("PS"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        Double offlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("ZT"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        Double totalPrice = MathUtils.add(onlinePrice, offlinePrice, 2);

        // 设置线上收入
        dataStatisticsResult.setToAccountIncome(MathUtils.add(onlinePrice, 0.0, 2));
        // 设置总收入(线上+线上)
        dataStatisticsResult.setMerchantIncome(totalPrice);

        // 查询已完成订单数
        int completeOrderCount =  ydShopOrderDao.findOrderListCount(merchantId, null, null,
                YdUserOrderStatusEnum.SUCCESS.getCode(), null, null, startTime, endTime);
        dataStatisticsResult.setCompleteOrderCount(completeOrderCount);

        // 查询拼单完成订单的用户数
        int completeOrderUserCount = ydShopOrderDao.getCompleteOrderUserCount(null, merchantId,
                YdUserOrderStatusEnum.SUCCESS.getCode(), startTime, endTime);
        dataStatisticsResult.setUserCompleteOrderCount(completeOrderUserCount);
        return dataStatisticsResult;
    }

    /**
     * 后台首页 商户热销数据
     * @param merchantId merchantId
     * @param platform admin | merchant
     * @param dateType week | month | year
     * @return List<IndexHotRankResult>
     */
    @Override
    public List<IndexHotRankResult> getHotRankData(Integer merchantId, String platform, String dateType) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        if ("admin".equalsIgnoreCase(platform)) {
            merchantId = null;
        } else {
            merchantId = storeInfo.getId();
        }

        // 默认查近七天, month查近30天, year查近365
        Date nowDate = new Date();
        String startTime = DateUtils.getDayStartDateStr(DateUtils.addDays(nowDate, -7));
        String endTime = DateUtils.getDetailDateTime(nowDate);
        if (("month").equalsIgnoreCase(dateType)) {
            startTime = DateUtils.getDetailDateTime(DateUtils.addDays(nowDate, -30));
        } else if (("year").equalsIgnoreCase(dateType)) {
            startTime = DateUtils.getDetailDateTime(DateUtils.addDays(nowDate, -365));
        }

        List<IndexHotRankResult> resultList = new ArrayList<>();

        // 查询订单成交量前五的商品
        List<YdUserOrderDetail> maxSalesItemList = ydUserOrderDetailDao.findMerchantMaxSalesItemData(merchantId, startTime, endTime);
        logger.info("====销量前五的maxSalesItemList=" + JSON.toJSONString(maxSalesItemList));

        // 根据商品id去查询时间段内已经完成的订单
        for (YdUserOrderDetail ydUserOrderDetail : maxSalesItemList) {
            IndexHotRankResult hotRankResult = new IndexHotRankResult();

            // 查询设置商品名称
            if ("admin".equalsIgnoreCase(platform)) {
                YdItem ydItem = ydItemDao.getYdItemById(ydUserOrderDetail.getItemId());
                if (ydItem == null) {
                    hotRankResult.setTitle("平台商品不存在,可能已经被删除,商品id=" + ydUserOrderDetail.getItemId());
                } else {
                    hotRankResult.setTitle(ydItem.getTitle());
                }
            } else {
                YdMerchantItem ydMerchantItem = ydMerchantItemDao.getYdShopMerchantItemById(ydUserOrderDetail.getMerchantItemId());
                if (ydMerchantItem == null) {
                    hotRankResult.setTitle("商户商品不存在,可能已经被删除,商品id=" + ydUserOrderDetail.getMerchantItemId());
                } else {
                    hotRankResult.setTitle(ydMerchantItem.getTitle());
                }
            }

            // 查询所有订单详情，设置销量，销售金额
            YdUserOrderDetail params = new YdUserOrderDetail();
            params.setStartTime(startTime);
            params.setEndTime(endTime);
            params.setOrderStatus("SUCCESS");

            if ("admin".equalsIgnoreCase(platform)) {
                params.setItemId(ydUserOrderDetail.getItemId());
                params.setSkuId(ydUserOrderDetail.getSkuId());
                logger.info("====平台销量前五的itemId=" + ydUserOrderDetail.getItemId() + " ,=skuId=" + ydUserOrderDetail.getSkuId());
            } else {
                params.setMerchantId(merchantId);
                params.setMerchantItemId(ydUserOrderDetail.getMerchantItemId());
                params.setMerchantSkuId(ydUserOrderDetail.getMerchantSkuId());
                logger.info("====商户销量前五的merchantItemId=" + ydUserOrderDetail.getMerchantItemId() + " ,=merchantSkuId=" + ydUserOrderDetail.getMerchantSkuId());
            }
            List<YdUserOrderDetail> orderDetailLists = this.ydUserOrderDetailDao.findOrderDetailListByPage(params, 0, Integer.MAX_VALUE);

            List<YdUserOrderDetailResult> orderDetailList = DTOUtils.convertList(orderDetailLists, YdUserOrderDetailResult.class);
            if (CollectionUtils.isNotEmpty(orderDetailList)) {
                double salePrice = orderDetailList.stream().mapToDouble(data -> data.getNum() * data.getMerchantItemPrice()).sum();
                hotRankResult.setSalePrice(MathUtils.add(salePrice, 0.0, 2));
                hotRankResult.setSaleCount(ydUserOrderDetail.getNum());
            } else {
                hotRankResult.setSaleCount(0);
                hotRankResult.setSalePrice(0.00);
            }

            // 查询设置商品浏览量
            YdVisitorLog ydVisitorLog = new YdVisitorLog();
            if ("admin".equalsIgnoreCase(platform)) {
                ydVisitorLog.setItemId(ydUserOrderDetail.getItemId());
                int itemVisitorCount = ydVisitorLogDao.getItemVisitorCount(ydVisitorLog);
                hotRankResult.setBrowseCount(itemVisitorCount);
            } else {
                ydVisitorLog.setMerchantItemId(merchantId);
                ydVisitorLog.setMerchantItemId(ydUserOrderDetail.getMerchantItemId());
                int itemVisitorCount = ydVisitorLogDao.getItemVisitorCount(ydVisitorLog);
                hotRankResult.setBrowseCount(itemVisitorCount);
            }
            resultList.add(hotRankResult);
        }
        return resultList;
    }

    /**
     * index首页数据 (APP)
     * @param merchantId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public AppIndexResult getAppIndexData(Integer merchantId, String startTime, String endTime) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        Integer storeId = storeInfo.getId();

        AppIndexResult appIndexResult = new AppIndexResult();
        // 查询设置账户余额
        YdMerchantAccountResult ydMerchantAccountResult = ydMerchantAccountService.getYdMerchantAccountByMerchantId(storeId);
        appIndexResult.setBalance(ydMerchantAccountResult.getBalance());

        // 查询设置礼品账户余额
        YdMerchantGiftAccountResult ydMerchantGiftAccountResult = ydMerchantGiftAccountService.getYdMerchantGiftAccountByMerchantId(storeId);
        appIndexResult.setGiftBalance(ydMerchantGiftAccountResult.getBalance());

        // 查询设置未读消息数量
        YdMerchantMessage ydMerchantMessage = new YdMerchantMessage();
        ydMerchantMessage.setMerchantId(merchantId);
        ydMerchantMessage.setIsRead("N");
        int messageCount = ydMerchantMessageDao.getYdMerchantMessageCount(ydMerchantMessage);
        appIndexResult.setNoReadMessage(messageCount);

        // 查询设置交易数量(已完成订单数)
        int orderNum =  ydShopOrderDao.findOrderListCount(storeId, null, null,
                YdUserOrderStatusEnum.SUCCESS.getCode(), null, null, startTime, endTime);
        appIndexResult.setOrderNum(orderNum);

        // 查询完整订单的数据，设置交易金额等数据
        List<YdUserOrder> orderList = ydShopOrderDao.findOrderListByPage(storeId, null, null,
                YdUserOrderStatusEnum.SUCCESS.getCode(), null, null, startTime, endTime, 0, Integer.MAX_VALUE);

        Double oldMobilePrice = orderList.stream().filter(data -> data.getOldMachineReducePrice() != null)
                .mapToDouble(YdUserOrder :: getOldMachineReducePrice).sum();
        appIndexResult.setOldMobilePrice(MathUtils.add(oldMobilePrice, 0.0, 2));

        Double integralPrice = orderList.stream().filter(data -> data.getIntegralReducePrice() != null)
                .mapToDouble(YdUserOrder :: getIntegralReducePrice).sum();
        appIndexResult.setIntegralPrice(MathUtils.add(integralPrice, 0.0, 2));

        Double offlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("ZT"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        appIndexResult.setOfflinePrice(MathUtils.add(offlinePrice, 0.0, 2));

        Double onlinePrice = orderList.stream().filter(data -> data.getReceiveWay().equalsIgnoreCase("PS"))
                .mapToDouble(YdUserOrder :: getPayPrice).sum();
        appIndexResult.setOnlinePrice(MathUtils.add(onlinePrice, 0.0, 2));

        appIndexResult.setOrderPrice(MathUtils.add(offlinePrice, onlinePrice, 2));
        return appIndexResult;
    }

    /**
     * app首页营收统计
     * @param merchantId    merchantId
     * @param startTime     yyyy-MM-dd
     * @param endTime       yyyy-MM-dd
     * @param type          online(线上), offline(线下), all(全部), 不传默认全部
     * @return
     */
    @Override
    public Map<String, Object> getAppSaleDetail(Integer merchantId, String startTime, String endTime, String type) {
        YdMerchantResult storeInfo = ydMerchantService.getStoreInfo(merchantId);
        Integer storeId = storeInfo.getId();
        // todo 诺基亚，索尼手机删掉
        Map<String, Object> resultData = new HashMap<>();
        List<Map<String, Object>> marketData = getAppMarketData(storeId, startTime, endTime, type);
        List<Map<String, Object>> itemData = getAppItemData(storeId, startTime, endTime, type);
        resultData.put("marketData", marketData);
        resultData.put("itemData", itemData);
        return resultData;
    }

    private List<Map<String, Object>> getAppItemData(Integer merchantId, String startTime, String endTime, String type) {
        List<Map<String, Object>> itemData = new ArrayList<>();

        // 查询时间段内的商品营收,去重，计算销售数量，销售金额, 按照销量排序， 销量高的在上面
        List<YdUserOrderDetail> orderDetailList = ydUserOrderDetailDao.getAppSaleData(merchantId, startTime, endTime, type);

        // 根据商品分组
        Map<Integer, List<YdUserOrderDetail>> itemList = orderDetailList.stream().collect(
                Collectors.groupingBy(YdUserOrderDetail :: getMerchantItemId));

        // 计算商品销量，销售金额
        List<YdUserOrderDetail> itemResultList = new ArrayList<>();
        for(Map.Entry<Integer, List<YdUserOrderDetail>> entry : itemList.entrySet()){
            List<YdUserOrderDetail> dataList = entry.getValue();
            YdUserOrderDetail ydUserOrderDetail = new YdUserOrderDetail();
            ydUserOrderDetail.setMerchantItemTitle(dataList.get(0).getMerchantItemTitle());
            ydUserOrderDetail.setNum(dataList.stream().mapToInt(YdUserOrderDetail :: getNum).sum());
            ydUserOrderDetail.setMerchantItemPrice(dataList.stream().
                    mapToDouble(detail -> detail.getNum() * detail.getMerchantItemPrice()).sum());
            itemResultList.add(ydUserOrderDetail);
        }

        // 按照销售金额倒序
        List<YdUserOrderDetail> itemDataList = itemResultList.stream().sorted(
                Comparator.comparing(YdUserOrderDetail::getMerchantItemPrice).reversed())
                .collect(Collectors.toList());

        // 遍历，存入map中
        itemDataList.forEach(ydUserOrderDetail -> {
            Map<String, Object> result = new HashMap<>();
            result.put("title", ydUserOrderDetail.getMerchantItemTitle());
            result.put("num", ydUserOrderDetail.getNum());
            result.put("salePrice", MathUtils.add(ydUserOrderDetail.getMerchantItemPrice(), 0.0, 2));
            itemData.add(result);
        });
        return itemData;
    }

    /**
     * @param merchantId
     * @param startTime     yyyy-MM-dd
     * @param endTime       yyyy-MM-dd
     * @param type
     * @return
     */
    private List<Map<String, Object>> getAppMarketData(Integer merchantId, String startTime, String endTime, String type) {
        List<Map<String, Object>> marketData = new ArrayList<>();

        // 获取所有时间段内所有日期
        List<String> dateList = DateUtils.getDateStrBetweenList(startTime, endTime, DateUtils.DEFAULT_DATE_FORMAT);

        // 查询时间段内营收统计
        List<YdUserOrder> orderList = ydShopOrderDao.getAppMarketData(merchantId, startTime, endTime + " 59:59:59", type);
        if (CollectionUtils.isEmpty(orderList)) {
            dateList.forEach(dataStr -> {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("dateStr", dataStr);
                hashMap.put("salePrice", 0.00);
                marketData.add(hashMap);
            });
        } else {
            dateList.forEach(dataStr -> {
                Map<String, Object> hashMap = new HashMap<>();
                double salePrice = orderList.stream().mapToDouble(ydUserOrder -> {
                    if (dataStr.equalsIgnoreCase(DateUtils.getDateTime(ydUserOrder.getUpdateTime(), DateUtils.DEFAULT_DATE_FORMAT))) {
                        return ydUserOrder.getPayPrice();
                    } else {
                        return 0.00;
                    }
                }).sum();
                hashMap.put("dateStr", dataStr);
                hashMap.put("salePrice", MathUtils.add(salePrice, 0.0, 2));
                marketData.add(hashMap);
            });
        }
        return marketData;
    }

}
