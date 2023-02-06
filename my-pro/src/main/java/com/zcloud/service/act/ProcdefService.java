package com.zcloud.service.act;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/** 
 * 说明： 流程管理接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface ProcdefService{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
}

