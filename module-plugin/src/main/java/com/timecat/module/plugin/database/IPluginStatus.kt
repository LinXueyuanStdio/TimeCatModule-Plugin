package com.timecat.module.plugin.database

import com.timecat.identity.data.base.IStatus

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
const val TYPE_PluginEnter: Long = 0x00000001
const val TYPE_RecordApi: Long = 0x00000002

interface IPluginStatus : IStatus {
    fun canPluginEnter(): Boolean = isStatusEnabled(TYPE_PluginEnter)
    fun canRecordApi(): Boolean = isStatusEnabled(TYPE_RecordApi)

    fun setPluginEnter(yes: Boolean) = updateStatus(TYPE_PluginEnter, yes)
    fun setRecordApi(yes: Boolean) = updateStatus(TYPE_RecordApi, yes)

    override fun statusDescription(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(if (canPluginEnter()) "插件主页面 " else "")
        stringBuilder.append(if (canRecordApi()) "符文体系 " else "")
        return stringBuilder.toString()
    }
}