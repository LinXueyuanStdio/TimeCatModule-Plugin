package com.timecat.module.plugin.database

import android.content.Context
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.File
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description null
 * @usage null
 */
@Entity(
    tableName = "Plugin",

    indices = [
        Index("uuid", unique = true)
    ]
)
data class Plugin(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var uuid: String,
    var type: Int,
    var title: String,

    var managerVersionCode: Int,
    var managerVersionName: String,

    var pluginVersionCode: Int,
    var pluginVersionName: String,

    var mainActivity: String,
) : Serializable {
    fun managerApkFile(context: Context): File = PluginDir.managerApkFile(context, this)
    fun pluginZipFile(context: Context): File = PluginDir.pluginZipFile(context, this)
}