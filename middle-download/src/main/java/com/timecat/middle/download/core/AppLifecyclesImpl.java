package com.timecat.middle.download.core;

import android.app.Application;
import android.content.Context;

import com.jess.arms.base.delegate.AppLifecycles;
import com.timecat.middle.download.DownloadConfigKt;
import com.zpj.downloader.ZDownloader;

import androidx.annotation.NonNull;

public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {

        // 配置下载器
        ZDownloader.config(application)
                   .setDownloadPath(DownloadConfigKt.getDownloadPath())
                   .setConcurrentMissionCount(DownloadConfigKt.getMaxDownloadConcurrentCount())
                   .setEnableNotification(DownloadConfigKt.getShowDownloadNotification())
                   .setThreadCount(DownloadConfigKt.getMaxDownloadThreadCount())
                   .init();
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
