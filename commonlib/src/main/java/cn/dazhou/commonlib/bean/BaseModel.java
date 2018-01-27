/*
 * Copyright (c) 2017. Hooyee@DaZhou
 * author                             bug                              date                               comment
 * Hooyee - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -2017/11/27 - - - - - - - - - - - - - - -init
 */

package cn.dazhou.commonlib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Hooyee on 2017/11/27.
 */

public class BaseModel<T> {
    private T obj;
    private boolean success;
    private String msg;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
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


    public static class BaseObj<T> implements Serializable {
        private List<T> datalist;
        private String  totalCount;
        private String currentPage;
        private String pageSize;

        public List<T> getDatalist() {
            return datalist;
        }

        public void setDatalist(List<T> datalist) {
            this.datalist = datalist;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }
    }
}
