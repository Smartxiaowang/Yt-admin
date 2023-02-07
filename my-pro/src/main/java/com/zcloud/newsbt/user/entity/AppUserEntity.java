package com.zcloud.newsbt.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AppUserEntity
 * @Description TODO
 * @Author Dear lin
 * @Date 8:55 2023/2/7/007
 * @Version 1.0
 **/
@Data
@TableName("ty_appuser")
public class AppUserEntity implements Serializable {

    @TableId(value = "APP_USER_ID",type = IdType.AUTO )
    private Long APP_USER_ID;
    private String APP_USER_NAME;
    private String APP_USER_PASSWORD;
    private String SALT;
    private String APP_USER_CREATTIME;
    private int ROLE;
    private int STATE;
    private String LAST_LOGIN_IP;
    private String LAST_LOGIN_TIME;
    private int GITF;
    private String EXPIRATION_TIME;
    private String LIFE;


}
