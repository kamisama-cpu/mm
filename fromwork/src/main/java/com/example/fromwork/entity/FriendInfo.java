package com.example.fromwork.entity;

public class FriendInfo {

    private String msg;
    private String userID;
    private String saveTime;


    //状态 -1：待确认 0：同意 1：拒绝
    private String isAgree;


    public FriendInfo(String msg, String userID, String saveTime, String isAgree) {
        this.msg = msg;
        this.userID = userID;
        this.saveTime = saveTime;
        this.isAgree = isAgree;
    }

    public FriendInfo() {
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "msg='" + msg + '\'' +
                ", userID='" + userID + '\'' +
                ", saveTime='" + saveTime + '\'' +
                ", isAgree='" + isAgree + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }
}
