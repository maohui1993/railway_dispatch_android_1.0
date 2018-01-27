package cn.dazhou.commonlib.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.dazhou.commonlib.api.DeviceVerifyApi;
import cn.dazhou.commonlib.bean.CheckModel;
import cn.dazhou.commonlib.bean.CheckPojo;
import cn.dazhou.commonlib.bean.HardwareAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Hooyee
 */

public class DeviceInfo {
    @SuppressLint("MissingPermission")
    public static String getDeviceId(Context context) {
        String deviceId = null;
        String d = null;
        if (PermissionUtil.hasPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = tm.getDeviceId();
            if(deviceId == null) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                deviceId = wifiManager.getConnectionInfo().getMacAddress();
            }
        } else {
            Util.toast(context, "缺少对应权限");
        }
        return deviceId;
    }

    public static boolean verify(final Context context) {
        DeviceVerifyApi api = HttpApiUtil.getInstanceWithLog("http://192.168.1.111:8088/", DeviceVerifyApi.class);

        HardwareAuth hardwareAuth = new HardwareAuth();
        hardwareAuth.setCode(getDeviceId(context));
        hardwareAuth.setName("华为");
        hardwareAuth.setHtype("1");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat codeDf = new SimpleDateFormat("yyyy-MM-dd");

        Date d = new Date();
        hardwareAuth.setMachineDate(df.format(d));
        String code = getDeviceId(context) + "#" + codeDf.format(d);
        String enCode = EnDeUtil.getCrc32Str(code.getBytes());
        hardwareAuth.setMachineCheckCode(enCode);


//        Call call = api.verifyDevice(hardwareAuth);
//        Call call = api.verifyDevice("test");
        Map map = new HashMap<String, HardwareAuth>();
        map.put("checkMachineValid", hardwareAuth);

        Call call = api.verifyDevice(map);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

//                Gson gson = new Gson();
//                CheckPojo pojo = gson.fromJson(response.body().toString(), CheckPojo.class);

                String jsonstr = JSON.toJSONString(response.body());
                Type type = new TypeReference<CheckPojo>() {}.getType();
                CheckPojo pojo = JSON.parseObject(jsonstr, type);

                CheckModel model = pojo.getObj();
                if (model.isResult()) {
                    Util.toast(context, "鉴权成功");
                } else {
                    Util.toast(context, model.getMessage());
                }
                Log.e("sdf", "df");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("sdf", "df");
            }
        });
        return true;
    }
}
