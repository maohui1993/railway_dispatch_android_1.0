/*
 * Copyright (c) 2017. Hooyee@DaZhou
 * author                             bug                              date                               comment
 * Hooyee - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -2017/11/20 - - - - - - - - - - - - - - -init
 */

package cn.dazhou.commonlib.bean;

/**
 * Created by lenovo on 2017/11/14.
 */
public class CheckModel {
    private String message;
    private boolean result;
    private String checkCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
