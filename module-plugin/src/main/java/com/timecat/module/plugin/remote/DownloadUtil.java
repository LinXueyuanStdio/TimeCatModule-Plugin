package com.timecat.module.plugin.remote;

import com.timecat.component.commonsdk.utils.override.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
public class DownloadUtil {

    //将下载的文件写入本地存储
    public static void writeFile2Disk(ResponseBody response, File file, DownloadListener downloadListener) {
        downloadListener.onStart();
        LogUtil.se("");

        if (response == null) {
            downloadListener.onFailure();
            LogUtil.se("");
            return;
        }

        LogUtil.se("");
        long currentLength = 0;
        OutputStream os = null;

        InputStream is = response.byteStream(); //获取下载输入流
        long totalLength = response.contentLength();

        LogUtil.se("" + totalLength);
        try {
            os = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += Math.abs(len);
                //计算当前下载百分比，并经由回调传出
                int p = (int) (100 * currentLength / totalLength);
                downloadListener.onProgress(Math.abs(p), 100);
            }
            //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
            LogUtil.se(file);
            downloadListener.onFinish(file.getAbsolutePath()); //下载完成
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                is.close(); //关闭输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtil.se(file.getAbsoluteFile());
            LogUtil.se(file.getTotalSpace() + "");
            LogUtil.se(file.toString());
        }
    }

}
