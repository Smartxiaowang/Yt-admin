package com.zcloud.entity.system;

public class DailyManager {
    private String DAILY_ID;        //日报ID
    private String WEEKLY_ID;
    private String DATETIME;        //日期
    private String DAYOFWEEK;       //周几
    private String PROJECT_NAME;    //项目名称
    private String TASK_PHASE;      //任务阶段
    private String JOB_DETAILS;     //工作详情
    private String FILLING_TYPE;    //填报类型
    private int HOURS;      //小时
    private String STATE;   //状态
    private String EMPLOYEE;  //人员
    private int ISDELETE;	//是否删除
    private String CREATOR;	//添加人
    private String CREATTIME;			//创建时间
    private String OPERATOR;		//修改人
    private String OPERATTIME;			//修改时间

    public String getDAILY_ID() {
        return DAILY_ID;
    }

    public void setDAILY_ID(String DAILY_ID) {
        this.DAILY_ID = DAILY_ID;
    }

    public String getWEEKLY_ID() {
        return WEEKLY_ID;
    }

    public void setWEEKLY_ID(String WEEKLY_ID) {
        this.WEEKLY_ID = WEEKLY_ID;
    }

    public String getDATETIME() {
        return DATETIME;
    }

    public void setDATETIME(String DATETIME) {
        this.DATETIME = DATETIME;
    }

    public String getDAYOFWEEK() {
        return DAYOFWEEK;
    }

    public void setDAYOFWEEK(String DAYOFWEEK) {
        this.DAYOFWEEK = DAYOFWEEK;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }

    public String getTASK_PHASE() {
        return TASK_PHASE;
    }

    public void setTASK_PHASE(String TASK_PHASE) {
        this.TASK_PHASE = TASK_PHASE;
    }

    public String getJOB_DETAILS() {
        return JOB_DETAILS;
    }

    public void setJOB_DETAILS(String JOB_DETAILS) {
        this.JOB_DETAILS = JOB_DETAILS;
    }

    public String getFILLING_TYPE() {
        return FILLING_TYPE;
    }

    public void setFILLING_TYPE(String FILLING_TYPE) {
        this.FILLING_TYPE = FILLING_TYPE;
    }

    public int getHOURS() {
        return HOURS;
    }

    public void setHOURS(int HOURS) {
        this.HOURS = HOURS;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getEMPLOYEE() {
        return EMPLOYEE;
    }

    public void setEMPLOYEE(String EMPLOYEE) {
        this.EMPLOYEE = EMPLOYEE;
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
