package com.zcloud.service.system;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/**
 * 说明：操作日志记录接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface FHlogService {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String USERNAME, String CONTENT)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

}
