package com.timecat.module.skin.database

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description 插件目录
 * - cache
 *   - skins
 *     - uuid
 *       - skin
 *         - 1.0.0
 *           - plugin-manager.apk
 *         - 1.0.1
 *           - plugin-manager.apk
 * @usage null
 */
object SkinDir {
    const val SKIN_DEPLOY_PATH = "skins"
    const val SKIN_MANAGER_PATH = "skin"
    const val skinFileName = "skin.apk"

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
    fun getSkinDir(context: Context): String? {
        val pluginDir = File(getCacheDir(context), SKIN_DEPLOY_PATH)
        return if (pluginDir.exists() || pluginDir.mkdirs()) {
            pluginDir.absolutePath
        } else null
    }

    @JvmStatic
    fun skinFile(context: Context, skin: Skin): File {
        return File(
            getSkinDir(context),
            "${skin.packageName}/${SKIN_MANAGER_PATH}/${skin.managerVersionName}/${skinFileName}"
        )
    }

}