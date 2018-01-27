package cn.dazhou.commonlib.util;

import android.util.Log;

import cn.dazhou.commonlib.api.HttpApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovo on 2017/9/12.
 */

public class CMDUtil {
    public static void sendCMD(String baseUrl, String relativeUrl, String idno, String chn, String command, String speed) {

        HttpApi httpApi = HttpApiUtil.getInstance(baseUrl, HttpApi.class);
        Call call = httpApi.moveCMD(relativeUrl, idno, chn, command, speed);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i("TAG", "success");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("TAG", "fail");
            }
        });

    }
}
