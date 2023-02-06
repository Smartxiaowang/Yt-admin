package com.zcloud.mapper.datasource.act;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/** 
 * 说明： 流程管理Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface ProcdefMapper{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
}

