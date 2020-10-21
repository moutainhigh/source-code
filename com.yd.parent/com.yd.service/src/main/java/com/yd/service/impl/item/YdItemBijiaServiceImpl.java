package com.yd.service.impl.item;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.req.YdBijiaReq;
import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.service.item.YdItemBijiaService;
import com.yd.core.utils.*;
import com.yd.service.bean.item.YdItem;
import com.yd.service.bean.item.YdItemSku;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.crawer.bean.CrawerSiteSku;
import com.yd.service.crawer.bean.YdBijia;
import com.yd.service.crawer.dao.CrawerSiteSkuDao;
import com.yd.service.crawer.dao.YdBijiaDao;
import com.yd.service.crawer.util.MockViewHelper;
import com.yd.service.dao.item.YdItemDao;
import com.yd.service.dao.item.YdItemSkuDao;
import com.yd.service.dao.item.YdMerchantItemSkuDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Title:比价配置Service接口
 * @Description:
 * @Author:Wuyc
 * @Since:2020-5-26 9:29:34
 * @Version:1.1.0
 */
@Service(dynamic = true)
public class YdItemBijiaServiceImpl implements YdItemBijiaService {

    @Resource
    private YdBijiaDao ydBijiaDao;

    @Resource
    private YdItemDao ydItemDao;

    @Resource
    private YdItemSkuDao ydItemSkuDao;

    @Resource
    private YdMerchantItemSkuDao ydMerchantItemSkuDao;

    @Resource
    private CrawerSiteSkuDao crawerSiteSkuDao;

    @Resource
    private CrawerService crawerService;

    @Override
    public List<CrawerSiteSkuResult> getBijiaBySkuId(Integer skuId) throws BusinessException {
        ValidateBusinessUtils.assertIdNotNull(skuId, "err_skuId_empty", "sku为空");
        YdItemSku ydItemSku = ydItemSkuDao.getYdItemSkuById(skuId);
        ValidateBusinessUtils.assertNonNull(ydItemSku, "err_no_sku", skuId + "对应的sku不存在");

        YdItem ydItem = ydItemDao.getYdItemById(ydItemSku.getItemId());
        ValidateBusinessUtils.assertNonNull(ydItem, "err_no_item", skuId + "对应的item不存在");

        List<CrawerSiteSkuResult> resultList = new ArrayList<>();
        YdBijia ydBijia = ydBijiaDao.findYdBijiaBySkuId(skuId);
        if (ydBijia == null) {
            resultList.add(getSiteSku(null, skuId, "京东"));
            resultList.add(getSiteSku(null, skuId, "苏宁"));
            resultList.add(getSiteSku(null, skuId, "天猫"));
            resultList.add(getSiteSku(null, skuId, "国美"));
        } else {
            resultList.add(getSiteSku(ydBijia.getJdItemId(), skuId, "京东"));
            resultList.add(getSiteSku(ydBijia.getSuningItemId(), skuId, "苏宁"));
            resultList.add(getSiteSku(ydBijia.getTmallItemId(), skuId, "天猫"));
            resultList.add(getSiteSku(ydBijia.getGuomeiItemId(), skuId, "国美"));
        }
        return resultList;
    }

