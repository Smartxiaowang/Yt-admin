package com.zcloud.newsbt.user.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcloud.newsbt.until.*;
import com.zcloud.newsbt.user.dao.YtUserDao;
import com.zcloud.newsbt.user.entity.AppUserEntity;
import com.zcloud.newsbt.user.service.YtUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName YtUserServiceImpl
 * @Description TODO
 * @Author Dear lin
 * @Date 16:46 2023/2/6/006
 * @Version 1.0
 **/
@Service
public class YtUserServiceImpl extends AbsBasePageServiceImpl<YtUserDao, AppUserEntity> implements YtUserService  {
    private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

    @Override
    public PageData<AppUserEntity> getSysUserDataList(Map<String, Object> params) {
        //分页
        IPage<AppUserEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, AppUserEntity.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updSysUserInfo(AppUserEntity appUserEntity) {
        insert(appUserEntity);
        return new Result();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result insertSysUserInfo(AppUserEntity appUserEntity) {
        updateById(appUserEntity);
        return new Result();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delSysUserByIds(String[] ids) {
        deleteBatchIds(Arrays.asList(ids));
        return new Result();
    }
    private QueryWrapper<AppUserEntity> getWrapper(Map<String, Object> params){
        //再此处做 筛选条件
        QueryWrapper<AppUserEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
