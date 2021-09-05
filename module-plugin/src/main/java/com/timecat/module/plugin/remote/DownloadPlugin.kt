package com.timecat.module.plugin.remote

import android.content.Context
import com.timecat.data.system.network.RetrofitHelper
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.manager.PluginInfo
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
): Disposable {
    if (localPlugin == null) {
        //两个都下载
    } else {
        if (remotePlugin.managerVersionCode > localPlugin.managerVersionCode
            && remotePlugin.pluginVersionCode > localPlugin.pluginVersionCode) {

        } else if (remotePlugin.managerVersionCode > localPlugin.managerVersionCode) {
            //下载 plugin-manager.apk
            return downloadFile(remotePlugin.managerUrl, remotePlugin.managerApkFile(context), listener)
        } else if (remotePlugin.pluginVersionCode > localPlugin.pluginVersionCode) {
            //下载 plugin-release.zip
            return downloadFile(remotePlugin.pluginZipUrl, remotePlugin.pluginZipFile(context), listener)
        }
    }
}

fun downloadFile(fromUrl: String, toFile: File, listener: DownloadListener): Disposable {
    return RetrofitHelper.getPluginService().downloadFile(fromUrl)
        .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
        .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
        .subscribe(
            {
                DownloadUtil.writeFile2Disk(it, toFile, listener)
            },
            {
                listener.onFailure()
            }
        )
}