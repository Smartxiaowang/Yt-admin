package com.bdh.service.bus.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bdh.entity.Page;
import com.bdh.entity.PageData;
import com.bdh.mapper.datasource.bus.GoodsInfoMapper;
import com.bdh.service.bus.GoodsInfoService;

/** 
 * 说明：商品信息
 * 作者：shaojie
 * 时间：2020-10-25
 * 官网：www.qdkjchina.com
 */
@Service
@Transactional //开启事物
public class GoodsInfoServiceImpl implements GoodsInfoService{

	@Autowired
	private GoodsInfoMapper goodsinfoMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		goodsinfoMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		goodsinfoMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		goodsinfoMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return goodsinfoMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return goodsinfoMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return goodsinfoMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		goodsinfoMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

