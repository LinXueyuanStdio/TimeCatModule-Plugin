package com.timecat.module.plugin.remote

import android.content.Context
import com.timecat.data.system.network.RetrofitHelper
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.manager.PluginInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description null
 * @usage null
 */
fun downloadRemotePlugin(
    context: Context,
    remotePlugin: PluginInfo,
    localPlugin: Plugin?,
    listener: DownloadListener
): Disposable? {
    if (localPlugin == null) {
        //两个都下载
        return download2Files(
            remotePlugin.managerUrl, remotePlugin.managerApkFile(context),
            remotePlugin.pluginZipUrl, remotePlugin.pluginZipFile(context),
            listener
        )
    } else {
        if (remotePlugin.managerVersionCode > localPlugin.managerVersionCode
            && remotePlugin.pluginVersionCode > localPlugin.pluginVersionCode
        ) {
            //两个都下载
            return download2Files(
                remotePlugin.managerUrl, remotePlugin.managerApkFile(context),
                remotePlugin.pluginZipUrl, remotePlugin.pluginZipFile(context),
                listener
            )
        } else if (remotePlugin.managerVersionCode > localPlugin.managerVersionCode) {
            //只下载 plugin-manager.apk
            return downloadFile(remotePlugin.managerUrl, remotePlugin.managerApkFile(context), listener)
        } else if (remotePlugin.pluginVersionCode > localPlugin.pluginVersionCode) {
            //只下载 plugin-release.zip
            return downloadFile(remotePlugin.pluginZipUrl, remotePlugin.pluginZipFile(context), listener)
        }
    }
    return null
}

fun download2Files(
    managerApkUrl: String, managerApkFile: File,
    pluginZipUrl: String, pluginZipFile: File,
    listener: DownloadListener
): Disposable {
    val manager = RetrofitHelper.getPluginService().downloadFile(managerApkUrl).doOnNext {
        DownloadUtil.writeFile2Disk(it, managerApkFile, object : DownloadListener by listener {
            override fun onProgress(currentLength: Int, total: Int) {
                listener.onProgress(currentLength / total * 50, 100)
            }

            override fun onFinish(localPath: String?) {
            }
        })
    }
    val plugin = RetrofitHelper.getPluginService().downloadFile(pluginZipUrl).doOnNext {
        DownloadUtil.writeFile2Disk(it, pluginZipFile, object : DownloadListener by listener {
            override fun onProgress(currentLength: Int, total: Int) {
                listener.onProgress(currentLength / total * 50 + 50, 100)
            }

            override fun onFinish(localPath: String?) {
            }
        })
    }
    return Observable.concat(manager, plugin)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
            },
            {
                listener.onFailure()
            }
        )
}

fun downloadFile(fromUrl: String, toFile: File, listener: DownloadListener): Disposable {
    return RetrofitHelper.getPluginService().downloadFile(fromUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
                DownloadUtil.writeFile2Disk(it, toFile, listener)
            },
            {
                listener.onFailure()
            }
        )
}