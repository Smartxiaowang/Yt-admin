package com.zcloud.controller.system;

import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.service.system.DailyManagerService;
import com.zcloud.util.DateUtil;
import com.zcloud.util.Jurisdiction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@ResponseBody
@RequestMapping("/weeklyexamine")
public class WeeklyExamineController extends BaseController {

    @Autowired
    private DailyManagerService dailyManagerService;

    @RequestMapping("/listAll")
    @ResponseBody
    public Object listAll(Page page)throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> list =  dailyManagerService.dayListPage(page);
        map.put("result",errInfo);
        map.put("dailyList",list);
        map.put("page",page);
        return map;
    }

    //通过一条数据
    @RequestMapping("/approved")
    @ResponseBody
    public Object approved(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        dailyManagerService.approved(pd);
        map.put("result",errInfo) ;
        return map;
    }


    //通过所有数据
    @RequestMapping("/approvedall")
    @ResponseBody
    public Object approvedAll(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String Arry[] =  pd.getString("DAILY_ID").split(",");
        dailyManagerService.approvedall(Arry);
        map.put("result",errInfo) ;
        return map;
    }

    //驳回一条数据
    @RequestMapping("/reject")
    @ResponseBody
    public Object reject(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        dailyManagerService.approved(pd);
        map.put("result",errInfo) ;
        return map;
    }
}
