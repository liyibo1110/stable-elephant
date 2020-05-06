package com.github.liyibo1110.stable.elephant.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.liyibo1110.stable.elephant.util.Constants;

@Component
public class HttpFetcher {

	private static Logger logger = LoggerFactory.getLogger(HttpFetcher.class);
	
	private final int timeout = Integer.MAX_VALUE;	//无限制
	private final String user_agent = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36";
	private final String accept_language = "zh-cn";
	private final String accept = "*/*";
	private final String accept_encoding = "gzip";

	protected CookieStore cookies = new BasicCookieStore();
	protected CloseableHttpClient client = init();

	private CloseableHttpClient init() {
		try {
			HttpClientBuilder builder = HttpClientBuilder.create();
			RequestConfig rc = RequestConfig.custom()
					.setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout)
					.setSocketTimeout(timeout)
					.build();
			builder.setDefaultRequestConfig(rc);
			builder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(timeout).build());
			List<Header> headers = new ArrayList<>();
			headers.add(new BasicHeader("User-Agent", user_agent));
			headers.add(new BasicHeader("Accept-Language", accept_language));
			headers.add(new BasicHeader("Accept", accept));
			headers.add(new BasicHeader("Accept-Encoding", accept_encoding));
			builder.setDefaultHeaders(headers);

			this.cookies.clear();
			builder.setDefaultCookieStore(this.cookies);

			return builder.build();
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public String post(String url, Map<String, Object> params) {
		HttpEntity entity = null;
		CloseableHttpResponse rsp = null;
		HttpPost p = null;
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			nvps.add(new BasicNameValuePair(key, value.toString()));  
		}
		try {
			p = new HttpPost(url);
			p.setEntity(new UrlEncodedFormEntity(nvps, Constants.DEFAULT_CHARSET));
			rsp = this.client.execute(p);
			
			/*logger.info(rsp.getAllHeaders().toString());
			logger.info("========================================================");
			for(Cookie cookie: this.cookies.getCookies()) {
				logger.info(cookie.getName());
				logger.info(cookie.getValue());
			}*/
			
			entity = rsp.getEntity();
			return EntityUtils.toString(entity);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			if(p != null) {
				p.abort();
			}
			EntityUtils.consumeQuietly(entity);
			try {
				rsp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String post(String url, String body) {
		HttpEntity entity = null;
		CloseableHttpResponse rsp = null;
		HttpPost p = null;
		
		StringEntity se = new StringEntity(body, "utf-8");
		se.setContentEncoding("utf-8");
		se.setContentType("application/json");
		try {
			p = new HttpPost(url);
			p.setEntity(se);
			rsp = this.client.execute(p);
			entity = rsp.getEntity();
			return EntityUtils.toString(entity);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			if(p != null) {
				p.abort();
			}
			EntityUtils.consumeQuietly(entity);
			try {
				rsp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getIp(HttpServletRequest request) {
	    String ip = request.getHeader("x-forwarded-for");
	    if(StringUtils.isNotBlank(ip)){
	    	//logger.info("从x-forwarded-for获取到的");
	    	return ip;
	    }
	    
	    ip = request.getHeader("Proxy-Client-IP");
	    if(StringUtils.isNotBlank(ip)){
	    	//logger.info("从Proxy-Client-IP获取到的");
	    	return ip;
	    }
	    
	    ip = request.getHeader("WL-Proxy-Client-IP");
	    if(StringUtils.isNotBlank(ip)){
	    	//logger.info("WL-Proxy-Client-IP");
	    	return ip;
	    }
	    
	    ip = request.getHeader("X-Real-IP");
	    if(StringUtils.isNotBlank(ip)){
	    	//logger.info("X-Real-IP");
	    	return ip;
	    }
	    
	    ip = request.getRemoteAddr();
	    if(StringUtils.isNotBlank(ip)){
	    	//logger.info("getRemoteAddr");
	    	return ip;
	    }
	    
	    return null;
	 }
}
