package com.zcloud.service.system.impl;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.mapper.datasource.system.WeeklyManagerMapper;
import com.zcloud.service.system.WeeklyManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //开启事物
public class WeeklyManagerServiceImpl implements WeeklyManagerService {

    @Autowired
    private WeeklyManagerMapper weeklyManagerMapper;

    @Override
    public List<PageData> listAllWeekly(Page page) throws Exception {
        return weeklyManagerMapper.listPage(page);
    }

    @Override
    public void addWeekly(PageData pd) throws Exception {
         weeklyManagerMapper.addWeekly(pd);
    }

    @Override
    public void edit(PageData pd) {
        weeklyManagerMapper.edit(pd);
    }
}
