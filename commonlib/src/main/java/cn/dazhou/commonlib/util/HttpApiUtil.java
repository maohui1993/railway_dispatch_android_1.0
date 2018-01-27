package cn.dazhou.commonlib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by lenovo on 2017/9/8.
 */

public class HttpApiUtil {
    public static Map<String, Object> map = new HashMap();
    /**
     * @param baseUrl 请以"/"结尾
     * @param api
     * @return
     */
    public static <T> T getInstance(String baseUrl, Class<T> api) {
        if (map.containsKey(api.getSimpleName())) {
            return (T) map.get(api.getSimpleName());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        T t =retrofit.create(api);
        if (t != null) {
            map.put(api.getSimpleName(), t);
        }
        return t;
    }

    public static <T> T getInstanceWithLog(String baseUrl, Class<T> api) {
        if (map.containsKey(api.getSimpleName())) {
            return (T) map.get(api.getSimpleName());
        }
        // 声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // 设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 自定义OkHttpClient
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        if(url.toString().contains("login?")) {
                            return new ArrayList();
                        }
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        T t =retrofit.create(api);
        if (t != null) {
            map.put(api.getSimpleName(), t);
        }
        return t;
    }

    public static <T> T getInstance(String baseUrl, Class<T> api, OkHttpClient okHttpClient) {
        if (map.containsKey(api.getSimpleName())) {
            return (T) map.get(api.getSimpleName());
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        T t =retrofit.create(api);
        if (t != null) {
            map.put(api.getSimpleName(), t);
        }
        return t;
    }
}
