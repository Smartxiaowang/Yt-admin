package com.zcloud.controller.system;

import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.Page;
import com.zcloud.entity.PageData;
import com.zcloud.service.system.DictionariesService;
import com.zcloud.service.system.PromanagrService;
import com.zcloud.util.DateUtil;
import com.zcloud.util.Jurisdiction;
import com.zcloud.util.Tools;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 说明：项目管理处理类
 * 作者：yangming
 * 官网：
 */
@Controller
@RequiresPermissions("project:list")
@RequestMapping("/project")
@Component
public class PromanagerController extends BaseController {

    @Autowired
    private PromanagrService promanagrService;
    @Autowired
    private DictionariesService dictionariesService;


    /**判断编码是否存在
     * @return
     */
    @RequestMapping(value="/findByCode")
    @ResponseBody
    public Object findByCode() throws Exception{
        Map<String,String> map = new HashMap<String,String>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        if(promanagrService.findByCode(pd) != null){
            errInfo = "fail";
        }
        map.put("result", errInfo);				//返回结果
        return map;
    }

    @RequestMapping("/projectlist")
    @ResponseBody
    public Object projectlist(Page page)throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        page.setPd(pd);
        List<PageData> list =  promanagrService.listAllProjact(page);
        map.put("projectList", list);
        map.put("result", errInfo);
        map.put("page",page);
        return  map;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Object editProject() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();

        pd.put("OPERATOR",Jurisdiction.getName());
        pd.put("OPERATTIME",DateUtil.date2Str(new Date()));
        promanagrService.editProject(pd);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping("/add")
    @ResponseBody
    public Object addProject() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        pd.put("PROJECT_ID", this.get32UUID());
        pd.put("CREATOR", Jurisdiction.getUsername());	//添加人
        pd.put("CREATTIME", DateUtil.date2Str(new Date()));	//添加时间
        pd.put("OPERATOR", Jurisdiction.getUsername());	//修改人
        pd.put("CREAT_USER_ID", Jurisdiction.getUSER_ID());	//添加人
        pd.put("OPERATTIME", DateUtil.date2Str(new Date()));	//修改时间
        pd.put("ISDELETE",0);//是否删除 1-是  0-否
        promanagrService.addProject(pd);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteProject() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        promanagrService.deleteProject(pd);
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

    @RequestMapping("/deleteAll")
    @ResponseBody
    public Object deleteAllProject() throws Exception{
        Map<String,Object> map = new HashMap<String,Object>();
        String errInfo = "success";
        PageData pd = new PageData();
        pd = this.getPageData();
        String ids = pd.getString("PROJECT_ID");
        if(Tools.notEmpty(ids)){
            String ArryIds[] =  ids.split(",");
            promanagrService.deleteAllProject(ArryIds);
            errInfo="success";
        }else{
            errInfo="error";
        }
        map.put("pd", pd);
        map.put("result", errInfo);
        return map;
    }

    /*@Scheduled(cron = "0 0 0 * * 1 ")
    public void execute(){
        System.out.println("hello");
    }


    //每隔三秒执行一次任务
    @Scheduled(fixedRate = 3000)
    public void cronJobs(){
        System.out.println("hello");
    }*/

    //实现Spring Boot 的文件下载功能，映射网址为/download
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request,
                               HttpServletResponse response) throws UnsupportedEncodingException {

        // 获取指定目录下的第一个文件
        File scFileDir = new File("E://music_eg");
        File TrxFiles[] = scFileDir.listFiles();
        System.out.println(TrxFiles[0]);
        String fileName = TrxFiles[0].getName(); //下载的文件名

        // 如果文件名不为空，则进行下载
        if (fileName != null) {
            //设置文件路径
            String realPath = "E://music_eg/";
            File file = new File(realPath, fileName);

            // 如果文件名存在，则进行下载
            if (file.exists()) {

                // 配置文件下载
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                // 实现文件下载
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("Download the song successfully!");
                }
                catch (Exception e) {
                    System.out.println("Download the song failed!");
                }
                finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
