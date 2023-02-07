package com.zcloud.newsbt.user.controller;

import com.zcloud.newsbt.until.LogOperation;
import com.zcloud.newsbt.until.PageData;
import com.zcloud.newsbt.until.Result;
import com.zcloud.newsbt.user.entity.AppUserEntity;
import com.zcloud.newsbt.user.service.YtUserService;
import com.zcloud.newsbt.user.service.serviceImpl.YtUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName YtUserController
 * @Description 升级spring boot用户管理
 * @Author Dear lin
 * @Date 16:45 2023/2/6/006
 * @Version 1.0
 **/
@RestController
@RequestMapping("/ytuser")
//接口文档swagger  待定
public class YtUserController {
    @Resource
    private YtUserService ytUserService;
    //系统用户

    @GetMapping("/getSysUserDataList")
    @LogOperation("系统用户查询")
    public Result<PageData> getSysUserDataList() {

        return null;
    }
    @PutMapping("/updSysUserInfo")
    public Result updSysUserInfo(@RequestBody AppUserEntity appUserEntity) {
        return new Result();
    }
    @PostMapping("/insertSysUserInfo")
    public Result insertSysUserInfo(@RequestBody AppUserEntity appUserEntity) {
        return new Result();
    }
    @DeleteMapping("/delSysUserByIds")
    public Result delSysUserByIds(@RequestBody String ids[]) {
        return new Result();
    }

    //app用户
    public void getAppUserDataList() {}
    public void updAppUserInfo() {}
    public void insertAppUserInfo() {}
    public void delAppUserByIds() {}
}
