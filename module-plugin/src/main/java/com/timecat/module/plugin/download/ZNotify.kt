package com.timecat.module.plugin.download

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.timecat.module.plugin.download.builder.BaseBuilder
import com.timecat.module.plugin.download.builder.ProgressBuilder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
fun buildProgressNotify(context: Context): ProgressBuilder {
    return ProgressBuilder(context)
}

fun buildNotify(context: Context): BaseBuilder {
    return BaseBuilder(context)
}

object ZNotify {
    var nm: NotificationManager? = null
    const val CHANNEL_ID = "com.time.cat.download.notification"
    private const val CHANNEL_NAME = "下载时的提示通知"

    fun init(context: Context) {
        if (nm == null) {
            nm = context.getSystemService(Activity.NOTIFICATION_SERVICE) as? NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
//            channel.canBypassDnd();//是否绕过请勿打扰模式
            channel.enableLights(false) //闪光灯
            channel.lockscreenVisibility = Notification.VISIBILITY_SECRET //锁屏显示通知
//            channel.setLightColor(Color.RED);//闪关灯的灯光颜色
//            channel.canShowBadge();//桌面launcher的消息角标
            channel.setShowBadge(true)
            channel.enableVibration(false) //是否允许震动
//            channel.getAudioAttributes();//获取系统通知响铃声音的配置
//            channel.getGroup();//获取通知取到组
            channel.setBypassDnd(false) //设置可绕过 请勿打扰模式
//            channel.setVibrationPattern(new long[]{100, 100, 200});//设置震动模式
//            channel.shouldShowLights();//是否会有灯光
            channel.setSound(null, null)
            channel.vibrationPattern = longArrayOf(0)
            nm?.createNotificationChannel(channel)
        }
    }

    fun notify(id: Int, notification: Notification?) {
        nm?.notify(id, notification)
    }

    fun cancel(id: Int) {
        nm?.cancel(id)
    }

    fun cancelAll() {
        nm?.cancelAll()
    }

}
