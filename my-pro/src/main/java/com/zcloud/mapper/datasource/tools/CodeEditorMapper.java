package com.zcloud.mapper.datasource.tools;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/**
 * 说明：代码编辑器Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface CodeEditorMapper{

	/**新增
	 * @param pd
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 */
	List<PageData> datalistPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
}

