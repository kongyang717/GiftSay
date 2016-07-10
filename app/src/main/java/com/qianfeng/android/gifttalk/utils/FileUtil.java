package com.qianfeng.android.gifttalk.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @auther Kong Yang
 * @since 2016/7/9
 */
public class FileUtil {

    /**
     * 保存文件到内部存储中
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void save(Context context, String fileName, String content)
            throws IOException {
        FileOutputStream os = context.openFileOutput(
                fileName, Context.MODE_PRIVATE);
        os.write(content.getBytes());
        os.close();
    }

    public static void saveByUrl(Context context, String url, String content)
            throws IOException {
        save(context, getFileNameFromUrl(url), content);
    }

    /**
     * 将url转化为MD5加密的文件名
     *
     * @param url url
     * @return 加密后的文件名
     */
    private static String getFileNameFromUrl(String url) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            byte[] digest = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(Integer.toHexString(b + 128));//加上128除去负数
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从内部存储中删除文件
     *
     * @param context  上下文
     * @param fileName 文件名
     */
    public static String read(Context context, String fileName)
            throws IOException {
        FileInputStream is = context.openFileInput(fileName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int l;
        while ((l = is.read(bytes)) != -1) {
            bos.write(bytes, 0, l);
        }
        String result = bos.toString();
        is.close();
        bos.close();
        return result;
    }

    public static String readByUrl(Context context, String url)
            throws IOException {
        return read(context, getFileNameFromUrl(url));
    }

}
