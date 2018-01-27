package cn.dazhou.commonlib.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

import cn.dazhou.commonlib.R;
import cn.dazhou.commonlib.api.HttpApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Hooyee on 2017/10/30.
 */

public class DownloadHelper {
    private static final String TAG = "DownloadHelper";
    private static final byte CONTINUE = 1;
    private static final byte PAUSE = -2;
    private static final byte CANCEL = -1;
    private static final byte PROCESSING = 2;
    private static final byte IDLE = 0;

    private long mFileSize;
    private Context mContext;
    private Notification mNotification;
    private NotificationManager mManager;

    private String downPath;

    private Class<Activity> mClazz;
    private boolean sendNotification;

    private boolean ringtone;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                float degree = (float) msg.obj;
                updateNotification(degree);
                removeMessages(1);
                Log.i("down", degree + "%");
            } else if (msg.what == 100) {
                updateNotification( 100);
                ringtone = true;
                Log.i("down",  "100%");
            }
        }

        private void updateNotification(float progress) {
            if (!ringtone) {
                mNotification.defaults = Notification.BADGE_ICON_NONE;
            }
            ringtone = false;
            mNotification.contentView.setProgressBar(R.id.progress, 100, (int)progress, false);
            mNotification.contentView.setTextViewText(R.id.tv_degree, progress + "%");

            mManager.notify(1, mNotification);
            removeMessages(1);
        }
    };

    private boolean mPause = false;
    private byte mState = IDLE;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WidgetUtil.NOTIFICATION_ACTION_CANCEL:
                    changeState(CANCEL);
                    break;
                case WidgetUtil.NOTIFICATION_ACTION_PAUSE:
                    changeState(mState == PAUSE ? CONTINUE : PAUSE);
                    break;
            }
        }
    };

    /**
     * 默认发送通知
     * @param mContext
     * @param activityClazz 用来处理通知的点击的activity
     */
    public DownloadHelper(Context mContext, Class activityClazz) {
        this.mContext = mContext;
        mClazz = activityClazz;
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.notification.BUTTON_CLICKED.cancel");
        intentFilter.addAction("action.notification.BUTTON_CLICKED.pause");
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    /**
     *
     * @param downPath 待下载的文件名称
     * @param localFilename 下载后存放在本地的名称
     * @param range 拉取范围
     * @param initSize 已下载大小
     * @param notification 是否发送通知
     * @Param activityClass 用来处理通知的点击事件
     */
    public void down(String downPath, final String localFilename, String range, final long initSize, boolean notification) {
        if (mState != IDLE) {
            Log.d(TAG, "had other task is running");
            return;
        }
        sendNotification = notification;
        changeState(PROCESSING);
        this.downPath = downPath;
        int point = downPath.lastIndexOf("/");
        String baseUrl = String.copyValueOf(downPath.toCharArray(), 0, point + 1);
        String filename = String.copyValueOf(downPath.toCharArray(), point + 1, downPath.length() - point - 1);
        if (notification) {
            mNotification = WidgetUtil.sendNotification(mContext, mClazz);
        }
        HttpApi api = HttpApiUtil.getInstance(baseUrl, HttpApi.class
//                , new OkHttpClient().newBuilder().addNetworkInterceptor(
//                        new Interceptor() {
//                    @Override
//                    public okhttp3.Response intercept(Chain chain) throws IOException {
//                        okhttp3.Response originalResponse = chain.proceed(chain.request());
//                        return originalResponse.newBuilder()
//                                .body(new ProgressResponseBody(originalResponse.body(), new ProgressResponseBody.ProgressListener() {
//                                    byte[] bytes = new byte[8192];
//                                    @Override
//                                    public void onPreExecute(long contentLength) {
//                                        mFileSize = contentLength;
//                                    }
//
//                                    @Override
//                                    public void update(long totalBytes, boolean done) {
//                                        if (done) {
//                                            Log.i("down", "success");
//                                            if (mNotification != null) {
//                                                mNotification.contentView.setProgressBar(R.id.progress, 100, 100, false);
//                                                mNotification.contentView.setTextViewText(R.id.tv_degree, "100.00%");
//                                                mManager.notify(1, mNotification);
//                                            }
//                                        } else {
//                                            mNotification.contentView.setProgressBar(R.id.progress, 100, (int)(totalBytes * 1.0f / mFileSize * 100), false);
//                                            DecimalFormat df = new DecimalFormat("0.00");
//                                            mNotification.contentView.setTextViewText(R.id.tv_degree, df.format(totalBytes * 1.0f / mFileSize * 100) + "%");
//                                            mManager.notify(1, mNotification);
//                                            Log.i("down", totalBytes * 1.0f / mFileSize * 100 + "%");
//                                        }
//                                    }
//
//                                })).build();
//                    }
//                }).build()
        );

        Call<ResponseBody> response = api.downFile("apk/", filename, range);
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                mFileSize = response.body().contentLength() + initSize;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writeToDisk(response.body().byteStream(), initSize, mFileSize, localFilename);
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("TAG", "failed");
            }
        });
    }

    private void installApk(Context context, String filepath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filepath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private boolean writeToDisk(InputStream inputStream, long initSize, long fileSize, String fileFullName) {
        long fileSizeDownloaded = initSize;
        boolean completed = false;
        RandomAccessFile accessFile = null;
        try {
            File file = new File(fileFullName);

            if (!file.getParentFile().exists()) {
                if (file.getParentFile().mkdirs()) {
                    file.createNewFile();
                }
            }
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(initSize);

            byte[] fileReader = new byte[1024 * 8];

            while (true) {
                if (mState == PAUSE) {
                    SharedPreferenceUtil.putLong(mContext, "break_point", fileSizeDownloaded);
                    SharedPreferenceUtil.putLong(mContext, "break_size", fileSize);
                    SharedPreferenceUtil.putString(mContext, "break_local_filename", fileFullName);
                    SharedPreferenceUtil.putString(mContext, "break_down_path", downPath);
                    break;
                }
                if (mState == CANCEL) {
                    file.delete();
                    break;
                }
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    completed = true;
                    break;
                }

                accessFile.write(fileReader, 0, read);
                fileSizeDownloaded += read;

                if (sendNotification) {
                    updateDegree(fileSize, fileSizeDownloaded, 1);
                }

                Log.d("emergency", "file download: " + fileSizeDownloaded + " of " + fileSize + "--->" + read);
            }
            if (completed) {
                if (sendNotification) {
                    updateDegree(fileSizeDownloaded, fileSizeDownloaded, 100);
                }
                installApk(mContext, fileFullName);
                changeState(IDLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (accessFile != null) {
                    accessFile.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    private void updateDegree(long fileSize, long fileSizeDownloaded, int what) {
        Message message = new Message();
        message.what = what;
        DecimalFormat df = new DecimalFormat("0.00");
        float degree = Float.valueOf((df.format(fileSizeDownloaded * 1.0f / fileSize * 100)));
        message.obj = degree;
        mHandler.sendMessageAtFrontOfQueue(message);
    }

    private void changeState(byte state) {
        mState = state;

        switch (mState) {
            // 暂停
            case PAUSE:
                mPause = true;
                if (sendNotification && mNotification != null) {
                    mNotification.contentView.setTextViewText(R.id.bt_pause, "继续");
                    mManager.notify(1, mNotification);
                }
                break;
            case CONTINUE:
                // 继续
                mPause = false;
                if (sendNotification && mNotification != null) {
                    mNotification.contentView.setTextViewText(R.id.bt_pause, "暂停");
                    mManager.notify(1, mNotification);
                }
                long breakPoint = SharedPreferenceUtil.getLong(mContext, "break_point", 0l);
                long fileSize = SharedPreferenceUtil.getLong(mContext, "break_size", 0l);
                String localFilename = SharedPreferenceUtil.getString(mContext, "break_local_filename", "");
                String downPath = SharedPreferenceUtil.getString(mContext, "break_down_path", "");
                String range = "bytes=0-";
                if (breakPoint != 0) {
                    range = String.format("bytes=%s-%s", breakPoint, fileSize);
                }
                down(downPath, localFilename, range, breakPoint, sendNotification);
                break;
            case CANCEL:
                if (sendNotification && mManager != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mManager.cancel(1);
                        }
                    }, 300);
                }
                SharedPreferenceUtil.remove(mContext, "break_point");
                SharedPreferenceUtil.remove(mContext, "break_size");
                SharedPreferenceUtil.remove(mContext, "break_local_filename");
                SharedPreferenceUtil.remove(mContext, "break_down_path");
                break;
        }
    }

    public void recycle() {
        mContext.unregisterReceiver(mReceiver);
    }
}
