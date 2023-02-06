package com.zcloud.service.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

import java.util.List;

public interface WeeklyManagerService {

    /**周报列表(全部)
     * @return
     * @throws Exception
     */
    List<PageData> listAllWeekly(Page page)throws Exception;

    /**新增周报
     * @return
     * @throws Exception
     */
    public void addWeekly(PageData pd)throws Exception;

    void edit(PageData pd);
}
