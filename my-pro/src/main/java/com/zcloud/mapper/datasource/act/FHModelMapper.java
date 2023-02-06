package com.zcloud.mapper.datasource.act;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/** 
 * 说明： 模型管理Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface FHModelMapper{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
}

