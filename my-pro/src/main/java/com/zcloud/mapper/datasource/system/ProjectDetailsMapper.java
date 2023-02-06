package com.zcloud.mapper.datasource.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProjectDetailsMapper {

    /**项目详情列表(全部)
     * @return
     * @throws Exception
     * @param pd
     */
    List<PageData> listPage(Page pd) throws Exception;

    /**新增详情列表(全部)
     * @return
     * @throws Exception
     */
    void add(PageData page);

    /**删除详情列表(全部)
     * @return
     * @throws Exception
     */
    void delete(PageData page);

    /**修改详情列表(全部)
     * @return
     * @throws Exception
     */
    void edit(PageData page);

    void deleteAll(String[] ids);

    PageData findUpdate(PageData pd);
}
