package com.zcloud.service.act;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/** 
 * 说明： 历史流程任务接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface HiprocdefService {
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd)throws Exception;

}
