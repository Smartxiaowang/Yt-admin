package com.zcloud.service.act;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/** 
 * 说明： 模型管理接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface FHModelService{
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
}

