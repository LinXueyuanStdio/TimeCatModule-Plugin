package com.timecat.module.plugin.database

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description 插件目录
 * - cache
 *   - plugins
 *     - uuid
 *       - manager
 *         - 1.0.0
 *           - plugin-manager.apk
 *         - 1.0.1
 *           - plugin-manager.apk
 *       - plugin
 *         - 1.0.0
 *           - plugin-release.zip
 *         - 1.0.1
 *           - plugin-release.zip
 * @usage null
 */
object PluginDir {
    const val PLUGIN_DEPLOY_PATH = "plugins"
    const val PLUGIN_MANAGER_PATH = "manager"

    /**
     * 动态加载的插件管理apk
     */
    const val sPluginManagerName = "plugin-manager.apk"

    /**
     * 缓存目录
     * @param context context
     * @return 缓存目录
     */
    @JvmStatic
    fun getCacheDir(context: Context): String {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val cacheDir = context.externalCacheDir
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir.absolutePath
            }
        }
        return context.cacheDir.absolutePath
    }

    @JvmStatic
    fun getPluginDir(context: Context): String? {
        val pluginDir = File(getCacheDir(context), PLUGIN_DEPLOY_PATH)
        return if (pluginDir.exists() || pluginDir.mkdirs()) {
            pluginDir.absolutePath
        } else null
    }

    @JvmStatic
    fun managerApkFile(context: Context, plugin: Plugin): File {
        return File(
            getPluginDir(context),
            "${plugin.uuid}/${PLUGIN_MANAGER_PATH}/${plugin.managerVersionName}/${sPluginManagerName}"
        )
    }

}