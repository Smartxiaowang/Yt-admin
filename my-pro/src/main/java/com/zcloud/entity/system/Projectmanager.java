package com.zcloud.entity.system;

/**
 * 说明：项目管理类
 * 作者：yangming
 * 官网：www.qdkjchina.com
 */
public class Projectmanager {
    private String PROJECT_ID;		//项目ID
    private String PROJECT_NAME;		//项目ID
    private String PROJECT_DESCRIPTION;	//项目描述
    private String PROJECT_TYPE; 	//项目类型
    private String PROJECT_PHASE;   //项目阶段
    private String PROJECT_LEADER;		//项目经理
    private String TECHNICAL_LEADER;    //技术经理
    private String PARTICIPANT;     //参与者
    private String STATE;           //状态
    private String SVN_ADDRESS;		//SVN地址
    private int ISDELETE;	//是否删除
    private String CREATOR;	//添加人
    private String CREATTIME;			//创建时间
    private String OPERATOR;		//修改人
    private String OPERATTIME;			//修改时间

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }

    public String getPROJECT_DESCRIPTION() {
        return PROJECT_DESCRIPTION;
    }

    public void setPROJACT_DESCRIPTION(String PROJACT_DESCRIPTION) {
        this.PROJECT_DESCRIPTION = PROJACT_DESCRIPTION;
    }

    public String getPROJACT_TYPE() {
        return PROJECT_TYPE;
    }

    public void setPROJACT_TYPE(String PROJACT_TYPE) {
        this.PROJECT_TYPE = PROJACT_TYPE;
    }

    public String getPROJECT_PHASE() {
        return PROJECT_PHASE;
    }

    public void setPROJECT_PHASE(String PROJECT_PHASE) {
        this.PROJECT_PHASE = PROJECT_PHASE;
    }

    public String getPROJACT_LEADER() {
        return PROJECT_LEADER;
    }

    public void setPROJACT_LEADER(String PROJACT_LEADER) {
        this.PROJECT_LEADER = PROJACT_LEADER;
    }

    public String getTECHNICAL_LEADER() {
        return TECHNICAL_LEADER;
    }

    public void setTECHNICAL_LEADER(String TECHNICAL_LEADER) {
        this.TECHNICAL_LEADER = TECHNICAL_LEADER;
    }

    public String getPARTICIPANT() {
        return PARTICIPANT;
    }

    public void setPARTICIPANT(String PARTICIPANT) {
        this.PARTICIPANT = PARTICIPANT;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getSVN_ADDRESS() {
        return SVN_ADDRESS;
    }

    public void setSVN_ADDRESS(String SVN_ADDRESS) {
        this.SVN_ADDRESS = SVN_ADDRESS;
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
