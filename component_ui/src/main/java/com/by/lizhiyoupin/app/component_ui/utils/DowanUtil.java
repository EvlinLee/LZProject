package com.by.lizhiyoupin.app.component_ui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.by.lizhiyoupin.app.common.log.LZLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * data:2019/10/31
 * author:jyx
 * function:图片下载工具类
 */
public class DowanUtil {
    public static final String TAG = DowanUtil.class.getSimpleName();

    public static String IMAGE_NAME = "lizhiyoupin_share_";

    //根据网络图片url路径保存到本地
    public static final File saveImageToSdCard(Context context, String image) {
        boolean success = false;
        File file = null;
        try {
            file = createStableImageFileNew(context);
            Bitmap bitmap = null;
            URL url = new URL(image);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            InputStream is = null;
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            FileOutputStream outStream;

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            outStream.flush();
            outStream.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //重要:保存到本地的图片无法显示到相册中的优化处理代码
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (success) {
            return file;
        } else {
            return null;
        }
    }

    //创建本地保存路径
    public static File createStableImageFileNew(Context context) throws IOException {
        long time = new Date().getTime();
        String imageFileName = IMAGE_NAME + time + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/uquan/shareImg/");
        Log.i("info", "=======保存路径====" + storageDir.getAbsolutePath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = new File(storageDir, imageFileName);
        return image;
    }

    //创建本地保存路径
    public static File createFileNew(Context context, String name) throws IOException {
        long time = new Date().getTime();
        String imageFileName = IMAGE_NAME + name + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/uquan/shareImg/");
        Log.i("info", "=======保存路径====" + storageDir.getAbsolutePath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = new File(storageDir, imageFileName);
        return image;
    }

    /**
     * 下载文件视频
     *
     * @param context
     * @param url
     */
    public static void downloadVideo(Context context, String url) {
        try {
            //下载路径，如果路径无效了，可换成你的下载路径

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            final long startTime = System.currentTimeMillis();
            Log.i(TAG, "startTime=" + startTime);
            //下载函数
            String filename;
            if (url.contains("?")) {
                String ustart = url.split("\\?")[0];
                if (ustart != null) {
                    filename = ustart.substring(url.lastIndexOf("/") + 1);
                } else {
                    filename = "lz_" + SystemClock.currentThreadTimeMillis();
                }
            } else {
                filename = url.substring(url.lastIndexOf("/") + 1);
            }
            //获取文件名
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            int fileSize = conn.getContentLength();//根据响应获取文件大小
            if (fileSize <= 0) throw new RuntimeException("无法获知文件大小 ");
            if (is == null) throw new RuntimeException("stream is null");
            Log.i(TAG, "download fileSize==" + (fileSize/1024/1024)+"M");
            LZLog.i(TAG,"filename="+filename);
            LZLog.i(TAG,"path="+path);
            //把数据存入路径+文件名
            File pathFile=new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String filePath=path + "/lz_" + filename;
            File file1 = new File(filePath);
            if (!file1.exists()) {
                boolean newFile = file1.createNewFile();
                LZLog.i(TAG,"newFile="+newFile);
            }
            FileOutputStream fos = new FileOutputStream(file1);
            byte buf[] = new byte[1024];
            int downLoadFileSize = 0;
            do {
                //循环读取
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
                downLoadFileSize += numread;
                //更新进度条
            } while (true);

            Log.i(TAG, "download success"+filePath);
            Log.i(TAG, "totalTime=" + (System.currentTimeMillis() - startTime));
            is.close();
            insertVideoToMediaStore(context, filePath, 5000);
        } catch (Exception ex) {
            Log.e(TAG, "error: " + ex.getMessage(), ex);
        }
    }

    //根据网络文件路径保存到本地
    public static final void saveH5AnyFileToSdCard(Context context, String url) {
        try {
            //下载路径，如果路径无效了，可换成你的下载路径

            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            final long startTime = System.currentTimeMillis();
            Log.i(TAG, "startTime=" + startTime);
            //下载函数
            String filename;
            if (url.contains("?")) {
                String ustart = url.split("\\?")[0];
                if (ustart != null) {
                    filename = ustart.substring(url.lastIndexOf("/") + 1);
                } else {
                    filename = "lz_" + SystemClock.currentThreadTimeMillis();
                }
            } else {
                filename = url.substring(url.lastIndexOf("/") + 1);
            }
            //获取文件名
            URL myURL = new URL(url);
            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            int fileSize = conn.getContentLength();//根据响应获取文件大小
            if (fileSize <= 0){
                //获取不到文件跳转浏览器
                jumpOtherWebh5(context,url);
                throw new RuntimeException("无法获知文件大小 ");
            }
            if (is == null) {
                jumpOtherWebh5(context,url);
                throw new RuntimeException("stream is null");
            }
            Log.i(TAG, "download fileSize==" + (fileSize / 1024 / 1024) + "M");
            LZLog.i(TAG, "filename=" + filename);
            LZLog.i(TAG, "path=" + path);
            //把数据存入路径+文件名
            File pathFile = new File(path);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            String filePath = path + "/lz_" + filename;
            File file1 = new File(filePath);
            if (!file1.exists()) {
                boolean newFile = file1.createNewFile();
                LZLog.i(TAG, "newFile=" + newFile);
            }
            FileOutputStream fos = new FileOutputStream(file1);
            byte buf[] = new byte[1024];
            int downLoadFileSize = 0;
            do {
                //循环读取
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
                downLoadFileSize += numread;
                //更新进度条
            } while (true);

            Log.i(TAG, "download success" + filePath);
            Log.i(TAG, "totalTime=" + (System.currentTimeMillis() - startTime));
            is.close();
            //通知媒体更新文件
            MediaScannerConnection.scanFile(context,
                    new String[] { file1.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, "error: " + ex.getMessage(), ex);
        }


    }

    /**
     * 跳转浏览器
     * @param context
     * @param url
     */
    private static void jumpOtherWebh5(Context context,String url){
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void notifyScanDcim(Context context, String filePath) {
        scanFile(context, filePath);
    }

    public static void insertVideoToMediaStore(Context context, String filePath, long duration) {
        insertVideoToMediaStore(context, filePath, -1, 0, 0, duration);
    }

 /*   public static void insertVideoToMediaStore(Context context, VideoUtil.VideoInfo videoInfo) {
        insertVideoToMediaStore(context, videoInfo.originalVideoFilePath, videoInfo.dateTaken, videoInfo.width, videoInfo.height, videoInfo.duringTime);
    }*/

    public static void insertImageToMediaStore(Context context, String filePath) {
        insertImageToMediaStore(context, filePath, -1, 0, 0);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 扫描系统相册核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对系统文夹只需要扫描,不用插入内容提供者,不然会重复
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void scanFile(Context context, String filePath) {
        if (!checkFile(filePath))
            return;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 非系统相册像MediaContent中插入数据，核心方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 针对非系统文件夹下的文件,使用该方法
     * 插入时初始化公共字段
     *
     * @param filePath 文件
     * @param time     ms
     * @return ContentValues
     */
    private static ContentValues initCommonContentValues(String filePath, long time) {
        ContentValues values = new ContentValues();
        File saveFile = new File(filePath);
        long timeMillis = getTimeWrap(time);
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        values.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    /**
     * 保存到照片到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的照片获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertImageToMediaStore(Context context, String filePath, long createTime, int width, int height) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0) values.put(MediaStore.Images.ImageColumns.WIDTH, 0);
            if (height > 0) values.put(MediaStore.Images.ImageColumns.HEIGHT, 0);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getPhotoMimeType(filePath));
        context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 保存到视频到本地，并插入MediaStore以保证相册可以查看到,这是更优化的方法，防止读取的视频获取不到宽高
     *
     * @param context    上下文
     * @param filePath   文件路径
     * @param createTime 创建时间 <=0时为当前时间 ms
     * @param duration   视频长度 ms
     * @param width      宽度
     * @param height     高度
     */
    public static void insertVideoToMediaStore(Context context, String filePath, long createTime, int width, int height, long duration) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, createTime);
        if (duration > 0)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0) values.put(MediaStore.Video.VideoColumns.WIDTH, width);
            if (height > 0) values.put(MediaStore.Video.VideoColumns.HEIGHT, height);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getVideoMimeType(filePath));
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }


    // 是不是系统相册
    private static boolean isSystemDcim(String path) {
        return path.toLowerCase().contains("dcim") || path.toLowerCase().contains("camera");
    }

    // 获取照片的mine_type
    private static String getPhotoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith("png")) {
            return "image/png";
        } else if (lowerPath.endsWith("gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    // 获取video的mine_type,暂时只支持mp4,3gp
    private static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }

    // 获得转化后的时间
    private static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

    // 检测文件存在
    private static boolean checkFile(String filePath) {
        //boolean result = FileUtil.fileIsExist(filePath);
        boolean result = false;
        File mFile = new File(filePath);
        if (mFile.exists()) {
            result = true;
        }else {
            Log.e(TAG, "文件不存在 path = " + filePath);
        }
        return result;
    }
}
