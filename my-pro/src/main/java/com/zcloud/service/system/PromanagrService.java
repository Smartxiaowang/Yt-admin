package com.zcloud.service.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 说明：项目管理服务接口
 * 作者：yangming
 * 官网：
 */
public interface PromanagrService {
    /**项目管理列表(全部)
     * @return
     * @throws Exception
     */
     List<PageData> listAllProjact(Page page)throws Exception;
    /**修改项目
     * @param pd
     * @throws Exception
     */
    public void editProject(PageData pd)throws Exception;
    /**新增项目
     * @param pd
     * @throws Exception
     */
    public void addProject(PageData pd)throws Exception;

    /**删除项目
     * @param pd
     * @throws Exception
     */
    public void deleteProject(PageData pd)throws Exception;

    /**批量删除项目
     * @param ids
     * @throws Exception
     */
    public void deleteAllProject(String[] ids)throws Exception;


    public PageData findByCode(PageData pd)throws Exception;
}
