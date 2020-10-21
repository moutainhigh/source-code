package com.yd.service.crawer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.yd.core.QiniuHelper;
import com.yd.service.crawer.util.MockViewHelper;

public class JDCrawerProcess {
	public static void main(String[] args) {
		// String url="http://detail.zol.com.cn/cell_phone/index1238183.shtml";
		String url="https://product.suning.com/0000000000/11604306043.html?safp=d488778a.13701.productWrap.2&safc=prd.3.ssdsn_pic01-1_jz";
		getSuNingItemImage(url);
	}
	public static String getItemContent(String url) {
		Element e=MockViewHelper.views(url).getDocument().getElementsByAttributeValue("id", "J-detail-content").get(0);
		Elements imgList=e.getElementsByTag("img");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<imgList.size();i++) {
			String imgUrl=imgList.get(i).attr("data-lazyload");
			if(imgUrl.startsWith("//")) {
				imgUrl="http:"+imgUrl;
			}
			System.out.println("上传图片："+imgUrl);
			String newImgUrl=QiniuHelper.upload(imgUrl);
			sb.append("<img src=\""+newImgUrl+"\"/>");
		}
		return sb.toString();
	}

	public static List<String> getItemImage(String url) {
		List<String> imageList = new ArrayList<>();
		Element e=MockViewHelper.views(url).getDocument().getElementsByAttributeValue("id", "threeSmallPic").get(0);
		Elements imgList=e.getElementsByTag("img");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<imgList.size();i++) {
			String imgUrl=imgList.get(i).attr("src");
			String newImageUrl = imgUrl.replaceAll("100x75", "800x600");
			String newImgUrl=QiniuHelper.upload(newImageUrl);
			imageList.add(newImgUrl);
		}
		return imageList;
	}

	public static String getSuningItemContent(String url) {
		Element e=MockViewHelper.views(url).getDocument().getElementsByAttributeValue("modulename", "商品详情").get(0);
		Elements imgList=e.getElementsByTag("img");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<imgList.size();i++) {
			String imgUrl=imgList.get(i).attr("src2");
			if(imgUrl.startsWith("//")) {
				imgUrl="http:"+imgUrl;
			}
			System.out.println("上传图片："+imgUrl);
			String newImgUrl=QiniuHelper.upload(imgUrl);
			sb.append("<img src=\""+newImgUrl+"\"/>");
		}
		return sb.toString();
	}

	public static List<String> getSuNingItemImage(String url) {
		List<String> imageList = new ArrayList<>();
		Element e=MockViewHelper.views(url).getDocument().getElementsByAttributeValue("modulename", "商品详情").get(0);
		Elements imgList=e.getElementsByTag("img");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<imgList.size();i++) {
			String imgUrl=imgList.get(i).attr("src2");
			imgUrl = "http:" + imgUrl;
			String newImageUrl = imgUrl.replaceAll("100x75", "800x600");
			String newImgUrl=QiniuHelper.upload(newImageUrl);
			imageList.add(newImgUrl);
		}
		return imageList;
	}

	
	//https://shop.m.jd.com/?venderid=1000003443 三星
	public static void t(String[] args) throws MalformedURLException, InterruptedException {
		String url="https://mall.jd.com/view_page-135049272.html";
		WebClient wc = new WebClient(BrowserVersion.CHROME);
		//是否使用不安全的SSL
		wc.getOptions().setUseInsecureSSL(true);
		//启用JS解释器，默认为true
		wc.getOptions().setJavaScriptEnabled(true);
		//禁用CSS
		wc.getOptions().setCssEnabled(false);
		//js运行错误时，是否抛出异常
		wc.getOptions().setThrowExceptionOnScriptError(false);
		//状态码错误时，是否抛出异常
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
		//是否允许使用ActiveX
		wc.getOptions().setActiveXNative(false);
		//等待js时间
		wc.waitForBackgroundJavaScript(600*1000);
		//设置Ajax异步处理控制器即启用Ajax支持
		wc.setAjaxController(new NicelyResynchronizingAjaxController());
		//设置超时时间
		wc.getOptions().setTimeout(1000000);
		//不跟踪抓取
		wc.getOptions().setDoNotTrackEnabled(false);
		WebRequest request=new WebRequest(new URL(url));
		request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
		
		 try {
			//模拟浏览器打开一个目标网址
			 HtmlPage htmlPage = wc.getPage(request);
			 //为了获取js执行的数据 线程开始沉睡等待
			 Thread.sleep(10000);//这个线程的等待 因为js加载需要时间的
			  //以xml形式获取响应文本
			String xml = htmlPage.asXml();
			 //并转为Document对象return
			System.out.println("====================================================");
			System.out.println(xml);
			System.out.println("====================================================");
			//System.out.println(xml.contains("结果.xls"));//false
		} catch (FailingHttpStatusCodeException e) {
			 e.printStackTrace();
		 } catch (MalformedURLException e) {
			 e.printStackTrace();
		 } catch (IOException e) {
			  e.printStackTrace();
		}
		
	}
}
