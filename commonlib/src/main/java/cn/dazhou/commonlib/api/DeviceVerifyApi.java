package cn.dazhou.commonlib.api;

import java.util.Map;

import cn.dazhou.commonlib.bean.HardwareAuth;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Hooyee on 2017/11/17.
 */

public interface DeviceVerifyApi {
    @FormUrlEncoded
    @POST("HardwareAuth/checkMachineValid")
    Call<Object> verifyDevice(@Field("checkMachineValid") HardwareAuth info);

    @FormUrlEncoded
    @POST("HardwareAuth/checkMachineValid")
    Call<Object> verifyDevice(@Field("checkMachineValid") String info);

    @FormUrlEncoded
    @POST("HardwareAuth/checkMachineValid")
    Call<Object> verifyDevice(@FieldMap Map<String, HardwareAuth> info);

//    @POST("HardwareAuth")
//    Call<Object> verifyDevice(@Body HardwareAuth info);
}
