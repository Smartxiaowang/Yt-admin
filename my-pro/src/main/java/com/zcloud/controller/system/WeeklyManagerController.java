package com.zcloud.controller.system;

import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.service.system.WeeklyManagerService;
import com.zcloud.util.Tools;
import freemarker.core.StringArraySequence;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 说明：周报管理处理类
 * 作者：yangming
 * 官网：
 */
@Controller
@RequiresPermissions("weekly:list")
@RequestMapping("/weekly")
public class WeeklyManagerController extends BaseController {

    @Autowired
    private WeeklyManagerService weeklyManagerService;

    @RequestMapping("/weeklylist")
    @ResponseBody
    public Object projectlist(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String time[] =  pd.getString("timeslot").split(",");
        if(time[0] =="" ||time[0].equals("")){
        }else{
            String start = time[0];
            String end = time[1];
            pd.put("start",start);
            pd.put("end",end);
        }
        page.setPd(pd);
        List<PageData> list =  weeklyManagerService.listAllWeekly(page);
        map.put("weeklyList", list);
        map.put("result", errInfo);
        map.put("page",page);
        return  map;
    }


    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        weeklyManagerService.edit(pd);
        map.put("result",errInfo);
        return map;
    }


}
