package com.zcloud.service.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

import java.util.List;

public interface DailyManagerService {

    /**日报列表(全部)
     * @return
     * @throws Exception
     */
    List<PageData> listPage(Page page)throws Exception;

    /**日报列表(当天)
     * @return
     * @throws Exception
     */
    List<PageData> dayListPage(Page page)throws Exception;

    /**添加日报
     * @return
     * @throws Exception
     */
    void add(PageData page) throws Exception;

    void delete(PageData pd);

    void edit(PageData pd);

    void approved(PageData pd) throws Exception;

    void approvedall(String[] ids)throws Exception;
}
