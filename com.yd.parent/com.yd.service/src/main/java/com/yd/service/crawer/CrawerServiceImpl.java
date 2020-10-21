package com.yd.service.crawer;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.JSONObject;
import com.yd.api.crawer.CrawerService;
import com.yd.api.crawer.req.CrawerSiteBrandReq;
import com.yd.api.crawer.req.CrawerSiteSkuReq;
import com.yd.api.crawer.result.BijiaColumn;
import com.yd.api.crawer.result.CrawerBrandResult;
import com.yd.api.crawer.result.CrawerItemResult;
import com.yd.api.crawer.result.CrawerSiteBrandResult;
import com.yd.api.crawer.result.CrawerSiteSkuResult;
import com.yd.api.crawer.result.CrawerTmallItemResult;
import com.yd.core.utils.BeanUtilExt;
import com.yd.core.utils.DTOUtils;
import com.yd.core.utils.PropertiesHelp;
import com.yd.service.bean.item.YdItem;
import com.yd.service.bean.item.YdItemSku;
import com.yd.service.bean.item.YdMerchantItemSku;
import com.yd.service.crawer.bean.CrawerBrand;
import com.yd.service.crawer.bean.CrawerItem;
import com.yd.service.crawer.bean.CrawerSiteBrand;
import com.yd.service.crawer.bean.CrawerSiteSku;
import com.yd.service.crawer.bean.CrawerTmallItem;
import com.yd.service.crawer.bean.YdBijia;
import com.yd.service.crawer.dao.CrawerBrandDao;
import com.yd.service.crawer.dao.CrawerItemDao;
import com.yd.service.crawer.dao.CrawerSiteBrandDao;
import com.yd.service.crawer.dao.CrawerSiteSkuDao;
import com.yd.service.crawer.dao.CrawerTmallItemDao;
import com.yd.service.crawer.dao.YdBijiaDao;
import com.yd.service.dao.item.YdItemDao;
import com.yd.service.dao.item.YdItemSkuDao;
import com.yd.service.dao.item.YdMerchantItemSkuDao;

@Service
public class CrawerServiceImpl implements CrawerService {

    private static final Logger logger = LoggerFactory.getLogger(CrawerServiceImpl.class);

    @Resource
    private YdItemDao ydItemDao;

    @Resource
    private YdBijiaDao ydBijiaDao;

    @Resource
    private YdItemSkuDao ydItemSkuDao;

    @Resource
    private CrawerBrandDao crawerBrandDao;

    @Resource
    private CrawerItemDao crawerItemDao;

    @Resource
    private CrawerSiteBrandDao crawerSiteBrandDao;

    @Resource
    private CrawerTmallItemDao crawerTmallItemDao;

    @Resource
    private CrawerSiteSkuDao crawerSiteSkuDao;

    @Resource
    private YdMerchantItemSkuDao ydMerchantItemSkuDao;

    @Override
    public void addBrand(CrawerBrandResult brand) {
        CrawerBrand item = new CrawerBrand();
        BeanUtilExt.copyProperties(item, brand);
        try {
            crawerBrandDao.insert(item);
        } catch (Exception e) {

        }
    }

    @Override
    public List<CrawerBrandResult> getBrandList() {
        List<CrawerBrand> list = crawerBrandDao.getBrandList();
        return DTOUtils.convertList(list, CrawerBrandResult.class);
    }

