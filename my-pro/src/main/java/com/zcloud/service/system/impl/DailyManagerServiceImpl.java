package com.zcloud.service.system.impl;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.DailyManagerMapper;
import com.zcloud.service.system.DailyManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DailyManagerServiceImpl implements DailyManagerService {

    @Autowired
    private DailyManagerMapper dailyManagerMapper;

    @Override
    public List<PageData> listPage(Page page) throws Exception {
        return dailyManagerMapper.datalistPage(page);
    }

    @Override
    public List<PageData> dayListPage(Page page) throws Exception {
        return dailyManagerMapper.daylistPage(page);
    }

    @Override
    public void add(PageData page) throws Exception {
        dailyManagerMapper.addDaily(page);
    }

    @Override
    public void delete(PageData pd) {
        dailyManagerMapper.delete(pd);
    }

    @Override
    public void edit(PageData pd) {
        dailyManagerMapper.edit(pd);
    }

    @Override
    public void approved(PageData pd) throws Exception {
        dailyManagerMapper.approved(pd);
    }

    @Override
    public void approvedall(String[] ids) {
        dailyManagerMapper.approvedall(ids);
    }
}
