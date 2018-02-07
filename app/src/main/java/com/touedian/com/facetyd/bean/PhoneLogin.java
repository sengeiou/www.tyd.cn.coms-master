package com.touedian.com.facetyd.bean;

/**
 * Created by Administrator on 2018/newhomepageone/26.
 */

//登录成功
public class PhoneLogin {


    /**
     * identity_status : 0
     * mobile : 13935225970
     * msg : 登录成功
     * status : newhomepageone
     * uid : 17
     * username : 13935225970
     */

    private int identity_status;
    private String mobile;
    private String msg;
    private int status;
    private String uid;
    private String username;
    /**
     * avatar : http://face.touedian.com/uploadfile/20180202/1517573277.jpg
     * end_time : 20260624
     * identity_card : 140202199601112039
     * start_time : 20160624
     * truename : 韩文瑞
     */

    private String avatar;
    private String end_time;
    private String identity_card;
    private String start_time;
    private String truename;

    public int getIdentity_status() {
        return identity_status;
    }

    public void setIdentity_status(int identity_status) {
        this.identity_status = identity_status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getIdentity_card() {
        return identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }
}
