package com.zcloud.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.entity.system.Dictionaries;
import com.zcloud.mapper.datasource.system.DictionariesMapper;
import com.zcloud.service.system.DictionariesService;

/**
 * 说明：按钮权限服务接口实现类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Service
@Transactional //开启事物
public class DictionariesServiceImpl implements DictionariesService {
	
	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("dictionaries_list.html?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listSubDictByParentId(String parentId) throws Exception {
		System.out.println(dictionariesMapper.listSubDictByParentId(parentId));
		return dictionariesMapper.listSubDictByParentId(parentId);
	}
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)用于代码生成器引用数据字典
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDictToCreateCode(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("setDID('"+dict.getDICTIONARIES_ID()+"');");
			dict.setSubDict(this.listAllDictToCreateCode(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return dictionariesMapper.datalistPage(page);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> listall(PageData pd)throws Exception{
		return dictionariesMapper.listall(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> getDictTree(PageData pd, List<PageData> dictsList)throws Exception{
		List<PageData>[] arrayDict = this.listAllbyPd(pd,dictsList);
		List<PageData> newDictList = arrayDict[1];
		for(PageData dict : newDictList){
			PageData pdDict = new PageData();
			pdDict.put("PARENT_ID", dict.getString("DICTIONARIES_ID"));
			this.getDictTree(pdDict,arrayDict[0]);
		}
		return arrayDict[0];
	}
	
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> getDictTree(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("dictionaries_list.html?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	/**下拉ztree用
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData>[] listAllbyPd(PageData pd,List<PageData> dictsList) throws Exception {
		List<PageData> dictList = this.listall(pd);
		List<PageData> dictNewList = new ArrayList<PageData>();
		dictNewList.addAll(dictList);
		dictsList.addAll(dictList);
		List<PageData>[] arrayDict = new List[2];
		arrayDict[0] = dictsList;
		arrayDict[1] = dictNewList;
		return arrayDict;
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return dictionariesMapper.findById(pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return dictionariesMapper.findByBianma(pd);
	}

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dictionariesMapper.save(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dictionariesMapper.edit(pd);
	}
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return dictionariesMapper.findFromTbs(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dictionariesMapper.delete(pd);
	}
	
}
