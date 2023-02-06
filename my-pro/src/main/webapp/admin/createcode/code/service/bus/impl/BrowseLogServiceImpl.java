package com.bdh.service.bus.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bdh.entity.Page;
import com.bdh.entity.PageData;
import com.bdh.mapper.datasource.bus.BrowseLogMapper;
import com.bdh.service.bus.BrowseLogService;

/** 
 * 说明：浏览日志
 * 作者：shaojie
 * 时间：2020-10-27
 * 官网：www.qdkjchina.com
 */
@Service
@Transactional //开启事物
public class BrowseLogServiceImpl implements BrowseLogService{

	@Autowired
	private BrowseLogMapper browselogMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		browselogMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		browselogMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		browselogMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return browselogMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return browselogMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return browselogMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		browselogMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

