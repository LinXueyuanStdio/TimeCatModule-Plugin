package com.timecat.module.plugin

import com.timecat.component.setting.DEF
import com.timecat.component.setting.DIR
import com.timecat.component.setting.PATH
import com.zpj.downloader.ZDownloader

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
var showDownloadNotification: Boolean
    get() = DEF.block().getBoolean("KEY_SHOW_DOWNLOAD_NOTIFICATION", true)
    set(value) {
        DEF.block().putBoolean("KEY_SHOW_DOWNLOAD_NOTIFICATION", value)
    }
var downloadPath: String
    get() = DEF.block().getString("KEY_DOWNLOAD_PATH", PATH.of(DIR.Downloads)) ?: PATH.of(DIR.Downloads)
    set(value) {
        DEF.block().putString("KEY_DOWNLOAD_PATH", value)
    }
var maxDownloadConcurrentCount: Int
    get() = DEF.block().getInt("KEY_MAX_DOWNLOAD_CONCURRENT_COUNT", 3)
    set(value) {
        DEF.block().putInt("KEY_MAX_DOWNLOAD_CONCURRENT_COUNT", value)
        ZDownloader.setDownloadConcurrentCount(value)
    }
var maxDownloadThreadCount: Int
    get() = DEF.block().getInt("KEY_MAX_DOWNLOAD_THREAD_COUNT", 3)
    set(value) {
        DEF.block().putInt("KEY_MAX_DOWNLOAD_THREAD_COUNT", value)
        ZDownloader.setDownloadThreadCount(value)
    }

const val ACTION = "action"
const val ACTION_SHOW_UPDATE = "show_update"
const val ACTION_SHOW_DOWNLOAD = "show_download"
const val ACTION_SEND_VIEW_PAGER_INDEX = "send_view_pager_index"