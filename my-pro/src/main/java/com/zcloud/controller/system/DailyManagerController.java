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
@RequestMapping("/daily")
public class DailyManagerController extends BaseController {

    @Autowired
    private DailyManagerService dailyManagerService;

    @RequestMapping("/add")
    @ResponseBody
    public Object addDaily() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("DAILY_ID", this.get32UUID());
        pd.put("EMPLOYEE", Jurisdiction.getUsername());//谁填写的
        pd.put("DATETIME",pd.getString("ADDDATETIME"));
        pd.put("CREATOR", Jurisdiction.getUsername());	//添加人
        pd.put("CREATTIME", DateUtil.date2Str(new Date()));	//添加时间
        pd.put("OPERATOR", Jurisdiction.getUsername());	//修改人
        pd.put("CREAT_USER_ID", Jurisdiction.getUSER_ID());	//添加人
        pd.put("OPERATTIME", DateUtil.date2Str(new Date()));	//修改时间
        pd.put("ISDELETE",0);//是否删除 1-是  0-否
        String datetime = "        ";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        pd.put("DAYOFWEEK",weekDays[w]);
        dailyManagerService.add(pd);
        map.put("result",errInfo);
        return map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        dailyManagerService.delete(pd);
        map.put("result",errInfo);
        return map;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Page page) throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        dailyManagerService.edit(pd);
        map.put("result",errInfo) ;
        return map;
    }

    @RequestMapping("/listAll")
    @ResponseBody
    public Object listAll(Page page)throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> list =  dailyManagerService.listPage(page);
        map.put("result",errInfo);
        map.put("dailyList",list);
        map.put("page",page);
        return map;
    }
}
