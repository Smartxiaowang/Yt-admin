package com.zcloud.service.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

import java.util.List;

public interface ProjectDetailsService {

    /**新增项目详情列表
     * @return
     * @throws Exception
     */
    public void add(PageData page)throws Exception;

    /**删除项目详情
     * @return
     * @throws Exception
     */
    public void delete(PageData page)throws Exception;

    /**删除多个项目详情
     * @return
     * @throws Exception
     */
    public void deleteAll(String[] ids) throws Exception;

    /**修改项目详情列表
     * @return
     * @throws Exception
     */
    public void edit(PageData page)throws Exception;

    /**项目详情列表(全部)
     * @return
     * @throws Exception
     */
    public List<PageData> listPage(Page pd)throws Exception;


    public PageData findUpdate(PageData pd)throws Exception;
}
