package com.zcloud.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.DatajurMapper;
import com.zcloud.service.system.DatajurService;

/** 
 * 说明： 组织数据权限表
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Service(value="datajurService")
@Transactional //开启事物
public class DatajurServiceImpl implements DatajurService{
	
	@Autowired
	private DatajurMapper datajurMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		datajurMapper.save( pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		datajurMapper.edit(pd);
	}
	
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)datajurMapper.findById(pd);
	}
	
	/**取出某用户的组织数据权限
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDEPARTMENT_IDS(String USERNAME)throws Exception{
		return (PageData)datajurMapper.getDEPARTMENT_IDS(USERNAME);
	}
	
}

