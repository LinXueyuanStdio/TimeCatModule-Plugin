package com.timecat.module.plugin.remote;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-14
 * @description null
 * @usage null
 */
public interface DownloadListener {
    void onStart();

    void onProgress(int currentLength, int total);

    void onFinish(String localPath);

    void onFailure();
}