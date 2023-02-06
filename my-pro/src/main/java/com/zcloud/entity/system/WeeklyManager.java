package com.zcloud.entity.system;

public class WeeklyManager {
    private String WEEKLY_ID;           //ID
    private int WHAT_WEEK;           //第几周
    private String NUMBER_PROJECT;      //参与项目数
    private String WEEKLY_HOURS;        //本周工时
    private String REPORTED_HOURS;      //已报工时
    private int ISDELETE;	            //是否删除
    private String CREATOR;	            //添加人
    private String CREATTIME;			//创建时间
    private String OPERATOR;		    //修改人
    private String OPERATTIME;			//修改时间

    public String getWEEKLY_ID() {
        return WEEKLY_ID;
    }

    public void setWEEKLY_ID(String WEEKLY_ID) {
        this.WEEKLY_ID = WEEKLY_ID;
    }

    public int getWHAT_WEEK() {
        return WHAT_WEEK;
    }

    public void setWHAT_WEEK(int WHAT_WEEK) {
        this.WHAT_WEEK = WHAT_WEEK;
    }

    public String getNUMBER_PROJECT() {
        return NUMBER_PROJECT;
    }

    public void setNUMBER_PROJECT(String NUMBER_PROJECT) {
        this.NUMBER_PROJECT = NUMBER_PROJECT;
    }

    public String getWEEKLY_HOURS() {
        return WEEKLY_HOURS;
    }

    public void setWEEKLY_HOURS(String WEEKLY_HOURS) {
        this.WEEKLY_HOURS = WEEKLY_HOURS;
    }

    public String getREPORTED_HOURS() {
        return REPORTED_HOURS;
    }

    public void setREPORTED_HOURS(String REPORTED_HOURS) {
        this.REPORTED_HOURS = REPORTED_HOURS;
    }

    public int getISDELETE() {
        return ISDELETE;
    }

    public void setISDELETE(int ISDELETE) {
        this.ISDELETE = ISDELETE;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public String getCREATTIME() {
        return CREATTIME;
    }

    public void setCREATTIME(String CREATTIME) {
        this.CREATTIME = CREATTIME;
    }

    public String getOPERATOR() {
        return OPERATOR;
    }

    public void setOPERATOR(String OPERATOR) {
        this.OPERATOR = OPERATOR;
    }

    public String getOPERATTIME() {
        return OPERATTIME;
    }

    public void setOPERATTIME(String OPERATTIME) {
        this.OPERATTIME = OPERATTIME;
    }
}
