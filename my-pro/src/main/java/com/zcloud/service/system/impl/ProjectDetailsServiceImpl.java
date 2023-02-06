package com.zcloud.service.system.impl;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.ProjectDetailsMapper;
import com.zcloud.service.system.ProjectDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //开启事物
public class ProjectDetailsServiceImpl implements ProjectDetailsService {

    @Autowired
    private ProjectDetailsMapper projectDetailsMapper;

    @Override
    /**项目详情列表(全部)
     * @return
     * @throws Exception
     */
    public List<PageData> listPage(Page pd) throws Exception {
        return projectDetailsMapper.listPage(pd);
    }

    @Override
    public void add(PageData page) throws Exception {
        projectDetailsMapper.add(page);
    }

    @Override
    public void delete(PageData page) throws Exception {
        projectDetailsMapper.delete(page);
    }

    @Override
    public void deleteAll(String[] ids) throws Exception {
        projectDetailsMapper.deleteAll(ids);
    }

    @Override
    public void edit(PageData page) throws Exception {
        projectDetailsMapper.edit(page);
    }

    @Override
    public PageData findUpdate(PageData pd)throws Exception{
        return projectDetailsMapper.findUpdate(pd);
    }
}
