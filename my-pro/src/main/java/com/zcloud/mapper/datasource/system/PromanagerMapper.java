package com.zcloud.mapper.datasource.system;

import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PromanagerMapper {
        /**项目列表(全部)
         * @return
         * @throws Exception
         */
        List<PageData> listPage(Page page);

        /**修改项目
         * @param pd
         * @throws Exception
         */
        void editProject(PageData pd);
        /**新增项目
         * @param pd
         * @throws Exception
         */
        void addProject(PageData pd)throws Exception;
       /**删除项目
        * @param pd
        * @throws Exception
        */
       void deleteProject(PageData pd);
      /**批量删除项目
      * @param ids
      * @throws Exception
      */
      void deleteAllProject(String[] ids);


    PageData findByCode(PageData pd);
}
