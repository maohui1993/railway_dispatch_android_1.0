/*
 * Copyright (c) 2017. Hooyee@DaZhou
 * author                             bug                              date                               comment
 * Hooyee - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -2017/11/20 - - - - - - - - - - - - - - -init
 */

package cn.dazhou.commonlib.bean;

/**
 * @author Hooyee on 2017/11/20.
 */

public class CheckPojo {
    private CheckModel obj;
    private boolean success;
    private String msg;

    public CheckModel getObj() {
        return obj;
    }

    public void setObj(CheckModel obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
