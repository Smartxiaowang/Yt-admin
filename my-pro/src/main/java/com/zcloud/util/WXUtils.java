package com.zcloud.util;

import java.io.BufferedReader;
import java.io.*;
import java.net.*;
import java.security.AlgorithmParameters;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.zcloud.entity.AuthToken;
import com.zcloud.entity.PageData;

public class WXUtils {
	private final static String requestUrl = "https://api.weixin.qq.com/sns/jscode2session"; 
//    private final static String appId = "wx7ff84f44fb881d41";
//    private final static String secret = "f8bba1057af41a8797c099ea443d8025";
    
    public static AuthToken getAuthToken(String code){
		  AuthToken vo = null;
		  AuthToken vo1 = null;
		  try {
		    String uri = "https://api.weixin.qq.com/cgi-bin/token?";
		    StringBuffer url = new StringBuffer(uri);
		    url.append("appid=").append(Const.OA_APPID);//公众号APPID 非小程序
		    url.append("&secret=").append(Const.OA_SECRET);//公众号secret 非小程序
//		    url.append("&code=").append(code);
		    url.append("&grant_type=").append("client_credential");
		    HttpURLConnection conn = HttpClientUtil.getHttpURLConnection(url.toString());
		    InputStream input = null;
		    if (conn.getResponseCode() == 200) {
		      input = conn.getInputStream();
		    } else {
		      input = conn.getErrorStream();
		    }
		    vo = JSON.parseObject(new String(HttpClientUtil.InputStreamTOByte(input),"utf-8"),AuthToken.class);
		    
		    String tokenuri = "https://api.weixin.qq.com/cgi-bin/user/info?";
		    StringBuffer tokenurl = new StringBuffer(tokenuri);
		    tokenurl.append("access_token=").append(vo.getAccess_token());
		    HttpURLConnection conn1 = HttpClientUtil.getHttpURLConnection(url.toString());
		    InputStream input1 = null;
		    if (conn1.getResponseCode() == 200) {
		    	input1 = conn1.getInputStream();
		    } else {
		      input1 = conn1.getErrorStream();
		    }
		    System.out.println(new String(HttpClientUtil.InputStreamTOByte(input1),"utf-8"));
		    vo1= JSON.parseObject(new String(HttpClientUtil.InputStreamTOByte(input),"utf-8"),AuthToken.class);
		  } catch (Exception e) {
		   // log.error("getAuthToken error", e);
		  }
		  return vo;
		}
    
    /**
     * 获取用户信息
     * 
     * @param accessToken 接口访问凭证
     * @param openId 用户标识
     * @return WeixinUserInfo
     */
    public static PageData getUserInfo(String accessToken, String openId) {
    	PageData pd = new PageData();
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 获取用户信息
        JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
        		pd.put("OPENID", jsonObject.getString("openid"));	//用户的标识
        		pd.put("SUBSCRIBE", jsonObject.getInteger("subscribe"));	//关注状态（1是关注，0是未关注），未关注时获取不到其余信息
        		pd.put("SUBSCRIBETIME", jsonObject.getString("subscribe_time"));	//用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
        		pd.put("NICKNAME",  jsonObject.getString("nickname"));	//昵称
        		pd.put("SEX", jsonObject.getInteger("sex"));	//用户的性别（1是男性，2是女性，0是未知）
        		pd.put("COUNTRY", jsonObject.getString("country"));	//用户所在国家
        		pd.put("PROVINCE", jsonObject.getString("province"));	//用户所在省份
        		pd.put("CITY", jsonObject.getString("city"));	// 用户所在城市
        		pd.put("LANGUAGE",jsonObject.getString("language"));	//用户的语言，简体中文为zh_CN
        		pd.put("HEADIMGURL", jsonObject.getString("headimgurl"));	// 用户头像
            } catch (Exception e) {
               e.printStackTrace();
            }
        }
        return pd;
    }
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
          //  log.error("连接超时：{}", ce);
        } catch (Exception e) {
           // log.error("https请求异常：{}", e);
        }
        return jsonObject;
    }
    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     */
    public static Map getSessionKeyOrOpenId(String code, String encryptedData, String iv) {
        //微信端登录code值
        Map<String, String> requestUrlParam = new HashMap<>();
        requestUrlParam.put("appid", Const.APPID);    //开发者设置中的appId
        requestUrlParam.put("secret", Const.SECRET);    //开发者设置中的appSecret
        requestUrlParam.put("js_code", code);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数
    
        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject res1 = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        Map result = new HashMap();
        if (res1 != null && res1.get("errcode") != null) {
            result.put("status", 0);
            result.put("msg", "解析失败,请检查入参code!");
            return result;
        } else {
            JSONObject res2 = getUserInfo(encryptedData, res1.get("session_key").toString(), iv);
            if (null != res2) {
                Map userInfo = new HashMap();
                result.put("status", 1);
                result.put("msg", "解密成功!");
                userInfo.put("openId", res2.get("openId"));
                userInfo.put("nickName", res2.get("nickName"));
                userInfo.put("gender", res2.get("gender"));
                userInfo.put("city", res2.get("city"));
                userInfo.put("province", res2.get("province"));
                userInfo.put("country", res2.get("country"));
                userInfo.put("avatarUrl", res2.get("avatarUrl"));
                userInfo.put("unionId", res2.get("unionId"));
                result.put("userInfo", userInfo);
                return result;
            }
        }
        result.put("status", 0);
        result.put("msg", "解析失败,请检查入参!");
        return result;
    }
    
    /**
     * 获取微信小程序 session_key 和 openid
     *
     * @param code 调用微信登陆返回的Code
     * @return
     */
    public static Map getOpenId(String code) {
        //微信端登录code值
        Map<String, String> requestUrlParam = new HashMap<>();
        requestUrlParam.put("appid", Const.APPID);    //开发者设置中的appId
        requestUrlParam.put("secret", Const.SECRET);    //开发者设置中的appSecret
        requestUrlParam.put("js_code", code);    //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "authorization_code");    //默认参数
    
        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject res1 = JSON.parseObject(sendPost(requestUrl, requestUrlParam));
        Map result = new HashMap();
        System.out.println(res1.toJSONString());
        if (res1 != null && res1.get("errcode") != null) {
            result.put("status", 0);
            result.put("msg", "解析失败,请检查入参code!");
            return result;
        } else {
            result.put("openid",res1.get("openid").toString());
        }
        result.put("status", 0);
        result.put("msg", "解析成功!");
        return result;
    }

	public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
		System.out.println(encryptedData);
		System.out.println(sessionKey);
		System.out.println(iv);
		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);
		// 偏移量
		byte[] ivByte = Base64.decode(iv);

		try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
            return null;
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	        return null;
	    }
	
	 /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url      发送请求的 URL
     * @param paramMap 请求参数
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";
        Iterator<String> it = paramMap.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}