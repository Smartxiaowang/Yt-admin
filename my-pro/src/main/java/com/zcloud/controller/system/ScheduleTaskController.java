package com.zcloud.controller.system;

import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.PageData;
import com.zcloud.service.system.WeeklyManagerService;
import com.zcloud.util.DateUtil;
import com.zcloud.util.Jurisdiction;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.tomcat.util.compat.JreCompat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;


/**
 * 说明：周报定时任务新增一条记录
 * 作者：yangming
 * 官网：
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScheduleTaskController extends BaseController {

    static DefaultWebSecurityManager manager = new DefaultWebSecurityManager();

    @Autowired
    private WeeklyManagerService weeklyManagerService;

    @Scheduled(cron = "0 34 0 * * 1")
    public void execute() throws Exception {
        ThreadContext.bind(manager);
        PageData pd = new PageData();
        pd.put("WEEKLY_ID",this.get32UUID());
        pd.put("WEEKLY_HOURS",40);
        Calendar c = Calendar.getInstance();
        int week = c.get(c.WEEK_OF_YEAR);
        pd.put("WHAT_WEEK",week);
        pd.put("NUMBNR_PROJECT","");
        //System.out.println(Jurisdiction.getUsername());
        //pd.put("CREATOR", Jurisdiction.getUsername());	//添加人
        pd.put("CREATTIME", DateUtil.date2Str(new Date()));	//添加时间
        //pd.put("OPERATOR", Jurisdiction.getUsername());	//修改人
        //pd.put("CREAT_USER_ID", Jurisdiction.getUSER_ID());	//添加人
        pd.put("OPERATTIME", DateUtil.date2Str(new Date()));	//修改时间
        pd.put("ISDELETE",0);//是否删除 1-是  0-否
        weeklyManagerService.addWeekly(pd);
    }
}
