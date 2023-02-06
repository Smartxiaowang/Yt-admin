package com.zcloud.mapper.datasource.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeeklyManagerMapper {
    List<PageData> listPage(Page page) throws Exception;

    void addWeekly(PageData pd);

    void edit(PageData pd);
}
