package cn.dazhou.commonlib.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;

import cn.dazhou.commonlib.api.HttpApi;
import cn.dazhou.commonlib.api.OnUploadListener;
import cn.dazhou.commonlib.bean.FileAccessI;
import cn.dazhou.commonlib.bean.FileBean;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static android.util.Log.i;

/**
 * Created by hooyee on 2017/6/27.
 */

public class FileUtil {
    private static final String LOG_TAG_HTTP = "FileUtil";
    public static final String FILE_SAVE_TO_LOCAL_PATH = Environment.getExternalStorageDirectory() + "/railway_emergency/file/";

    // 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
    @SuppressLint("NewApi")
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static byte[] getBytes(String path) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(new File(path));
        byte[] bytes = new byte[1024 * 8];
        int length;
        while (true) {
            length = in.read(bytes);
            if (length == -1) {
                break;
            }
            out.write(bytes, 0, length);
        }
        return out.toByteArray();
    }

    public static String saveByteToLocalFile(byte[] bytes, String fileName) {
        File file = new File(FILE_SAVE_TO_LOCAL_PATH, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 返回文件大小，最大单位: M，最小单位: K
     *
     * @param length
     * @return
     */
    public static String getSizeByB(long length) {
        DecimalFormat df = new DecimalFormat("#.00");
        String sizestr = df.format(length / 1024 / 1024);
        if (sizestr != null && !"".equals(sizestr)) {
            if ("00".equals(sizestr.substring(sizestr.lastIndexOf(".") + 1))) {
                sizestr = sizestr.substring(0, sizestr.lastIndexOf(".")) + "M";
                if ("M".equals(sizestr)) {
                    sizestr = df.format(length / 1024);
                    if ("00".equals(sizestr.substring(sizestr.lastIndexOf(".") + 1))) {
                        sizestr = sizestr.substring(0, sizestr.lastIndexOf("."));
                        if ("".equals(sizestr)) {
                            sizestr = "小于0";
                        }
                    }
                    sizestr += "K";
                }
            }
        } else {
            sizestr = "0K";
        }
        return sizestr;
    }


    /**
     * 分片上传选择的本地文件
     *
     * @param uploadpath
     * @param uploadsavepath
     * @param data 记录上传的文件的分片总数  String：文件名  Integer：当前片数
     *             Map数据格式：
     *             <p>
     *             Map<String, Object> param = new HashMap<>();
     *             param.put("thumbnail", filename);
     *             param.put("remark", "暂无备注");
     *             param.put("functions", "路径： " + result.getPath());
     *             param.put("size", sizestr);
     *             param.put("length", length);
     *             param.put("filename", filename);
     *             param.put("filepath", filepath);
     *             param.put("chuck", 0);
     *             param.put("flag", null);
     *             param.put("finishchuck", "");
     */
    public static void cut_upload_adapter(final String uploadpath, final String uploadsavepath, final List<FileBean> data, ExecutorService fixedThreadPool, final OnUploadListener uploadListener, final HttpApi httpApi, final double latitude, final double longitude) {
        int count = data.size();
        if (count <= 0) return;

        for (int i = 1; i < count; i++) {
            final int position = i;
            final FileBean bean = data.get(i);
            final String uploadTime = new Date().getTime() + "";
            final Map<String, Integer> chuckNoMap = new HashMap<>();  // 记录上传的文件的分片总数  String：文件名  Integer：当前片数
            chuckNoMap.put(bean.getFilename(), chuckNoMap.get(bean.getFilename()) != null ? chuckNoMap.get(bean.getFilename()) : 0);

            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    cut_upload_one(uploadpath, uploadsavepath, bean, uploadTime, chuckNoMap, uploadListener, httpApi, latitude, longitude);

                }
            });
        }
    }

    private static void cut_upload_one(String uploadpath, final String uploadsavepath, final FileBean bean, final String uploadTime, final Map<String, Integer> chuckNoMap, final OnUploadListener uploadListener, final HttpApi httpApi, final double latitude, final double longitude) {
        if (bean.isFlag()) {

            try {
                final FileAccessI fileAccessI = new FileAccessI(bean.getFilepath(), 0);
                Long nStartPos = 0l;
                final Long length = fileAccessI.getFileLength();
                int mBufferSize = 512 * 1024; // 每次处理1024 * 1024字节
                if (length < mBufferSize) {
                    mBufferSize = new Long(length).intValue();
                }
                byte[] buffer = new byte[mBufferSize];
                FileAccessI.Detail detail;
                long nRead = 0l;
                long nStart = nStartPos;
                int chunk = bean.getChuck();
                final int chunkno = (int) Math.ceil((float) length / mBufferSize);

                while (nStart < length) {
                    if (chunk > 1 && chunk == (chunkno - 1)) {
                        mBufferSize = new Long(length - chunk * mBufferSize).intValue();
                    }
                    detail = fileAccessI.getContent(nStart, mBufferSize);
                    nRead = detail.length;
                    buffer = detail.b;
                    final JSONObject mInDataJson = new JSONObject();
                    mInDataJson.put("fileName", bean.getFilename());
                    mInDataJson.put("chunk", chunk);  // 当前片数

                    chunk++;
                    nStart += nRead;

                    final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("json", mInDataJson.toString())
                            .addFormDataPart("data", encodeByte(buffer))
                            .addFormDataPart("uploadTime", uploadTime)
                            .addFormDataPart("savePath", "android");

                    boolean flag = true;
                    String str = bean.getFinishchuck();
                    if (!"".equals(str)) {
                        String[] arr = str.split(",");
                        if (!bean.isNes()) {
                            if (!(Arrays.asList(arr).contains((chunk - 1) + ""))) {
                                flag = false;
                            }
                        }
                    }

                    if (flag) {
                        // 执行请求
                        Call<ResponseBody> call = httpApi.cutUpload(uploadpath, builder.build().parts());
                        bean.setChuck(chunk - 1);
                        bean.setFinishchuck("".equals(bean.getFinishchuck()) ? chunk - 1 + "" : bean.getFinishchuck() + "," + (chunk - 1));

                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                chuckNoMap.put(bean.getFilename() + "", chuckNoMap.get(bean.getFilename()) + 1);
                                try {
                                    String jsonstr = response.body().string();
                                    Log.v("Upload", "success");
                                    i("http-test", jsonstr);

                                    if (uploadListener != null) {
                                        bean.setProcess(chuckNoMap.get(bean.getFilename()) * 1.0f / chunkno * 100);
                                        uploadListener.onUpdate(bean);
                                    }

                                    if (chuckNoMap.get(bean.getFilename()).equals(chunkno)) {

                                        Call saveSQLcall = httpApi.cutUploadSaveSQL(uploadsavepath, uploadTime, chunkno + "", "android", bean.getFilename() + "", length + "", longitude + "", latitude + "", "", "移动终端", "", bean.getRemark(), "", "");
                                        saveSQLcall.enqueue(new Callback() {
                                            @Override
                                            public void onResponse(Call call, Response response) {
                                                bean.setFlag(false);
                                                bean.setProcess(100);

                                                Log.i("TIPS", "onResponse: " + bean.getFilename());
                                                for (UploadListener o : listeners) {
                                                    o.onComplete();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call call, Throwable t) {
                                                Log.e("http-test-save", t.getMessage());
                                                bean.setProcess(0);
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("http-test-cut-upload", t.getMessage());
//                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        final int chuckc = chuckNoMap.get(bean.getFilename()) + 1;
                        if (uploadListener != null) {
                            bean.setProcess(chuckc * 1.0f / chunkno * 100);
                            uploadListener.onUpdate(bean);
                        }

                        if (chuckc == chunkno) {
                            Call saveSQLcall = httpApi.cutUploadSaveSQL(uploadsavepath, uploadTime, chunkno + "", "android", bean.getFilename() + "", length + "", 0 + "", 0 + "", "", "", "", bean.getRemark(), "", "");
                            saveSQLcall.enqueue(new Callback() {
                                @Override
                                public void onResponse(Call call, Response response) {
                                    bean.setFlag(false);
                                    bean.setProcess(100);

                                    for (UploadListener o : listeners) {
                                        o.onComplete();
                                    }
                                }

                                @Override
                                public void onFailure(Call call, Throwable t) {
                                    Log.e("http-test-save", t.getMessage());
//                                    adapter.notifyDataSetChanged();
                                    bean.setProcess(0);
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                Log.d("EXCEPETION", e.getMessage());
            }
        } else {
            Log.i("TIPS", "文件【" + bean.getFilename() + "】已上传");
        }
    }

    // 防乱码
    public static String encodeByte(byte[] buffer) {
        return Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
    }

    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Railway");

            Log.d("LOG_TAG", "Successfully created mediaStorageDir: " + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("LOG_TAG", "Error in Creating mediaStorageDir: " + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d("LOG_TAG", "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Vector<UploadListener> listeners = new Vector<UploadListener>();

    public interface UploadListener {
        void onComplete();
    }

    public static void register(UploadListener o) {
        listeners.add(o);
    }

    public static void unregister(UploadListener o) {
        listeners.remove(o);
    }
}
