package cn.dazhou.commonlib;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.commonlib.util.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Hooyee on 2017/11/30.
 * @param <T> 服务器获取的json数据转换成T
 * @param <M> onSuccess回调方法中的参数类型
 */
public abstract class BasePresenterSupport<T extends BaseModel, M, V> implements BasePresenter{
    protected Context mContext;
    protected V mView;

    public BasePresenterSupport(Context mContext, V v) {
        this.mContext = mContext;
        this.mView = v;
    }

    /**
     *
     * @param httpApi
     * @param t 获取到服务的json数据被转换成的类型
     * @param requestCode 请求是的标记，用来断定onSuccess和onFailed方法是对应哪次请求
     * @param methodName
     * @param params
     */
    public void http(Object httpApi, final Class<T> t, final int requestCode, String methodName, Pair... params) {
        try {
            final Class clazz = httpApi.getClass();
            Object[] objs = new Object[params.length];
            Class[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = (Class) params[i].first;
                objs[i] = params[i].second;
            }
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            Call call = (Call) method.invoke(httpApi, objs);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    String json = JSON.toJSONString(response.body());
                    BaseModel<M> model = JSON.parseObject(json, t);
                    if (model.isSuccess()) {
                        onSuccess(model.getObj(), requestCode);
                    } else {
                        Util.toast(mContext, model.getMsg());
                        onFailed(requestCode);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Util.toast(mContext, "连接服务器失败");
                    onFailed(requestCode);
                }
            });

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 参数类型默认为String
     * @param httpApi
     * @param methodName
     * @param params
     */
    public void http(Object httpApi, final Class<T> t, final int requestCode, String methodName, String... params) {
        try {
            Class clazz = httpApi.getClass();
            Class[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = String.class;
            }
            Method method = clazz.getDeclaredMethod(methodName, paramTypes);
            Call<Object> call = (Call) method.invoke(httpApi, params);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.i("TAG", "sdf");
                    String json = JSON.toJSONString(response.body());
                    BaseModel<M> model = JSON.parseObject(json, t);
                    if (model.isSuccess()) {
                        onSuccess(model.getObj(), requestCode);
                    } else {
                        Util.toast(mContext, model.getMsg());
                        onFailed(requestCode);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Util.toast(mContext, "连接服务器失败");
                    onFailed(requestCode);
                }
            });

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onSuccess(M m, int requestCode);

    protected abstract void onFailed(int code);
}
