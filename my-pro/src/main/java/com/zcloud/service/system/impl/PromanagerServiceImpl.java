package com.zcloud.service.system.impl;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.PromanagerMapper;
import com.zcloud.service.system.PromanagrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional //开启事物
public class PromanagerServiceImpl implements PromanagrService {
    @Autowired
    private PromanagerMapper promanagerMapper;
    /**项目管理列表(全部)
     * @return
     * @throws Exception
     */
    public List<PageData> listAllProjact(Page page)throws Exception{
        return promanagerMapper.listPage(page);
    }
    /**修改项目
     * @return
     * @throws Exception
     */
    @Override
    public void editProject(PageData pd) throws Exception {
        promanagerMapper.editProject(pd);
    }

    @Override
    public void addProject(PageData pd) throws Exception {
        promanagerMapper.addProject(pd);
    }

    @Override
    public void deleteProject(PageData pd) throws Exception {
        promanagerMapper.deleteProject(pd);
    }

    @Override
    public void deleteAllProject(String[] ids) throws Exception {
        promanagerMapper.deleteAllProject(ids);
    }

    @Override
    public PageData findByCode(PageData pd)throws Exception{
        return promanagerMapper.findByCode(pd);
    }
}