    @Override
    public void updateItemCompareConfig(Integer skuId, String siteName, String link) throws BusinessException {
        ValidateBusinessUtils.assertIdNotNull(skuId, "err_skuId_empty", "sku为空");
        ValidateBusinessUtils.assertStringNotBlank(link, "err_link_empty", "链接为空");
        ValidateBusinessUtils.assertStringNotBlank(siteName, "err_siteName_empty", "渠道为空");

        YdItemSku ydItemSku = ydItemSkuDao.getYdItemSkuById(skuId);
        ValidateBusinessUtils.assertNonNull(ydItemSku, "err_no_sku", skuId + "对应的sku不存在");

        YdItem ydItem = ydItemDao.getYdItemById(ydItemSku.getItemId());
        ValidateBusinessUtils.assertNonNull(ydItem, "err_no_item", skuId + "对应的item不存在");

        CrawerSiteSku crawerSiteSku = insertSiteSku(skuId, siteName, link);
        YdBijia ydBijia = ydBijiaDao.findYdBijiaBySkuId(skuId);
        if (ydBijia == null) {
            ydBijia = new YdBijia();
            ydBijia.setCreateTime(new Date());
            ydBijia.setUpdateTime(new Date());
            ydBijia.setSkuId(skuId);
            ydBijia.setItemId(ydItem.getId());
            ydBijiaDao.insert(ydBijia);
        }
        Integer oldSiteSkuId = setSiteSkuId(crawerSiteSku.getId(), siteName, ydBijia);

        // 修改比价关系表
        ydBijiaDao.updateBySkuId(ydBijia);

        // 删除旧的配置链接地址
        if (oldSiteSkuId != null && oldSiteSkuId > 0) {
            crawerSiteSkuDao.deleteCrawerSiteSkuById(oldSiteSkuId);
        }

        try {
            CrawerSiteSkuResult crawerSiteSkuResult = new CrawerSiteSkuResult();
            BeanUtilExt.copyProperties(crawerSiteSkuResult, crawerSiteSku);
            setPrice(skuId, crawerSiteSkuResult);
        } catch (Exception e) {

        }
    }

    @Override
    public void synItemNewPrice() {
        List<YdBijia> bijiaList = ydBijiaDao.findYdBijiasByPage(new YdBijiaReq(), 0, Integer.MAX_VALUE);
        if (CollectionUtils.isEmpty(bijiaList)) return;
        for (YdBijia ydBijia : bijiaList) {
            try {
                if (ydBijia.getTmallItemId() != null && ydBijia.getTmallItemId() > 0) {
                    CrawerSiteSkuResult crawerSiteSkuResult = getSiteSku(ydBijia.getId(), ydBijia.getItemId(), "天猫");
                    if (crawerSiteSkuResult.getId() == null || crawerSiteSkuResult.getId() <= 0) continue;
                    setPrice(ydBijia.getSkuId(), crawerSiteSkuResult);
                }

                if (ydBijia.getJdItemId() != null && ydBijia.getJdItemId() > 0) {
                    CrawerSiteSkuResult crawerSiteSkuResult = getSiteSku(ydBijia.getId(), ydBijia.getItemId(), "京东");
                    if (crawerSiteSkuResult.getId() == null || crawerSiteSkuResult.getId() <= 0) continue;
                    setPrice(ydBijia.getSkuId(), crawerSiteSkuResult);
                }

                if (ydBijia.getSuningItemId() != null && ydBijia.getSuningItemId() > 0) {
                    CrawerSiteSkuResult crawerSiteSkuResult = getSiteSku(ydBijia.getId(), ydBijia.getItemId(), "苏宁");
                    if (crawerSiteSkuResult.getId() == null || crawerSiteSkuResult.getId() <= 0) continue;
                    setPrice(ydBijia.getSkuId(), crawerSiteSkuResult);
                }

                if (ydBijia.getGuomeiItemId() != null && ydBijia.getGuomeiItemId() > 0) {
                    CrawerSiteSkuResult crawerSiteSkuResult = getSiteSku(ydBijia.getId(), ydBijia.getItemId(), "国美");
                    if (crawerSiteSkuResult.getId() == null || crawerSiteSkuResult.getId() <= 0) continue;
                    setPrice(ydBijia.getSkuId(), crawerSiteSkuResult);
                }
            } catch (Exception e) {

            }
        }
    }

    private void setPrice(Integer skuId, CrawerSiteSkuResult crawerSiteSku) {
        if ("天猫".equalsIgnoreCase(crawerSiteSku.getSiteName())) {
            Double tmPrice = tmallSku(crawerSiteSku);
            if (tmPrice == null || tmPrice < 0) {
                setDefaultPrice(skuId, crawerSiteSku);
            } else {
                crawerSiteSku.setPrice(tmPrice);
                crawerService.updateSkuByLink(crawerSiteSku);
            }
        } else if ("京东".equalsIgnoreCase(crawerSiteSku.getSiteName())) {
            setDefaultPrice(skuId, crawerSiteSku);
        } else if ("苏宁".equalsIgnoreCase(crawerSiteSku.getSiteName())) {
            String snPrice = getSuningPrice(crawerSiteSku.getLink());
            if (StringUtils.isEmpty(snPrice)) {
                setDefaultPrice(skuId, crawerSiteSku);
            } else {
                crawerSiteSku.setPrice(Double.parseDouble(snPrice));
                System.out.println("====crawerSiteSku=" + JSON.toJSONString(crawerSiteSku));
                crawerService.updateSkuByLink(crawerSiteSku);
            }
        } else if ("国美".equalsIgnoreCase(crawerSiteSku.getSiteName())) {
            setDefaultPrice(skuId, crawerSiteSku);
        }
    }

