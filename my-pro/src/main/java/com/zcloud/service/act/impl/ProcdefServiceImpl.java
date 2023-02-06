package com.zcloud.service.act.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.act.ProcdefMapper;
import com.zcloud.service.act.ProcdefService;

import java.util.List;

/** 
 * 说明： 流程管理接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 * @version
 */
@Service(value="procdefServiceImpl")
@Transactional //开启事物
public class ProcdefServiceImpl implements ProcdefService{

	@Autowired
	private ProcdefMapper procdefMapper;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return procdefMapper.datalistPage(page);
	}
	
}

