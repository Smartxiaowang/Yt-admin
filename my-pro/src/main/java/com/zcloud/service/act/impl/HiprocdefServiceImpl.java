package com.zcloud.service.act.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.act.HiprocdefMapper;
import com.zcloud.service.act.HiprocdefService;

/** 
 * 说明： 历史流程任务接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Service(value="hiprocdefServiceImpl")
@Transactional //开启事物
public class HiprocdefServiceImpl implements HiprocdefService {

	@Autowired
	private HiprocdefMapper hiprocdefMapper;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		return hiprocdefMapper.datalistPage(page);
	}

	/**历史流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hivarList(PageData pd) throws Exception {
		return hiprocdefMapper.hivarList(pd);
	}

}
