package com.yd.api.pay.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.*;

public class HttpClientUtil {
	private static final Logger	LOG=Logger.getLogger(HttpClientUtil.class);
	
	
	public static String postBody(String url, String params) throws ClientProtocolException, IOException {
		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new StringEntity(params, "UTF-8"));
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httpost);
		String ret = EntityUtils.toString(response.getEntity(), "utf-8");
		return ret;
	}
	
	public static String get(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		String body = null;
		StringBuilder str = new StringBuilder();
		str.append(url);
		
		if(params!=null){
			boolean first = true;
			for (String key : params.keySet()) {
				if (first) {
					first = false;
					str.append("?");
				} else {
					str.append("&");
				}
				str.append(key).append("=").append(params.get(key));
			}
		}
		HttpGet get = new HttpGet(str.toString());
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}
	
	public static String getWithHeader(String url, Map<String, String> params,Map<String,String> header) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		String body = null;
		StringBuilder str = new StringBuilder();
		str.append(url);
		
		if(params!=null){
			boolean first = true;
			for (String key : params.keySet()) {
				if (first) {
					first = false;
					str.append("?");
				} else {
					str.append("&");
				}
				str.append(key).append("=").append(params.get(key));
			}
		}
		HttpGet get = new HttpGet(str.toString());
		if(header!=null && header.size()>0) {
			for(Entry<String,String> entry:header.entrySet()) {
				get.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {

		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = null;

		try {
			httpclient.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String charset = EntityUtils.getContentCharSet(entity);

		String body = null;
		try {
			body = EntityUtils.toString(entity,"UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}
	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpPost post = postForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}
	
	private static HttpPost postForm(String url, Map<String, String> params) {

		HttpPost httpost = new HttpPost(url);
		// httpost.setHeader(HTTP.CHARSET_PARAM, "UTF-8");
		// httpost.setHeader("Content-Type",
		// "	application/x-www-form-urlencoded");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps,
					"UTF-8");
			// entity.setContentType("text/xml;　charset=utf-8");
			entity.setContentType("application/x-www-form-urlencoded");

			httpost.setEntity(entity);

			// httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	public static final String sendHttpsRequestByPost(String url, Map<String, String> params) {
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		//创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		//这个好像是HOST验证
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};
		try {
			//TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			//创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
			for (Map.Entry<String, String> entry : params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
}