    @Override
    public void addItem(CrawerItemResult result) {
        CrawerItem item = new CrawerItem();
        BeanUtilExt.copyProperties(item, result);
        try {
            CrawerItem crawerItem = new CrawerItem();
            crawerItem.setTitle(result.getTitle());
            List<CrawerItem> itemList = crawerItemDao.findCrawerItemsByPage(crawerItem, 0, 10);
            if (CollectionUtils.isEmpty(itemList)) {
                crawerItemDao.insert(item);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public List<CrawerItemResult> getItemList() {
        List<CrawerItem> itemList = crawerItemDao.getUnExecuteList();
        return DTOUtils.convertList(itemList, CrawerItemResult.class);
    }

    @Override
    public void updateExecuteStatus(Integer id) {
        crawerItemDao.updateExecuteStatus(id);
    }

    @Override
    public CrawerItemResult getDetail(Integer id) {
        CrawerItem item = crawerItemDao.findCrawerItemById(id);
        if (item == null) {
            return null;
        }
        CrawerItemResult result = new CrawerItemResult();
        BeanUtilExt.copyProperties(result, item);

        return result;
    }

    @Override
    public List<CrawerSiteBrandResult> getAllCrawerSiteBrand() {
        CrawerSiteBrandReq req = new CrawerSiteBrandReq();
        List<CrawerSiteBrand> list = crawerSiteBrandDao.findCrawerSiteBrandsByPage(req, 0, Integer.MAX_VALUE);

        return DTOUtils.convertList(list, CrawerSiteBrandResult.class);
    }

    @Override
    public List<CrawerTmallItemResult> getUndoTmallItem() {
        List<CrawerTmallItem> list = crawerTmallItemDao.getUndoTmallItem();

        return DTOUtils.convertList(list, CrawerTmallItemResult.class);
    }

    @Override
    public void addSku(CrawerSiteSkuResult result) {
        CrawerSiteSku item = new CrawerSiteSku();
        BeanUtilExt.copyProperties(item, result);
        crawerSiteSkuDao.insert(item);
    }

    @Override
    public void updateTmallItemStatus(Integer id) {
        crawerTmallItemDao.updateTmallItemStatus(id);
    }

    @Override
    public void updateTmallItemTitle(Integer id, String title) {
        crawerTmallItemDao.updateTmallItemTitle(id, title);
    }

    @Override
    public List<CrawerSiteSkuResult> getUndoSkuList() {
        List<CrawerSiteSku> list = crawerSiteSkuDao.getUndoSkuList();
        return DTOUtils.convertList(list, CrawerSiteSkuResult.class);
    }

    @Override
    public int updateSkuByLink(CrawerSiteSkuResult ss) {
        CrawerSiteSku item = new CrawerSiteSku();
        BeanUtilExt.copyProperties(item, ss);
        return crawerSiteSkuDao.updateSkuByLink(item);
    }

    @Override
    public List<CrawerSiteSkuResult> getAllSkuList() {
        CrawerSiteSkuReq req = new CrawerSiteSkuReq();
        List<CrawerSiteSku> skuList = crawerSiteSkuDao.findCrawerSiteSkusByPage(req, 0, Integer.MAX_VALUE);
        return DTOUtils.convertList(skuList, CrawerSiteSkuResult.class);
    }

    @Override
    public Map<String, List<CrawerSiteSkuResult>> search(Integer skuId) {
        Map<String, List<CrawerSiteSkuResult>> map = new HashMap<String, List<CrawerSiteSkuResult>>();
        YdItemSku sku = ydItemSkuDao.getYdItemSkuById(skuId);
        YdItem item = ydItemDao.getYdItemById(sku.getItemId());

        List<CrawerSiteSkuResult> jingdong = search("京东", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
        List<CrawerSiteSkuResult> guomei = search("国美", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
        List<CrawerSiteSkuResult> tmall = search("天猫", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
        List<CrawerSiteSkuResult> suning = search("苏宁", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));

        map.put("jingdong", jingdong);
        map.put("guomei", guomei);
        map.put("tmall", tmall);
        map.put("suning", suning);
        return map;
    }

    public String getSpecNameValues(String specNameValueJson) {
        if (StringUtils.isEmpty(specNameValueJson)) {
            return "";
        }
        String str = "";
        JSONObject json = JSONObject.parseObject(specNameValueJson);
        for (Entry<String, Object> entry : json.entrySet()) {
            str += entry.getValue() + " ";
        }

        return str;
    }


    @Override
    public List<CrawerSiteSkuResult> search(String siteName, String brand, String title) {
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

            String lucenePath = PropertiesHelp.getProperty("lucenePath");
            Directory directory = FSDirectory.open(new File(lucenePath));
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

        return result;
    }

    @Override
    public List<BijiaColumn> getBijia(Integer merchantSkuId) {
        YdMerchantItemSku merchantSku = ydMerchantItemSkuDao.getYdMerchantItemSkuById(merchantSkuId);
        if (merchantSku == null) {
            return null;
        }
        List<BijiaColumn> list = new ArrayList<BijiaColumn>();
        YdBijia bijia = ydBijiaDao.findYdBijiaBySkuId(merchantSku.getSkuId());
        if (bijia == null) {
            return null;
        }
        Integer tmallId = bijia.getTmallItemId();
        Integer jdId = bijia.getJdItemId();
        Integer suningId = bijia.getSuningItemId();
        Integer guomeiId = bijia.getGuomeiItemId();

        if (tmallId != null && tmallId != 0) {
            CrawerSiteSku siteSku = crawerSiteSkuDao.findCrawerSiteSkuById(tmallId);
            BijiaColumn column = siteSkuToBijiaColumn(siteSku);
            if (column != null) {
                list.add(column);
            }
        }
        if (jdId != null && jdId != 0) {
            CrawerSiteSku siteSku = crawerSiteSkuDao.findCrawerSiteSkuById(jdId);
            BijiaColumn column = siteSkuToBijiaColumn(siteSku);
            if (column != null) {
                list.add(column);
            }
        }
        if (suningId != null && suningId != 0) {
            CrawerSiteSku siteSku = crawerSiteSkuDao.findCrawerSiteSkuById(suningId);
            BijiaColumn column = siteSkuToBijiaColumn(siteSku);
            if (column != null) {
                list.add(column);
            }
        }
        if (guomeiId != null && guomeiId != 0) {
            CrawerSiteSku siteSku = crawerSiteSkuDao.findCrawerSiteSkuById(guomeiId);
            BijiaColumn column = siteSkuToBijiaColumn(siteSku);
            if (column != null) {
                list.add(column);
            }
        }
        return list;
    }

    private BijiaColumn siteSkuToBijiaColumn(CrawerSiteSku siteSku) {
        if (siteSku != null) {
            BijiaColumn column = new BijiaColumn();
            column.setSiteName(siteSku.getSiteName());
            column.setTitle(siteSku.getTitle());
            column.setCover(siteSku.getCover());
            String price = "";
            if (siteSku.getPrice() == null || siteSku.getPrice() < 0) {
                price = "-";
            } else {
                price = new BigDecimal("" + siteSku.getPrice()).setScale(2, BigDecimal.ROUND_UP).toString();
            }
            column.setPrice(price);
            return column;
        }

        return null;
    }

    @Override
    public void createSearchIndex() {
        List<CrawerSiteSkuResult> list = getAllSkuList();
        try {
            String lucenePath = PropertiesHelp.getProperty("lucenePath");
            System.out.println("=========================================");
            System.out.println("=============lucenePath:" + lucenePath);
            System.out.println("=========================================");
            Directory directory = FSDirectory.open(new File(lucenePath));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CrawerSiteSkuResult> getCoverNullList() {
        List<CrawerSiteSku> list = crawerSiteSkuDao.getCoverNullList();
        return DTOUtils.convertList(list, CrawerSiteSkuResult.class);
    }

    @Override
    public void chooseItem(Integer skuId, String currSite, Integer id) {

        YdBijia bijia = new YdBijia();
        bijia.setSkuId(skuId);
        YdItemSku sku = ydItemSkuDao.getYdItemSkuById(skuId);
        bijia.setItemId(sku.getItemId());
        if ("京东".equals(currSite)) {
            bijia.setJdItemId(id);
        } else if ("国美".equals(currSite)) {
            bijia.setGuomeiItemId(id);
        } else if ("苏宁".equals(currSite)) {
            bijia.setSuningItemId(id);
        } else if ("天猫".equals(currSite)) {
            bijia.setTmallItemId(id);
        }
        int count = ydBijiaDao.updateBySkuId(bijia);
        if (count == 0) {
            bijia.setCreateTime(new Date());
            ydBijiaDao.insert(bijia);
        }
    }

    /**
     * 商品库商品绑定平台商品,
     */
    @Override
    public void bindOtherPlatformItem() {
        YdItemSku ydItemSku = new YdItemSku();
        List<YdItemSku> skuList = ydItemSkuDao.getAll(ydItemSku);
        Date startTime = new Date();
        for (YdItemSku sku : skuList) {
            try {
                Integer skuId = sku.getId();
                YdItem item = ydItemDao.getYdItemById(sku.getItemId());
                List<CrawerSiteSkuResult> jingdongList = search("京东", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
                setBindItem(skuId, jingdongList);

                List<CrawerSiteSkuResult> guomeiList = search("国美", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
                setBindItem(skuId, guomeiList);

                List<CrawerSiteSkuResult> tianmaoList = search("天猫", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
                setBindItem(skuId, tianmaoList);

                List<CrawerSiteSkuResult> suningList = search("苏宁", item.getBrand(), item.getTitle() + " " + getSpecNameValues(sku.getSpecNameValueJson()));
                setBindItem(skuId, suningList);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("====");
            }
        }

        Date endTime = new Date();
        logger.info("====绑定平台商品用时=" + (endTime.getTime() - startTime.getTime()));
    }

    private void setBindItem(Integer skuId, List<CrawerSiteSkuResult> crawerSiteList) {
        if (CollectionUtils.isEmpty(crawerSiteList)) return;
        chooseItem(skuId, crawerSiteList.get(0).getSiteName(), crawerSiteList.get(0).getId());
    }

}