    private void setDefaultPrice(Integer skuId, CrawerSiteSkuResult crawerSiteSku) {
        List<Double> addPriceList = new ArrayList<>();
        addPriceList.add(50.0);
        addPriceList.add(100.0);
        addPriceList.add(150.0);
        addPriceList.add(200.0);
        addPriceList.add(250.0);
        int random = new Random().nextInt(4) + 1;
        YdItemSku ydItemSku = ydItemSkuDao.getYdItemSkuById(skuId);
        Double newPrice = MathUtils.add(ydItemSku.getSalePrice(), addPriceList.get(random), 2);
        crawerSiteSku.setPrice(newPrice);
        System.out.println("====crawerSiteSku=" + JSON.toJSONString(crawerSiteSku));
        crawerService.updateSkuByLink(crawerSiteSku);
    }


    // ---------------------------------------------- private method ---------------------------------------------

    private CrawerSiteSkuResult getSiteSku(Integer id, Integer skuId, String siteName) {
        if (id == null) {
            CrawerSiteSkuResult crawerSiteSkuResult = new CrawerSiteSkuResult();
            crawerSiteSkuResult.setSkuId(skuId);
            crawerSiteSkuResult.setSiteName(siteName);
            return crawerSiteSkuResult;
        }
        CrawerSiteSku crawerSiteSku = crawerSiteSkuDao.findCrawerSiteSkuById(id);
        if (crawerSiteSku != null) {
            CrawerSiteSkuResult crawerSiteSkuResult = new CrawerSiteSkuResult();
            BeanUtilExt.copyProperties(crawerSiteSkuResult, crawerSiteSku);
            crawerSiteSkuResult.setSkuId(skuId);
            return crawerSiteSkuResult;
        }
        CrawerSiteSkuResult crawerSiteSkuResult = new CrawerSiteSkuResult();
        crawerSiteSkuResult.setSkuId(skuId);
        crawerSiteSkuResult.setSiteName(siteName);
        return crawerSiteSkuResult;
    }

    /**
     * 保存抓取规格信息
     *
     * @param skuId
     * @param siteName
     * @param link
     * @return
     */
    private CrawerSiteSku insertSiteSku(Integer skuId, String siteName, String link) throws BusinessException {
        YdItemSku ydItemSku = ydItemSkuDao.getYdItemSkuById(skuId);
        ValidateBusinessUtils.assertNonNull(ydItemSku, "err_no_sku", skuId + "对应的sku不存在");

        YdItem ydItem = ydItemDao.getYdItemById(ydItemSku.getItemId());
        ValidateBusinessUtils.assertNonNull(ydItem, "err_no_item", skuId + "对应的item不存在");

        String title = StringUtils.isEmpty(ydItemSku.getTitle()) ? ydItem.getTitle() : ydItemSku.getTitle();
        String cover = StringUtils.isEmpty(ydItemSku.getSkuCover()) ? ydItem.getItemCover() : ydItemSku.getSkuCover();

        CrawerSiteSku crawerSiteSku = new CrawerSiteSku();
        crawerSiteSku.setCreateTime(new Date());
        crawerSiteSku.setSiteName(siteName);
        crawerSiteSku.setBrand(ydItem.getBrand());
        crawerSiteSku.setTitle(title);
        crawerSiteSku.setLink(link);
        crawerSiteSku.setCover(cover);
        crawerSiteSkuDao.insert(crawerSiteSku);
        return crawerSiteSku;
    }

