package com.zcloud.entity.system;

public class ProjectDetails {
    private String  DETAILS_ID;          //项目详情ID
    private String PROJECT_ID;           //projectmanager表的ID
    private String VERSION_NAME;         //版本名称
    private String UPLOAD_CONTENT;      //维护内容
    private String UPDATE_TIME;         //更新时间
    private String PLAN_START_TIME;     //计划开始时间
    private String PLAN_END_TIME;       //计划结束时间
    private String LEADER;              //负责人
    private String VERSION;             //项目版本号
    private String SVN_ADDRESS;         //SVN地址
    private String REMARKS;             //备注
    private int ISDELETE;	            //是否删除
    private String CREATOR;	            //添加人
    private String CREATTIME;			//创建时间
    private String OPERATOR;		    //修改人
    private String OPERATTIME;			//修改时间

    public String getDETAILS_ID() {
        return DETAILS_ID;
    }

    public void setDETAILS_ID(String DETAILS_ID) {
        this.DETAILS_ID = DETAILS_ID;
    }

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public String getVERSION_NAME() {
        return VERSION_NAME;
    }

    public void setVERSION_NAME(String VERSION_NAME) {
        this.VERSION_NAME = VERSION_NAME;
    }

    public String getUPLOAD_CONTENT() {
        return UPLOAD_CONTENT;
    }

    public void setUPLOAD_CONTENT(String UPLOAD_CONTENT) {
        this.UPLOAD_CONTENT = UPLOAD_CONTENT;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public String getPLAN_START_TIME() {
        return PLAN_START_TIME;
    }

    public void setPLAN_START_TIME(String PLAN_START_TIME) {
        this.PLAN_START_TIME = PLAN_START_TIME;
    }

    public String getPLAN_END_TIME() {
        return PLAN_END_TIME;
    }

    public void setPLAN_END_TIME(String PLAN_END_TIME) {
        this.PLAN_END_TIME = PLAN_END_TIME;
    }

    public String getLEADER() {
        return LEADER;
    }

    public void setLEADER(String LEADER) {
        this.LEADER = LEADER;
    }

    public String getVERSION() {
        return VERSION;
    }

    public void setVERSION(String VERSION) {
        this.VERSION = VERSION;
    }

    public String getSVN_ADDRESS() {
        return SVN_ADDRESS;
    }

    public void setSVN_ADDRESS(String SVN_ADDRESS) {
        this.SVN_ADDRESS = SVN_ADDRESS;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
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
