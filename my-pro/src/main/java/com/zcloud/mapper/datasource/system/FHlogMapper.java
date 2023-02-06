package com.zcloud.mapper.datasource.system;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/**
 * 说明：操作日志记录Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface FHlogMapper {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);

}