    /**
     * 设置对应的配置比价链接id，返回旧的比价链接id
     *
     * @param siteSkuId
     * @param siteName
     * @param ydBijia
     * @return
     * @throws BusinessException
     */
    private Integer setSiteSkuId(Integer siteSkuId, String siteName, YdBijia ydBijia) throws BusinessException {
        Integer oldSiteSkuId = null;
        if ("国美".equals(siteName)) {
            oldSiteSkuId = ydBijia.getGuomeiItemId();
            ydBijia.setGuomeiItemId(siteSkuId);
        } else if ("京东".equals(siteName)) {
            oldSiteSkuId = ydBijia.getJdItemId();
            ydBijia.setJdItemId(siteSkuId);
        } else if ("天猫".equals(siteName)) {
            oldSiteSkuId = ydBijia.getTmallItemId();
            ydBijia.setTmallItemId(siteSkuId);
        } else if ("苏宁".equals(siteName)) {
            oldSiteSkuId = ydBijia.getSuningItemId();
            ydBijia.setSuningItemId(siteSkuId);
        }
        return oldSiteSkuId;
    }


    //------------------------------------------------- 去第三方抓取价钱比价 --------------------------------------

    public static String getSuningPrice(String itemUrl) {
        try {
            itemUrl = itemUrl.replaceAll("https://product.suning.com/0000000000/", "").replaceAll(".html", "");
            String priceMockUrl = "https://pas.suning.com/nspcsale_0_" + getFullShopId2(itemUrl) + "_" + getFullShopId2(itemUrl) + "_0000000000_10_010_0100100_20089_1000000_9017_10106_Z001___R1901001_0.44_0___00006J675___.html?callback=pcData&_=1575291502277";
            String body = MockViewHelper.views(priceMockUrl).getBody();
            int index1 = body.indexOf("(");
            int index2 = body.indexOf(")");
            body = body.substring(index1 + 1, index2);
            JSONObject json = JSONObject.parseObject(body);
            JSONArray arr = json.getJSONObject("data").getJSONObject("price").getJSONArray("saleInfo");
            if (arr.size() == 1) {
                JSONObject p = arr.getJSONObject(0);
                if (StringUtils.isNotEmpty(p.getString("promotionPrice"))) {
                    return p.getString("promotionPrice");
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static String getFullShopId2(String shopId) {
        String str = "";
        for (int i = 0; i < 18 - shopId.length(); i++) {
            str += "0";
        }
        return str + shopId;
    }


    //---------------------------------  获取天猫价钱 ---------------------------------------------
    public static Map<String, String> getCookies() {
        Map<String, String> cookie = new HashMap<String, String>();
        String str = "t=f0c8a36f8d24ddb20e3365790e78b018; lid=%E5%85%A8%E5%8A%9B%E4%BB%A5%E8%B5%B4%E6%88%91%E4%BB%AC%E5%BF%83%E4%B8%AD%E7%9A%84%E6%A2%A6; _tb_token_=eeee5e0e6b0e4; cookie2=19984f7a654a9e3dc7ff5e7759dd539c; cna=lE40FmWCiVACATy6H9jH1mMh; cq=ccp%3D1; pnm_cku822=098%23E1hvx9vUvbpvj9CkvvvvvjiPRs5UgjDRR2zOgjEUPmPygjnmP25Ogj3Un25pzvGCvvpvvPMMvphvC9mvphvvvvyCvhACk3Uoj4ZT%2BLpZbQfrFfmt%2B3CQo%2BexRdItn0vEYE7re4TNahLWzC0X5C107reYr2UpVj%2BO3w0AhE9aWDNBlLyzhbUf8r3l53VOh7QEKphv8vvvph%2BvvvvvvvCjnvvv9aIvvhNjvvvmjvvvBGwvvvUUvvCj1vvvvI4EvpvVvpCmpYspuphvmvvvpoGfdU3aRphvCvvvphm5vpvhvvCCBv%3D%3D; l=dBrXwpZgqnw2y9wQKOCgNuIRf4_OlIRfguPRwlX2i_5IX1L6Vu7Ok3hdkep6cjWcG6Yp4s6vWjptQUg7-yk6suWnJ9cdvdHDBef..; isg=BAoK6V6sY6FY0e9EpAmfII7HW_BsU4XLfG7VFpRD1t3oR6sBfY-7ZAP9U_M-twbt";
        String[] arr = str.split(";");
        for (String items : arr) {
            try {
                String name = items.trim().split("=")[0];
                String value = items.trim().split("=")[1];
                cookie.put(name.trim(), value.trim());
            } catch (Exception e) {

            }
        }
        return cookie;
    }

    public Double tmallSku(CrawerSiteSkuResult crawerSiteSku) {
        try {
            String itemSkuUrl = crawerSiteSku.getLink();
            System.out.println("====itemSkuUrl" + itemSkuUrl);
            Element ii = MockViewHelper.views(itemSkuUrl).getDocument().getElementsByAttributeValue("class", "scroller preview-scroller").first().getElementsByTag("img").get(0);
            String cover = ii.attr("data-src");
            if (cover.startsWith("//")) {
                cover = "https:" + cover;
            }
            Document doc = MockViewHelper.views(itemSkuUrl, itemSkuUrl, getCookies()).getDocument();
            Elements scrips = doc.getElementsByTag("script");
            System.out.println("=======size:" + scrips.size());
            if (scrips.size() > 0) {
                for (int i = 0; i < scrips.size(); i++) {
                    System.out.println("=============================" + i + "=======================");
                    String html = scrips.get(i).html().trim();
                    if (html.startsWith("var _DATA_Detail =")) {
                        int index1 = html.indexOf("{");
                        int index2 = html.length() - 1;
                        html = html.substring(index1, index2);
                        System.out.println(html);
                        JSONObject json = JSONObject.parseObject(html);
                        String title = json.getJSONObject("item").getString("title");
                        JSONArray propsArr = json.getJSONObject("skuBase").getJSONArray("props");
                        JSONObject skuCore = json.getJSONObject("mock").getJSONObject("skuCore").getJSONObject("sku2info");

                        Map<String, Map<String, String>> propMap = new HashMap<String, Map<String, String>>();
                        if (propsArr != null && propsArr.size() > 0) {
                            for (int k = 0; k < propsArr.size(); k++) {
                                String pid = propsArr.getJSONObject(k).getString("pid");
                                JSONArray valueArr = propsArr.getJSONObject(k).getJSONArray("values");
                                Map<String, String> map = new HashMap<String, String>();
                                if (valueArr != null && valueArr.size() > 0) {
                                    for (int p = 0; p < valueArr.size(); p++) {
                                        String vid = valueArr.getJSONObject(p).getString("vid");
                                        String name = valueArr.getJSONObject(p).getString("name");
                                        map.put(vid, name);
                                    }
                                }
                                propMap.put(pid, map);
                            }
                        }
                        JSONArray skuArr = json.getJSONObject("skuBase").getJSONArray("skus");
                        if (skuArr != null && skuArr.size() > 0) {
                            for (int j = 0; j < skuArr.size(); j++) {
                                String skuId = skuArr.getJSONObject(j).getString("skuId");
                                String propPath = skuArr.getJSONObject(j).getString("propPath");
                                StringBuffer sb = new StringBuffer();
                                if (StringUtils.isNotEmpty(propPath)) {
                                    sb.append("【");
                                    for (String prop : propPath.split(";")) {
                                        String propKey = prop.split(":")[0];
                                        String propValue = prop.split(":")[1];
                                        sb.append(propMap.get(propKey).get(propValue) + " ");
                                    }
                                    sb.append("】");
                                }
                                String price = skuCore.getJSONObject(skuId).getJSONObject("price").getString("priceMoney");
                                Double tmPrice = new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue();
                                crawerSiteSku.setPrice(tmPrice);
                                crawerService.updateSkuByLink(crawerSiteSku);
                            }
                        } else if (skuArr == null) {
                            String price = skuCore.getJSONObject("0").getJSONObject("price").getString("priceMoney");
                            Double tmPrice = new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue();
                            crawerSiteSku.setPrice(tmPrice);
                            crawerService.updateSkuByLink(crawerSiteSku);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return 0.00;
    }

}
