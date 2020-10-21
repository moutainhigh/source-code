package com.yd.api.service.crawer;

/**
 * @Title:商品爬虫接口
 * @Description:
 * @Author:Wuyc
 * @Since:2019-11-27 18:12:55
 * @Version:1.1.0
 */
public interface YdCrawerService {

	/**
	 * 抓取数据,顺序为
	 * 	1. 爬虫品牌库
	 * 	2. 爬虫商品库
	 * 	3. 优度商品,优度商品规格
	 * 	4. 同步商品发布时间
	 * 	5. 优度商品图片
	 * 	6. 优度商品详情
	 */
	void synAll();

	void synCrawerBrand();

	void synCrawerItem();

	void synYdItemAndSku();

	void synItemPublicTime();

	void synYdImage();

	void synYdContent();

	void deleteRepeatItem();

}
