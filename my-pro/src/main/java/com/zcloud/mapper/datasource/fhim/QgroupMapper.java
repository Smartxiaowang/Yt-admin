package com.zcloud.mapper.datasource.fhim;

import java.util.List;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

/**
 * 说明：群组Mapper
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface QgroupMapper {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**群检索列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> searchListAll(PageData pd)throws Exception;
	
	/**我在的全部群列表 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> mylistAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	public void delTp(PageData pd)throws Exception;

}
