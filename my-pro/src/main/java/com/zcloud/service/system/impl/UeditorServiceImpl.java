package com.zcloud.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.UeditorMapper;
import com.zcloud.service.system.UeditorService;

/** 
 * 说明： 富文本编辑器内容缓存接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
@Service
@Transactional //开启事物
public class UeditorServiceImpl implements UeditorService{

	@Autowired
	private UeditorMapper ueditorMapper;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return ueditorMapper.findById(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		ueditorMapper.edit(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		ueditorMapper.delete(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		ueditorMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

