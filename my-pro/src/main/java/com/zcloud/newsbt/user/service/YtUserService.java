package com.zcloud.newsbt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcloud.newsbt.until.BaseService;
import com.zcloud.newsbt.until.LogOperation;
import com.zcloud.newsbt.until.PageData;
import com.zcloud.newsbt.until.Result;
import com.zcloud.newsbt.user.entity.AppUserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName YtUserServiceImpl
 * @Description TODO
 * @Author Dear lin
 * @Date 16:46 2023/2/6/006
 * @Version 1.0
 **/
public interface YtUserService extends BaseService<AppUserEntity> {

    public PageData<AppUserEntity> getSysUserDataList(Map<String, Object> params);
    public Result updSysUserInfo(@RequestBody AppUserEntity appUserEntity);

    public Result insertSysUserInfo(@RequestBody AppUserEntity appUserEntity);

    public Result delSysUserByIds(@RequestBody String ids[]);
}
