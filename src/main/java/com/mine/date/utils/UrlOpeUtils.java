package com.mine.date.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class UrlOpeUtils {
	static String STR_URL = "(^https?://[^/]*)/?[^/]*";
	static Pattern patternUrl = Pattern.compile(STR_URL);
	
	public static String getUrlNoUri(String url){
		if(url == null){
			return null;
		}
		Matcher matcher = patternUrl.matcher(url);
		if(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	
	public static String getUrlNoUri(HttpServletRequest request){
    	if(request == null){
    		return null;
    	}
    	String url = request.getRequestURL().toString();
    	if(null != request.getHeader("x-forwarded-proto") && request.getHeader("x-forwarded-proto").equalsIgnoreCase("https")) {
			url = url.replace("http://", "https://");
		}
    	
    	return UrlOpeUtils.getUrlNoUri(url);
    }
	
	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> queryPairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return queryPairs;
	}
	public static HttpEntity<String> buildTokenCurl(String body, String clientId, String clientSecret) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		// "suiQ43gFSxWqauFgrDv3lQ:fMiYFrsfE8kzaQkTxy0X7wHP0SZ9kq5A";
		String plainCreds = clientId + ":" + clientSecret;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = org.apache.commons.codec.binary.Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		headers.add("Authorization", "Basic " + base64Creds);
		return new HttpEntity<>(body, headers);
	}
	public static String redirectURLByProt(HttpServletRequest request, String uri) {
		if (null != request.getHeader("x-forwarded-proto")
				&& request.getHeader("x-forwarded-proto").equalsIgnoreCase("https")) {
			// using the https
			if ("/".equals(request.getRequestURI())) {
				return "redirect:" + request.getRequestURL().toString().replace("http://", "https://") + uri;
			}
			return "redirect:https://" + request.getRequestURL().toString().replace("http://", "")
					.replace(request.getRequestURI(), "/" + uri);
		} else {
			// using http
			if ("/".equals(request.getRequestURI())) {
				return "redirect:" + request.getRequestURL().toString() + uri;
			}
			return "redirect:" + request.getRequestURL().toString().replace(request.getRequestURI(), "/" + uri);
		}
	}
}
