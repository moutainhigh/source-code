package com.yg.core;


import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.yg.core.utils.DateUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

public class QiniuHelper {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static String accessKey = "nHXa04vz1ytDzqjHTag5m_vurM6ePKegsGtun_Ch";
    // private static String secretKey = "kJsR71vhQBvHDLO4IjbQOx";
    private static String secretKey = "kJsR71vhQBvHDLO4IjbQOx-LprFkXTwRfPMI7R2w";

    //要上传的空间
    private static String BUCKET = "51-jujibao";
    //密钥配置
    private static String DOMAIN = "https://c9.51jujibao.com";


	
    public static void main(String[] args) throws IOException, URISyntaxException {
//		String str="http://img.qlchat.com/qlLive/adminImg/1RJDKG34-ORJC-CLXI-1506077276735-GAXRZ2IIW7YL.jpg";
//		System.out.println(upload(str));
        String filename = "InsertPic_(12-06-11-39-06).jpg";
        File file = new File("C:\\Users\\Administrator\\Desktop\\award\\" + filename);
//		
//		byte[] data=FileUtils.readFileToByteArray(file);
//		String p=upload(data);
//		System.out.println(p);
//		
//		String str=upload("http://img.qlchat.com/qlLive/parental/8ZYGO8N1-5UO7-SDES-1530772445573-VLGK7T3JLQ6X.png");
//		System.out.println(str);
//
//		Auth auth = Auth.create(accessKey, secretKey);
//		String token = auth.uploadToken(BUCKET);
//		System.out.println(token);
        
        upload("https://c9.51jujibao.com/youPlus/merchant/2019/09/17/14/32/561568701976");
    }


    public static String getToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(BUCKET);

        return token;
    }

    public static String upload(byte[] data) {
        String filename = DateUtil.date(new Date(), DateUtil.DEFAULT_DATEFULLTIME_FORMAT) + (new Random().nextInt(1000) + 9000);
        String filePath = "upload/" + DateUtil.date(new Date(), DateUtil.DEFAULT_DATE_FORMAT).replaceAll("-", "/") + "/";
        String key = filePath + filename;
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(BUCKET);
        UploadManager um = new UploadManager();
        try {
            Response resp = um.put(data, key, token);

            //System.out.println("resp:"+JSONObject.toJSONString(resp));
            if (resp.statusCode == 200) {
                return DOMAIN + "/" + key;
            }
        } catch (QiniuException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String uploadTmp(byte[] data) {
        String filename = DateUtil.date(new Date(), DateUtil.DEFAULT_DATEFULLTIME_FORMAT) + (new Random().nextInt(1000) + 9000);
        String filePath = "tmp/" + DateUtil.date(new Date(), DateUtil.DEFAULT_DATE_FORMAT).replaceAll("-", "/") + "/";
        String key = filePath + filename;
        Auth auth = Auth.create(accessKey, secretKey);
        String token = auth.uploadToken(BUCKET);
        UploadManager um = new UploadManager();
        try {
            Response resp = um.put(data, key, token);

            //System.out.println("resp:"+JSONObject.toJSONString(resp));
            if (resp.statusCode == 200) {
                return DOMAIN + "/" + key;
            }
        } catch (QiniuException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String upload(String imageUrl) {
        //new一个URL对象  
        try {
            URL url = new URL(imageUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(60 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            return upload(data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来  
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);
        }
        //关闭输入流  
        inStream.close();
        //把outStream里的数据写入内存  
        return outStream.toByteArray();
    }
}
