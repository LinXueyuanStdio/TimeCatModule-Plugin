package com.timecat.module.skin.download

import com.timecat.middle.download.DownloadNotificationInterceptor
import com.timecat.module.skin.SkinDownloadActivity


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
class SkinDownloadNotificationInterceptor : DownloadNotificationInterceptor(SkinDownloadActivity::class.java)