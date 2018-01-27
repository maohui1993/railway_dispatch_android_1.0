package cn.dazhou.railway.dispatch.mvp.login;

/**
 * Created by Hooyee on 2018/1/27.
 * mail: hooyee_moly@foxmail.com
 */

public class RegisterInfo {
    private int type;            // 0车， 1站点， 2调度台
    private String date;
    private String trainId;

    public RegisterInfo() {
    }

    public RegisterInfo(int type, String date, String trainId) {
        this.type = type;
        this.date = date;
        this.trainId = trainId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }
}
