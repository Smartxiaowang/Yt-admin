package com.zcloud.service.fhim.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.fhim.IQgroupMapper;
import com.zcloud.service.fhim.IQgroupService;

/**
 * 说明：我在的群组服务接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Service(value="iQgroupServiceImpl")
@Transactional //开启事物
public class IQgroupServiceImpl implements IQgroupService {
	
	@Autowired
	private IQgroupMapper iQgroupMapper;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		iQgroupMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		iQgroupMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		iQgroupMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception{
		return iQgroupMapper.datalistPage(page);
	}
	
	/**列表(全部群成员)带分页
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> memberslistPage(Page page)throws Exception{
		return iQgroupMapper.memberslistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return iQgroupMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return iQgroupMapper.findById(pd);
	}
	
	/**判断我是否在某群 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByIdandQid(PageData pd)throws Exception{
		return iQgroupMapper.findByIdandQid(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		iQgroupMapper.deleteAll(ArrayDATA_IDS);
	}
	
}
