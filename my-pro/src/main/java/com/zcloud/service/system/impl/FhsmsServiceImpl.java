package com.zcloud.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.FhsmsMapper;
import com.zcloud.service.system.FhsmsService;

/**
 * 说明：站内信服务接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Service
@Transactional //开启事物
public class FhsmsServiceImpl implements FhsmsService {
	
	@Autowired
	private FhsmsMapper fhsmsMapper;

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		fhsmsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		fhsmsMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		fhsmsMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return fhsmsMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return fhsmsMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return fhsmsMapper.findById(pd);
	}
	
	/**获取未读总数
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFhsmsCount(String USERNAME)throws Exception{
		return fhsmsMapper.findFhsmsCount(USERNAME);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		fhsmsMapper.deleteAll(ArrayDATA_IDS);
	}

}
