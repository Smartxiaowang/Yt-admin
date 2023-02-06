package com.zcloud.service.system;

import com.zcloud.entity.PageData;

/** 
 * 说明： 富文本编辑器内容缓存接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
public interface UeditorService{
	
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
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
}

