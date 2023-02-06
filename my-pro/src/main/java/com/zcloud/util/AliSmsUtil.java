package com.zcloud.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AliSmsUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIDJTv7Z3iNLyp";
    static final String accessKeySecret = "LcupykSFftCHbRvzuJIv5YWY0tWKaV";

    public static SendSmsResponse sendSms(Map<String,Object> sendmap) throws ClientException {
    	Map<String,Object> map = new HashMap<String,Object>();
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
//    	String alipaysms = Tools.readTxtFile("admin/config/alipaysms.txt");//读取WEBSOCKET配置
//		if(null != alipaysms && !"".equals(alipaysms)){
//			String strIW[] = alipaysms.split(",");
//			if(strIW.length == 3){
//				map.put("phoneNumber", strIW[0]);	//待发送手机号
//				map.put("signName", strIW[1]);		//短信签名
//				map.put("templateCode", strIW[2]);				//短信模板
//			}
//		}
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号18630110835
        request.setPhoneNumbers(sendmap.get("phone").toString());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("兔车车");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_205405192");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//        if(sendmap.get("paytype").toString().equals("zfb")){
//        	paytype="支付宝";
//        }else{
//        	paytype="网银";
//        }
        request.setTemplateParam("{\"code\":\""+sendmap.get("code").toString()+"\"}");
        //  request.setTemplateParam("{\"code\":\""+234+"\",\"\code\":\""+234+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }
	  public static  String getCode(){
	    	String code = "";
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
	 
				int r = random.nextInt(10); //每次随机出一个数字（0-9）
	 
				code = code + r;  //把每次随机出的数字拼在一起
	 
			}
			return code;
	    }
    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("18630363710");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {
        //发短信
    	Map<String,Object> map = new HashMap<String,Object>();
		String code=getCode();
		map.put("phone",  "18630363710");
		map.put("code", code);
		//SendSmsResponse ssms= AliSmsUtil.sendSms(map);
//		map.put("infoDetail", "阿斯顿发");
//		map.put("phone", "18630363710");
//		map.put("infoType", "阿三地方");
        SendSmsResponse response = sendSms(map);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

//        Thread.sleep(3000L);
//
//        //查明细
//        if(response.getCode() != null && response.getCode().equals("OK")) {
//            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
//            System.out.println("短信明细查询接口返回数据----------------");
//            System.out.println("Code=" + querySendDetailsResponse.getCode());
//            System.out.println("Message=" + querySendDetailsResponse.getMessage());
//            int i = 0;
//            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
//            {
//                System.out.println("SmsSendDetailDTO["+i+"]:");
//                System.out.println("Content=" + smsSendDetailDTO.getContent());
//                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
//                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
//                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
//                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
//                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
//                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
//                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
//            }
//            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
//            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
//        }

    }
}
