package com.bdh.service.bus.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bdh.entity.Page;
import com.bdh.entity.PageData;
import com.bdh.mapper.datasource.bus.AWARDSMapper;
import com.bdh.service.bus.AWARDSService;

/** 
 * 说明：抽奖规则
 * 作者：shaojie
 * 时间：2020-10-31
 * 官网：www.qdkjchina.com
 */
@Service
@Transactional //开启事物
public class AWARDSServiceImpl implements AWARDSService{

	@Autowired
	private AWARDSMapper awardsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		awardsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		awardsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		awardsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return awardsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return awardsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return awardsMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		awardsMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

