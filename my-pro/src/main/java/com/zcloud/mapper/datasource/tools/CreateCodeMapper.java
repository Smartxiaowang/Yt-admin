package com.zcloud.mapper.datasource.tools;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/**
 * 说明：代码生成器Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface CreateCodeMapper {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd);
	
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
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**列表(主表)
	 * @param page
	 * @throws Exception
	 */
	List<PageData> listFa();

}
