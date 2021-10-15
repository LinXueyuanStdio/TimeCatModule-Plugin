package com.timecat.module.skin.database

import com.timecat.identity.data.base.IStatus

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
const val TYPE_None: Long = 0x00000000
const val TYPE_PluginEnter: Long = 0x00000001
const val TYPE_RecordApi: Long = 0x00000002

interface ISkinStatus : IStatus {
    fun appSkin(): Boolean = isStatusEnabled(TYPE_None)

    fun setAppSkin(yes: Boolean) = updateStatus(TYPE_None, yes)

    override fun statusDescription(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(if (appSkin()) "应用皮肤 " else "")
        return stringBuilder.toString()
    }
}