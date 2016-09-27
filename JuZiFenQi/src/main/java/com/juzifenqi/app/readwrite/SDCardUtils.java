package com.juzifenqi.app.readwrite;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.juzifenqi.app.constant.Const;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Desc:   SDCard文件读写管理工具
 * Time:   2016-09-23 12:53
 * Author: chende
 */
public class SDCardUtils {

    private final static String BASE_DIR = Const.SDCARD_FOLDER_NAME;
    private final static String DATA_CHACHE_PATH = "/caches/";
    private final static String LOGS_PATH = "/logs/";
    private final static String APKS_PATH = "/apks/";
    private final static String PORTRAIT_PATH = "/portrait/";
    private final static String IMAGE_PATH = "/image/";
    private static String rootPath;

    /**
     * 本地文件管理初始化
     */
    public static void initPath(Context mContext) {
        String basePath = getBasePath(mContext);
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        initFoldpath(mContext);
    }

    /**
     * 初始化存放图片的目录
     * <p/>
     * mContext
     */
    private static void initFoldpath(Context mContext) {
        makeSureFolderExist(getBasePath(mContext) + File.separator + DATA_CHACHE_PATH);
        makeSureFolderExist(getBasePath(mContext) + File.separator + LOGS_PATH);
        makeSureFolderExist(getBasePath(mContext) + File.separator + APKS_PATH);
        makeSureFolderExist(getBasePath(mContext) + File.separator + PORTRAIT_PATH);
        makeSureFolderExist(getBasePath(mContext) + File.separator + IMAGE_PATH);
    }

    private static String getBasePath(Context mContext) {
        String basePath = null;
        if (!TextUtils.isEmpty(rootPath)) {
            basePath = rootPath;
        } else {
            if (!externalStorageWriteable()) {
                basePath = mContext.getCacheDir().getAbsolutePath() + File.separator + BASE_DIR;
            } else {
                basePath = Environment.getExternalStorageDirectory() + File.separator + BASE_DIR;
            }
        }
        rootPath = basePath;
        return basePath;
    }

    private static void makeSureFolderExist(String path) {
        File file_path = new File(path);
        if (file_path.isFile()) {// 若为文件，则直接删除文件
            file_path.delete();
        }
        if (!file_path.exists()) {// 并创建目录
            file_path.mkdirs();
        }
    }


    public static String getApksPath(Context mContext) {//获取指定路径的文件
        File file = new File(getBasePath(mContext) + File.separator + APKS_PATH);
        makeSureFolderExist(file.getAbsolutePath());
        return file.getAbsolutePath();
    }


    public static String getLogsPath(Context mContext, String fileName) {//获取指定路径的文件
        File file = new File(getBasePath(mContext) + File.separator + LOGS_PATH + fileName);
        makeSureFolderExist(file.getParent());
        return file.getAbsolutePath();
    }

    public static String getHeadPortraitPath(Context mContext, String fileName) {//获取指定路径的文件
        File file = new File(getBasePath(mContext) + File.separator + PORTRAIT_PATH + fileName);
        makeSureFolderExist(file.getParent());
        return file.getAbsolutePath();
    }

    public static String getImgPath(Context mContext, String fileName) {//获取指定路径的文件
        File file = new File(getBasePath(mContext) + File.separator + IMAGE_PATH + fileName);
        makeSureFolderExist(file.getParent());
        return file.getAbsolutePath();
    }

    /**
     * 检测外部存储是否可写
     *
     * @return boolean 外部存储是否可写
     */
    private static boolean externalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String readFile(String fileName, Context mContext) {
        File file = new File(getBasePath(mContext) + File.separator + DATA_CHACHE_PATH + fileName);
        StringBuilder stringBuilder = new StringBuilder("");
        char[] array = new char[1024];
        int length = -1;
        if (file == null || !file.isFile() || !file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), "utf-8");
            reader = new BufferedReader(is);
            while ((length = reader.read(array)) != -1) {
                stringBuilder.append(String.valueOf(array));
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TextUtils.isEmpty(stringBuilder) ? null : stringBuilder.toString();
    }

    public static String readLogs(String fileName, Context mContext) {
        String absPath = getBasePath(mContext) + File.separator + LOGS_PATH + fileName;
        File file = new File(absPath);
        if (!file.exists())
            return null;
        String text = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileReader = new FileReader(file.getAbsoluteFile());
            bufferedReader = new BufferedReader(fileReader);
            while ((text = bufferedReader.readLine()) != null) {
                stringBuilder.append(text);
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static void writeLogs(String fileName, String content, Context mContext) {
        String absPath = getBasePath(mContext) + File.separator + LOGS_PATH + fileName;
        writeFile(content, absPath);
    }

    public static boolean writeFile(String content, String absPath) {
        File file = new File(absPath);
        if (file.exists())
            file.delete();
        FileWriter fileWriter = null;
        try {
            makeSureFolderExist(file.getParent());
            fileWriter = new FileWriter(file.getAbsoluteFile(), false);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void cleanCache(Context mContext) {
        deleteFile(getBasePath(mContext) + File.separator + LOGS_PATH);
        deleteFile(getBasePath(mContext) + File.separator + APKS_PATH);
        deleteFile(getBasePath(mContext) + File.separator + PORTRAIT_PATH);
        deleteFile(getBasePath(mContext) + File.separator + IMAGE_PATH);
        deleteFile(getBasePath(mContext) + File.separator + DATA_CHACHE_PATH);
    }

    public static void deleteFile(String folderPath) {// 删除指定目录上面的所有文件及子目录（包含子目录下面的所有文件）
        File file = new File(folderPath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile()) {
                        f.delete();
                    } else {
                        deleteFile(f.getAbsolutePath());
                    }
                }
            }
        }
    }


}