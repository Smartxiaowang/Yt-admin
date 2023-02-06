package com.zcloud.controller.system;

import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.service.system.ProjectDetailsService;
import com.zcloud.util.DateUtil;
import com.zcloud.util.Jurisdiction;
import com.zcloud.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projectDetails")
@ResponseBody
public class ProjectDetailsController extends BaseController {

    @Autowired
    private ProjectDetailsService projectDetailsService;

    @RequestMapping("/add")
    @ResponseBody
    public Object add() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("DETAILS_ID", this.get32UUID());
        pd.put("CREATOR", Jurisdiction.getUsername());	//添加人
        pd.put("CREATTIME", DateUtil.date2Str(new Date()));	//添加时间
        pd.put("OPERATOR", Jurisdiction.getUsername());	//修改人
        pd.put("CREAT_USER_ID", Jurisdiction.getUSER_ID());	//添加人
        pd.put("OPERATTIME", DateUtil.date2Str(new Date()));	//修改时间
        pd.put("ISDELETE",0);//是否删除 1-是  0-否
        projectDetailsService.add(pd);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("ISDELETE",1);
        projectDetailsService.delete(pd);
        map.put("result",errInfo);
        return map;
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public Object deleteAll() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String ids =  pd.getString("DETAILS_ID");
        if(Tools.notEmpty(ids)){
            String arry[] =  ids.split(",");
            projectDetailsService.deleteAll(arry);
            errInfo = "success";
        }else{
            errInfo ="error";
        }
        map.put("result",errInfo);
        return map;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Object edit() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("OPERATOR",Jurisdiction.getUsername());
        pd.put("OPERATTIME",DateUtil.date2Str(new Date()));
        projectDetailsService.edit(pd);
        map.put("result",errInfo);
        return map;
    }

    @RequestMapping("/listAll")
    @ResponseBody
    public Object listAll(Page page) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = getPageData();
        pd.put("PROJECT_ID",pd.getString("PROJECT_ID"));
        pd.put("LEADER",pd.getString("LEADERSEARCH"));
        page.setPd(pd);
        List<PageData> list =  projectDetailsService.listPage(page);
        map.put("DetailsList", list);
        map.put("result", errInfo);
        map.put("page",page);
        return map;
    }

    @RequestMapping("/findUpdate")
    @ResponseBody
    public Object findUpdate(String code,String type) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd.put("CODE",code);
        pd.put("TYPE",type);
        pd =  projectDetailsService.findUpdate(pd);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

}
