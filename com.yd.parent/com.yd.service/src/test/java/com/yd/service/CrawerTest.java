package com.yd.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.BijiaProcess;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.crawer.result.CrawerTmallItemResult;
import com.yd.service.crawer.util.MockViewHelper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class CrawerTest {
    @Resource
    private List<BijiaProcess> bijiaProcess;
    @Resource
    private CrawerService crawerService;


//    public static void main(String[] args) throws IOException {
//        List<String> list = FileUtils.readLines(new File("d:\\国美.txt"));
//        for (String str : list) {
//            str = str.replaceAll("	", "");
//            //System.out.println(str);
//            String brand = str.split(",")[0].trim();
//            String link = str.split(",")[1].trim();
//
//            System.out.println("insert into crawer_site_brand(site_name,brand,index_url) values('天猫','" + brand + "','" + link + "');");
//        }
//
//
//    }

    @Test
    public void initItemContent() throws IOException {
        List<CrawerSiteBrandResult> list = crawerService.getAllCrawerSiteBrand();
        for (CrawerSiteBrandResult brand : list) {
            for (BijiaProcess process : bijiaProcess) {
                if (process.isSite(brand.getSiteName())) {
                    process.getExecuteSku(brand);
                }
            }

        }
    }

    @Test
    public void index() throws IOException {
        List<CrawerSiteSkuResult> list = crawerService.getAllSkuList();
        Directory directory = FSDirectory.open(new File("d:\\indexDir"));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        indexWriter.deleteAll();
        System.out.println("=============一共 :" + list.size());
        int count = 0;
        for (CrawerSiteSkuResult sku : list) {
            System.out.println(count++ + "=============处理sku:" + sku.getId());
            org.apache.lucene.document.Document document = new org.apache.lucene.document.Document();
            document.add(new StringField("id", "" + sku.getId(), Field.Store.YES));
            document.add(new StringField("siteName", sku.getSiteName(), Field.Store.YES));
            document.add(new StringField("brand", sku.getBrand(), Field.Store.YES));
            document.add(new TextField("title", sku.getTitle(), Field.Store.YES));
            document.add(new StringField("price", sku.getPrice() + "", Field.Store.YES));
            indexWriter.addDocument(document);
        }
        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void search1() {
        String siteName = "国美";
        String brand = "﻿华为";
        String title = "华为P30 Pro 亮黑色 8GB+512GB（全网通） ";
        crawerService.search(siteName, "华为", "华为P30 Pro 亮黑色 8GB+512GB（全网通）");
    }


    public static void main(String[] args) {
        String price = getSuningPrice2("https://product.suning.com/0000000000/10310613862.html");
        System.out.println("====price=" + price);
//        Element i = MockViewHelper.views("https://item.m.jd.com/product/62846657404.html").getDocument().getElementsByAttributeValue("class", "pic_list").first().getElementsByTag("img").get(0);
//        String cover = i.attr("src");
//        System.out.println(i.toString());
    }


    @Test
    public void search() throws IOException, ParseException {
        String siteName = "国美";
        String brand = "﻿华为";
        String title = "华为P30 Pro 亮黑色 8GB+512GB（全网通） ";
        System.out.println("============================================================");
        System.out.println("===============siteName=======" + siteName);
        System.out.println("============brand:" + brand);
        System.out.println("============title:" + title);
        List<CrawerSiteSkuResult> result = new ArrayList<CrawerSiteSkuResult>();
        Analyzer analyzer = new IKAnalyzer();
        // 1.2.创建查询解析器对象
        Query query1 = new TermQuery(new Term("siteName", siteName));
        Query query2 = new TermQuery(new Term("brand", brand));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(query1, Occur.MUST);
        booleanQuery.add(query2, Occur.MUST);
        QueryParser qp = new QueryParser("title", analyzer);
        IndexReader reader = null;
        try {
            Query query = qp.parse(title);
            booleanQuery.add(query, Occur.MUST);

            Directory directory = FSDirectory.open(new File("d:\\luceneIndex"));
            reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs topDocs = searcher.search(booleanQuery, 30);
            System.out.println("实际查询到的结果数量: " + topDocs.totalHits);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            for (ScoreDoc scoreDoc : scoreDocs) {
                System.out.println("= = = = = = = = = = = = = = = = = = =");
                int docId = scoreDoc.doc;
                org.apache.lucene.document.Document doc = searcher.doc(docId);
                System.out.println("id:" + doc.get("id"));
                System.out.println("siteName: " + doc.get("siteName"));
                System.out.println("brand: " + doc.get("brand"));
                System.out.println("title: " + doc.get("title"));
                System.out.println("price: " + doc.get("price"));
                String docId2 = doc.get("id");
                String docSiteName = doc.get("siteName");
                String docBrand = doc.get("brand");
                String docTitle = doc.get("title");
                String docPrice = doc.get("price");
                CrawerSiteSkuResult item = new CrawerSiteSkuResult();
                item.setId(Integer.parseInt(docId2));
                item.setSiteName(docSiteName);
                item.setBrand(docBrand);
                item.setTitle(docTitle);
                if (StringUtils.isNotEmpty(docPrice)) {
                    item.setPrice(Double.parseDouble(docPrice));
                }
                result.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void initCover() {
        //pic_list
        List<CrawerSiteSkuResult> list = crawerService.getCoverNullList();
        for (CrawerSiteSkuResult sku : list) {
            String url = sku.getLink();
            System.out.println(url);
            if ("京东".equals(sku.getSiteName())) {
                try {
                    Element i = MockViewHelper.views(url).getDocument().getElementsByAttributeValue("class", "pic_list").first().getElementsByTag("img").get(0);
                    String cover = i.attr("src");
                    if (cover.startsWith("//")) {
                        cover = "https:" + cover;
                    }
                    sku.setCover(cover);
                    System.out.println("=-=======京东 cover:" + cover);
                    crawerService.updateSkuByLink(sku);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("苏宁".equals(sku.getSiteName())) {
                Element bigImg = MockViewHelper.views(url).getDocument().getElementById("bigImg").getElementsByTag("img").get(0);
                String cover = bigImg.attr("src");
                if (cover.startsWith("//")) {
                    cover = "https:" + cover;
                }
                sku.setCover(cover);
                System.out.println("苏宁：" + cover);
                crawerService.updateSkuByLink(sku);
            } else if ("天猫".equals(sku.getSiteName())) {
                try {
                    Element ii = MockViewHelper.views(url).getDocument().getElementsByAttributeValue("class", "scroller preview-scroller").first().getElementsByTag("img").get(0);
                    String cover = ii.attr("data-src");
                    if (cover.startsWith("//")) {
                        cover = "https:" + cover;
                    }
                    System.out.println("天猫 cover：" + cover);
                    sku.setCover(cover);
                    crawerService.updateSkuByLink(sku);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void initSku() throws IOException {
        List<CrawerSiteSkuResult> list = crawerService.getUndoSkuList();
        for (CrawerSiteSkuResult sku : list) {
            System.out.println("==========================================" + sku.getId());
            String url = sku.getLink();
            System.out.println("url===" + url);
            if ("苏宁".equals(sku.getSiteName())) {
                Element bigImg = MockViewHelper.views(url).getDocument().getElementById("bigImg").getElementsByTag("img").get(0);
                String cover = bigImg.attr("src");
                if (cover.startsWith("//")) {
                    cover = "https:" + cover;
                }
                sku.setCover(cover);
                crawerService.updateSkuByLink(sku);

                Elements scripts = MockViewHelper.views(url).getDocument().getElementsByTag("script");
                System.out.println("script size:" + scripts.size());
                for (int i = 0; i < scripts.size(); i++) {
                    System.out.println("====================i:" + i);
                    if (scripts.get(i).html().contains("var mySuningSideBarSign =")) {
                        String html = scripts.get(i).html();
                        html = html.split("\"itemDisplayName\"")[1];
                        int index1 = html.indexOf("\"");
                        int index2 = html.indexOf("\"", 5);
                        String title = html.substring(index1 + 1, index2);
                        sku.setTitle(title);

                        String shopId = sku.getLink().replaceAll("https://product.suning.com/0000000000/", "").replaceAll(".html", "");
                        String priceMockUrl = "https://pas.suning.com/nspcsale_0_" + getFullShopId(shopId) + "_" + getFullShopId(shopId) + "_0000000000_10_010_0100100_20089_1000000_9017_10106_Z001___R1901001_0.44_0___00006J675___.html?callback=pcData&_=1575291502277";
                        String price = getSuningPrice(shopId);
                        if (price == null) {
                            System.out.println("==========shopId:" + shopId);
                            System.out.println("=========priceMockUrl==" + priceMockUrl);
                            System.out.println("============price is null =====================");
                            price = "-1";
                        }
                        sku.setPrice(Double.parseDouble(price));
                        crawerService.updateSkuByLink(sku);
                    }
                }
            } else if ("国美".equals(sku.getSiteName())) {
                if (url.startsWith("//")) {
                    url = "https:" + url;
                }
                Element i = MockViewHelper.views("http://item.gome.com.cn/9140118203-1130559480.html").getDocument().getElementsByAttributeValue("class", "pic-big").first().getElementsByTag("img").get(0);
                String cover = i.attr("jqimg");
                if (cover.startsWith("//")) {
                    cover = "https:" + cover;
                }
                sku.setCover(cover);
                crawerService.updateSkuByLink(sku);
                Elements scripts = MockViewHelper.views(url).getDocument().getElementsByTag("script");
                String price = null;
                if (scripts.size() > 0) {
                    for (int j = 0; j < scripts.size(); j++) {
                        String html = scripts.get(j).html().trim();
                        if (html.startsWith("var prdInfo =")) {
                            price = html.split("gomePrice:\"")[1];
                            price = price.substring(0, price.indexOf("\""));
                        }
                    }
                }
                System.out.println("price:" + price);
                if (StringUtils.isNotEmpty(price)) {
                    sku.setPrice(Double.parseDouble(price));
                    crawerService.updateSkuByLink(sku);
                }
            } else if ("天猫".equals(sku.getSiteName())) {
                tmallSku(sku);
            }
        }

    }


    private String getSuningPrice(String shopId) {
        String priceMockUrl = "https://pas.suning.com/nspcsale_0_" + getFullShopId(shopId) + "_" + getFullShopId(shopId) + "_0000000000_10_010_0100100_20089_1000000_9017_10106_Z001___R1901001_0.44_0___00006J675___.html?callback=pcData&_=1575291502277";
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
        return null;
    }

    public static String getSuningPrice2(String shopId) {
        shopId = shopId.replaceAll("https://product.suning.com/0000000000/", "").replaceAll(".html", "");
        String priceMockUrl = "https://pas.suning.com/nspcsale_0_" + getFullShopId2(shopId) + "_" + getFullShopId2(shopId) + "_0000000000_10_010_0100100_20089_1000000_9017_10106_Z001___R1901001_0.44_0___00006J675___.html?callback=pcData&_=1575291502277";
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
        return null;
    }

    public static String getFullShopId2(String shopId) {
        String str = "";
        for (int i = 0; i < 18 - shopId.length(); i++) {
            str += "0";
        }
        return str + shopId;
    }

    private String getFullShopId(String shopId) {
        String str = "";
        for (int i = 0; i < 18 - shopId.length(); i++) {
            str += "0";
        }
        return str + shopId;
    }

    @Test
    public void initTmallItem() throws IOException {
        List<CrawerTmallItemResult> list = crawerService.getUndoTmallItem();
        for (CrawerTmallItemResult item : list) {
            if ("苏宁".equals(item.getSiteName())) {
                System.out.println("===========处理:" + JSONObject.toJSONString(item));
                String itemId = item.getItemId();
                String url = "https://product.suning.com/0000000000/" + itemId + ".html";
                System.out.println("===url:" + url);
                Element container = MockViewHelper.views(url).getDocument().getElementById("J-TZM");
                Elements lis = container.getElementsByTag("li");
                Set<String> skuSet = new HashSet<String>();
                for (int i = 0; i < lis.size(); i++) {
                    String sku = lis.get(i).attr("sku");
                    System.out.println(newSku(sku));
                    skuSet.add(newSku(sku));
                }
                if (skuSet.size() > 0) {
                    for (String sku : skuSet) {
                        CrawerSiteSkuResult ss = new CrawerSiteSkuResult();
                        ss.setBrand(item.getBrand());
                        ss.setSiteName(item.getSiteName());
                        ss.setLink("https://product.suning.com/0000000000/" + sku + ".html");
                        ss.setCreateTime(new Date());
                        ss.setExtra(item.getItemId());
                        try {
                            System.out.println("插入：===" + JSONObject.toJSONString(ss));
                            crawerService.addSku(ss);
                        } catch (Exception e) {

                        }

                    }
                    crawerService.updateTmallItemStatus(item.getId());
                } else {
                    CrawerSiteSkuResult ss = new CrawerSiteSkuResult();
                    ss.setBrand(item.getBrand());
                    ss.setSiteName(item.getSiteName());
                    ss.setLink("https://product.suning.com/0000000000/" + item.getItemId() + ".html");
                    ss.setCreateTime(new Date());
                    ss.setExtra(item.getItemId());
                    try {
                        System.out.println("插入：===" + JSONObject.toJSONString(ss));
                        crawerService.addSku(ss);
                    } catch (Exception e) {

                    }
                    crawerService.updateTmallItemStatus(item.getId());
                }

            } else if ("天猫".equals(item.getSiteName())) {
                tmallItem(item);
            }
        }
    }


    public void tmallSku(CrawerSiteSkuResult sku) {
        String url = sku.getLink();
        System.out.println("===url:" + url);
        Element ii = MockViewHelper.views(url).getDocument().getElementsByAttributeValue("class", "scroller preview-scroller").first().getElementsByTag("img").get(0);
        String cover = ii.attr("data-src");
        if (cover.startsWith("//")) {
            cover = "https:" + cover;
        }
        sku.setCover(cover);
        crawerService.updateSkuByLink(sku);


        Document doc = MockViewHelper.views(url, url, getCookies()).getDocument();
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
                            sku.setPropPath(propPath);
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
                            sku.setTitle(sb.toString() + title);
                            String price = skuCore.getJSONObject(skuId).getJSONObject("price").getString("priceMoney");
                            sku.setPrice(new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue());
                            sku.setCreateTime(new Date());
                            try {
                                System.out.println("更新：===" + JSONObject.toJSONString(sku));
                                crawerService.updateSkuByLink(sku);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (skuArr == null) {
                        sku.setTitle(title);
                        sku.setCreateTime(new Date());
                        String price = skuCore.getJSONObject("0").getJSONObject("price").getString("priceMoney");
                        sku.setPrice(new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue());
                        try {
                            System.out.println("更新：===" + JSONObject.toJSONString(sku));
                            crawerService.updateSkuByLink(sku);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    public void tmallItem(CrawerTmallItemResult crawerItem) {
        String url = "https://detail.m.tmall.com/item.htm?id=" + crawerItem.getItemId();
        System.out.println("===url:" + url);
        Document doc = MockViewHelper.views(url, url, getCookies()).getDocument();
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
                    crawerService.updateTmallItemTitle(crawerItem.getId(), title);
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
                            CrawerSiteSkuResult ss = new CrawerSiteSkuResult();
                            ss.setBrand(crawerItem.getBrand());
                            ss.setSiteName(crawerItem.getSiteName());
                            ss.setPropPath(propPath);
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
                            ss.setTitle(sb.toString() + title);
                            String price = skuCore.getJSONObject(skuId).getJSONObject("price").getString("priceMoney");
                            ss.setPrice(new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue());
                            ss.setLink("https://detail.m.tmall.com/item.htm?id=" + crawerItem.getItemId() + "&skuId=" + skuId);
                            ss.setCreateTime(new Date());
                            ss.setExtra(crawerItem.getItemId());
                            try {
                                System.out.println("插入：===" + JSONObject.toJSONString(ss));
                                int count = crawerService.updateSkuByLink(ss);
                                if (count == 0) {
                                    crawerService.addSku(ss);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            crawerService.updateTmallItemStatus(crawerItem.getId());
                        }
                    } else if (skuArr == null) {
                        CrawerSiteSkuResult ss = new CrawerSiteSkuResult();
                        ss.setBrand(crawerItem.getBrand());
                        ss.setSiteName(crawerItem.getSiteName());
                        ss.setTitle(title);
                        ss.setLink("https://detail.m.tmall.com/item.htm?id=" + crawerItem.getItemId());
                        ss.setCreateTime(new Date());
                        ss.setExtra(crawerItem.getItemId());
                        String price = skuCore.getJSONObject("0").getJSONObject("price").getString("priceMoney");
                        ss.setPrice(new BigDecimal(price).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_CEILING).doubleValue());
                        try {
                            System.out.println("插入：===" + JSONObject.toJSONString(ss));
                            int count = crawerService.updateSkuByLink(ss);
                            if (count == 0) {
                                crawerService.addSku(ss);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        crawerService.updateTmallItemStatus(crawerItem.getId());
                    }

                }
            }
        }
    }

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

    private static String newSku(String sku) {
        for (int i = 0; i < sku.length(); i++) {
            if (sku.charAt(i) != '0') {
                return sku.substring(i);
            }
        }
        return sku;
    }
}
