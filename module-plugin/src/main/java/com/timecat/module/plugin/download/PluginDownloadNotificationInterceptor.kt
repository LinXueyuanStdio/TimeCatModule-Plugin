package com.timecat.module.plugin.download

import com.timecat.middle.download.DownloadNotificationInterceptor
import com.timecat.module.plugin.PluginDownloadActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
class PluginDownloadNotificationInterceptor : DownloadNotificationInterceptor(PluginDownloadActivity::class.java)