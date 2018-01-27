package cn.dazhou.commonlib.api;

import cn.dazhou.commonlib.bean.FileBean;

/**
 * Created by Hooyee on 2017/11/15.
 */

public interface OnUploadListener {
    void onSuccess();
    void onUpdate(FileBean bean);
}
