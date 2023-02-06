package com.zcloud.mapper.datasource.system;

import com.zcloud.entity.PageData;

/** 
 * 说明： 富文本编辑器内容缓存Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface UeditorMapper{
	
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
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
}

