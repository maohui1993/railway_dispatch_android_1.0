package cn.dazhou.commonlib.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.dazhou.commonlib.R;
import cn.dazhou.commonlib.api.HttpApi;
import cn.dazhou.commonlib.bean.BaseModel;
import cn.dazhou.commonlib.bean.SysAppVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hooyee on 2017/11/1.
 */

public class ServerUtil {
    public static List<VersionListener> sListeners;

    /**
     *
     * @param context
     * @param href
     * @param downloadHelper
     * @param root 用于展示progressBar的viewgroup
     * @param forceUpdate 是否强制更新
     */
    public static void checkVersion(final Context context, String baseUrl, String href, final DownloadHelper downloadHelper, final ViewGroup root, final boolean forceUpdate) {

        HttpApi api = HttpApiUtil.getInstance(baseUrl, HttpApi.class);
        Call<BaseModel<SysAppVersion>> call = api.getVersion(href);
        final View v = WidgetUtil.createProgressBar(context, root, true);

        call.enqueue(new Callback<BaseModel<SysAppVersion>>() {
            @Override
            public void onResponse(Call<BaseModel<SysAppVersion>> call, final Response<BaseModel<SysAppVersion>> response) {
                Log.i("down", "suc");
                if (root != null) {
                    root.removeView(v);
                }
                if (response.body().getObj() == null) {
                    Util.toast(context, context.getResources().getString(R.string.tip_newest_version));
                    return;
                }
                String versionCode = response.body().getObj().getVersioncode();
                if(versionCode == null) {
                    return;
                }
                if(ActivityUtils.getVersionCode(context) >= Long.valueOf(versionCode)) {
                    Util.toast(context, context.getResources().getString(R.string.tip_newest_version));
                    return;
                }

                final String filename = versionCode + ".apk";
                if (forceUpdate) {
                    AlertDialog.Builder dialogBuilder = WidgetUtil.showDialog(context, context.getResources().getString(R.string.version_force_update));
                    dialogBuilder.setNegativeButton(null, null);
                    dialogBuilder.setPositiveButton(context.getResources().getString(R.string.sure), null).show();
                    downloadHelper.down(response.body().getObj().getDownloaddir(), FileUtil.FILE_SAVE_TO_LOCAL_PATH + filename, "bytes=0-", 0, false);
                } else {
                    String content = response.body().getObj().getAppname() + "有新版本： " + response.body().getObj().getVersionname() + "\n" + "请问是否需要更新？";
                    AlertDialog.Builder dialogBuilder = WidgetUtil.showDialog(context, content);
                    dialogBuilder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadHelper.down(response.body().getObj().getDownloaddir(), FileUtil.FILE_SAVE_TO_LOCAL_PATH + filename, "bytes=0-", 0, true);
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<BaseModel<SysAppVersion>> call, Throwable t) {
                root.removeView(v);
            }
        });
    }

    public static void checkVersion(final Context context, String baseUrl, String href, final DownloadHelper downloadHelper, final ViewGroup root) {
        checkVersion(context, baseUrl, href, downloadHelper, root, false);
    }

    public static void checkVersion(final Context context, String baseUrl, String href) {
        HttpApi api = HttpApiUtil.getInstance(baseUrl, HttpApi.class);
        Call<BaseModel<SysAppVersion>> call = api.getVersion(href);

        call.enqueue(new Callback<BaseModel<SysAppVersion>>() {
            @Override
            public void onResponse(Call<BaseModel<SysAppVersion>> call, final Response<BaseModel<SysAppVersion>> response) {
                if (response.body() == null || response.body().getObj() == null) {
//                    pushVersion(false);
                    return;
                }
                String versionCode = response.body().getObj().getVersioncode();
                String status = response.body().getObj().getUpdatetype();
                if(versionCode == null) {
                    return;
                }
                if(ActivityUtils.getVersionCode(context) >= Long.valueOf(versionCode)) {
//                    pushVersion(false);
                    return;
                }
                // 检测到新版本
                pushVersion(true, status);
            }

            @Override
            public void onFailure(Call<BaseModel<SysAppVersion>> call, Throwable t) {
//                pushVersion(false);
            }
        });
    }

    public static void register(VersionListener versionListener) {
        if (sListeners == null) {
            sListeners = new ArrayList<>();
        }
        sListeners.add(versionListener);
    }

    public static void unregister(VersionListener versionListener) {
        sListeners.remove(versionListener);
    }

    private static void pushVersion(boolean hasUpdate, String status) {
        for (VersionListener v : sListeners) {
            v.onCheckoutNewVersion(hasUpdate, status);
        }
    }

    public interface VersionListener {
        String FORCE_UPDATE = "1";
        void onCheckoutNewVersion(boolean hasUpdate, String status);
    }
}
