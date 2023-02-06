package com.zcloud.mapper.datasource.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

import java.util.List;

public interface DailyManagerMapper {

    List<PageData> datalistPage(Page page)throws Exception;

    void addDaily(PageData page);

    void delete(PageData pd);

    void edit(PageData pd);

    List<PageData> daylistPage(Page page);

    void approved(PageData pd);

    void approvedall(String[] ids);
}
