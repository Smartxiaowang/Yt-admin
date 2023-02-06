package com.zcloud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zcloud.entity.PageData;

@Component
public class BasicInfoUtil {
	
	private static String basicInfoId;
	
	@Value("${config.basicInfo.id}")
    public void setBasicInfoId(String basicInfoId) {
		BasicInfoUtil.basicInfoId = basicInfoId;
    }

	private static String basicUrl;
	
	@Value("${config.basic.url}")
	public void setBasicUrl(String basicUrl) {
		BasicInfoUtil.basicUrl = basicUrl;
    }
	
	/*
	 * 判断是否在有效期内
	 */
	public static boolean checkValid() {
		try {
			String errInfo = "success";
			String uri = basicUrl +"/admin/isValid";
	        CloseableHttpClient client = null;
	        CloseableHttpResponse response = null;
	        try {
	            HttpPost httpPost = new HttpPost(uri);
	            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
	//                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
	//                        ContentType.create("application/x-www-form-urlencoded", "UTF-8")));
	            List params=new ArrayList();
	            //添加参数
	            params.add(new BasicNameValuePair("BASICINFO_ID",basicInfoId));
	            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
	
	            client = HttpClients.createDefault();
	            response = client.execute(httpPost);
	            HttpEntity entity = response.getEntity();
	            String result = EntityUtils.toString(entity);
	            System.out.println(result);
	            HashMap resultMap = JSON.parseObject(result, HashMap.class);//返回结果转换为map
	            
	            if(resultMap.get("result").equals("success")) {
	            	return true;
	            }else {
	            	return false;
	            }
	        }finally {
	            if (response != null) {
	                response.close();
	            }
	            if (client != null) {
	                client.close();
	            }
	        }
		}catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	
	/*
	 * 数据同步到总后台
	 * url 请求地址
	 * pd 数据实体
	 */
	public static boolean doSync(String url,PageData pd) {
		try {
			String errInfo = "success";
			String uri = basicUrl + url;
			System.out.println(uri);
	        CloseableHttpClient client = null;
	        CloseableHttpResponse response = null;
	        try {
	            HttpPost httpPost = new HttpPost(uri);
	            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
	//                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
	//                        ContentType.create("application/x-www-form-urlencoded", "UTF-8")));
	            List params=new ArrayList();
	            //添加参数
	            Map<Object, Object> map = (Map<Object, Object>)pd;
	            for (Map.Entry<Object, Object> entry : map.entrySet()) {
	            	params.add(new BasicNameValuePair(entry.getKey().toString(),((Object)entry.getValue()).toString()));
	            }
	            params.add(new BasicNameValuePair("BASICINFO_ID",basicInfoId));
	            httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
	
	            client = HttpClients.createDefault();
	            response = client.execute(httpPost);
	            HttpEntity entity = response.getEntity();
	            String result = EntityUtils.toString(entity);
	            System.out.println(result);
	            HashMap resultMap = JSON.parseObject(result, HashMap.class);//返回结果转换为map
	            
	            if(resultMap.get("result").equals("success")) {
	            	return true;
	            }else {
	            	return false;
	            }
	        }finally {
	            if (response != null) {
	                response.close();
	            }
	            if (client != null) {
	                client.close();
	            }
	        }
		}catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
}
