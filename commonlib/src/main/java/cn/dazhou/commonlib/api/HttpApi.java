package cn.dazhou.commonlib.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.commonlib.bean.SysAppVersion;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by hooyee on 2017/8/8.
 */

public interface HttpApi {
    /**
     * @param url 相对baseUrl的路径
     * @param key @Query("usercode") key 等价于 get请求里面的 usercode=key
     * @return
     */
    @GET("{url}")
    Call<Object> getData(@Path(value = "url", encoded = true) String url, @Query(value = "usercode", encoded = true) String key);

    @GET("{url}")
    Call<Object> getData(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Call<Object> queryByStation(@Path(value = "url", encoded = true) String url,
                                @Query(value = "stationid", encoded = true) String key,
                                @Query(value = "pageSize") String pageSize,
                                @Query(value = "currentPage") String currentPage);

    @GET("{url}")
    Call<Object> queryUsersByCondition(@Path(value = "url", encoded = true) String url,
                                       @Query(value = "userid") String userid,
                                       @Query(value = "usercode") String usercode,
                                       @Query(value = "ipphone") String ipphone);

    @GET("{url}")
    Call<Object> getData(@Path(value = "url", encoded = true) String url,
                         @Query("userid") String userid,
                         @Query("device") String device,
                         @Query("ip") String ip,
                         @Query("latitude") String latitude,
                         @Query("longitude") String longitude,
                         @Query("mtype") String mtype
    );

    @Streaming
    @GET("{suffix_path}{icon_path}")
    Call<ResponseBody> downFile(@Path(value = "suffix_path", encoded = true) String suffixPath, @Path(value = "icon_path", encoded = true) String iconPath, @Header("Range") String range); //bytes=0-801 //一般请求下载整个文件是bytes=0- 或不用这个头

    @Multipart
    @POST("{url}")
    Call<ResponseBody> upload(@Path(value = "url", encoded = true) String url, @Part List<MultipartBody.Part> file);

    @GET("{url}")
    Call<Object> moveCMD(@Path(value = "url", encoded = true) String url, @Query("DevIDNO") String idno, @Query("Chn") String chn, @Query("Command") String command, @Query("Speed") String speed);

    @Multipart
    @POST("{url}")
    Call<ResponseBody> cutUpload(@Path(value = "url", encoded = true) String url, @Part List<MultipartBody.Part> file);

    @POST("{url}")
    Call<Object> cutUploadSaveSQL(@Path(value = "url", encoded = true) String url,
                                  @Query("uploadTime")String uploadTime,
                                  @Query("chunkno") String chunkno,
                                  @Query("savePath") String savePath,
                                  @Query("fileName") String fileName,
                                  @Query("attachmentsize") String attachmentsize,
                                  @Query("longitude") String longitude,
                                  @Query("latitude") String latitude,
                                  @Query("userId") String userId,
                                  @Query("fromdevice") String fromdevice,
                                  @Query("atype") String atype,
                                  @Query("remark") String remark,
                                  @Query("description") String description,
                                  @Query("stationid") String stationid);

    @POST("{url}")
    Call<Object> reciverMsg(@Path(value = "url", encoded = true) String url,
                            @Query("sender") String sender,
                            @Query("receivetime") Date receivetime,
                            @Query("filename") String filename,
                            @Query("uploadTime") String uploadTime,
                            @Query("attachmentsize") String attachmentsize,
                            @Query("data") String data,
                            @Query("data2") String data2,
                            @Query("smsType") int smsType);

    @GET("{url}")
    Call<BaseModel<SysAppVersion>> getVersion(@Path(value = "url", encoded = true) String url);

    @FormUrlEncoded
    @POST("{url}")
    Call<Object> login(@Path(value = "url", encoded = true) String path, @Query(value = "usercode", encoded = true) String username, @Query(value = "password", encoded = true) String password, @FieldMap Map<String, Object> info);
}
