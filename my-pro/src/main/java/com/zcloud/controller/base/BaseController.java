package com.zcloud.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zcloud.entity.PageData;
import com.zcloud.util.UuidUtil;

/**
 * 说明：所有处理类父类  
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public class BaseController {

	/**
	 * new PageData对象
	 * @return
	 */
	public PageData getPageData() {
		return new PageData(this.getRequest());
	}

	/**
	 * 得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}
	
	/**得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		return UuidUtil.get32UUID();
	}
	
}
