package com.timecat.module.plugin.core.api

import android.net.Uri
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.plugin.database.Plugin

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
object ApiParser {
    const val SCHEMA = "world"
    const val HOST = "plugin.timecat.local"
    const val QUERY_Uuid = "uuid"
    const val DEFAULT_QUERY_Uuid: String = ""
    const val redirect = "redirect"

    fun getUuid(url: String): String {
        if (url.startsWith(SCHEMA)) {
            val uri = Uri.parse(url)
            return uri.getQueryParameter(QUERY_Uuid) ?: DEFAULT_QUERY_Uuid
        } else {
            return url
        }
    }

    fun toPath(plugin: Plugin): String {
        val uri = Uri.EMPTY.buildUpon().scheme(SCHEMA)
            .authority(HOST)
            .appendQueryParameter(redirect, RouterHub.GLOBAL_PluginApiContainerService)
            .appendQueryParameter(QUERY_Uuid, plugin.uuid)
            .build()
        return uri.toString()
    }

}