package com.zcloud.service.system;

import com.zcloud.entity.PageData;

/**
 * 说明：头像编辑服务接口
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public interface PhotoService {
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
}
