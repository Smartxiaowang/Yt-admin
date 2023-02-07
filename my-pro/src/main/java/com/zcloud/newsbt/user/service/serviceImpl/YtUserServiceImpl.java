package com.zcloud.newsbt.user.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcloud.newsbt.until.IpUtils;
import com.zcloud.newsbt.until.PageData;
import com.zcloud.newsbt.until.Result;
import com.zcloud.newsbt.user.dao.YtUserDao;
import com.zcloud.newsbt.user.entity.AppUserEntity;
import com.zcloud.newsbt.user.service.YtUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @ClassName YtUserServiceImpl
 * @Description TODO
 * @Author Dear lin
 * @Date 16:46 2023/2/6/006
 * @Version 1.0
 **/
@Service
public class YtUserServiceImpl extends ServiceImpl<YtUserDao, AppUserEntity> implements YtUserService  {
    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    @Override
    public Result<PageData> getSysUserDataList() {
        //分页
        return null;
    }

    @Override
    @Transactional
    public Result updSysUserInfo(AppUserEntity appUserEntity) {
        baseMapper.insert(appUserEntity);
        return new Result();
    }

    @Override
    @Transactional
    public Result insertSysUserInfo(AppUserEntity appUserEntity) {
        baseMapper.updateById(appUserEntity);
        return new Result();
    }

    @Override
    @Transactional
    public Result delSysUserByIds(String[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
        return new Result();
    }
}
