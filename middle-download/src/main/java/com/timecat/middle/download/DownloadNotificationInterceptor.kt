package com.timecat.middle.download

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.zpj.downloader.BaseMission
import com.zpj.downloader.INotificationInterceptor

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
abstract class DownloadNotificationInterceptor(val downloadClass: Class<*>) : INotificationInterceptor {
    override fun onProgress(context: Context, mission: BaseMission<*>, progress: Float, isPause: Boolean) {
        val intent = Intent(context, downloadClass)
        intent.putExtra(ACTION, ACTION_SHOW_DOWNLOAD)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        ZNotify.init(context)
        buildProgressNotify(context)
            .setProgressAndFormat(progress, false, "")
            .setContentTitle("${if (isPause) "已暂停：" else ""}${mission.name}")
            .setContentIntent(pendingIntent)
            .setId(mission.notifyId)
            .show()
    }

    override fun onFinished(context: Context, mission: BaseMission<*>) {
        ZNotify.cancel(mission.notifyId)

        val intent = Intent(context, downloadClass)
        intent.putExtra(ACTION, ACTION_SHOW_DOWNLOAD)
        val pi: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        ZNotify.init(context)
        buildNotify(context)
            .setContentTitle(mission.name)
            .setContentText("下载已完成")
            .setContentIntent(pi)
            .setId(mission.notifyId)
            .show()
    }

    override fun onError(context: Context, mission: BaseMission<*>, errCode: Int) {
        ZNotify.cancel(mission.notifyId)

        val intent = Intent(context, downloadClass)
        intent.putExtra(ACTION, ACTION_SHOW_DOWNLOAD)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        ZNotify.init(context)
        buildNotify(context)
            .setContentTitle("下载出错$errCode:${mission.name}")
            .setContentIntent(pendingIntent)
            .setId(mission.notifyId)
            .show()
    }

    override fun onCancel(context: Context, mission: BaseMission<*>) {
        ZNotify.cancel(mission.notifyId)
    }

    override fun onCancelAll(context: Context) {
        ZNotify.cancelAll()
    }
}
