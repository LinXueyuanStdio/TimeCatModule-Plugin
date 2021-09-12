package com.timecat.module.plugin.database

import android.content.Context
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.timecat.component.setting.DEF
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
    var type: Long = TYPE_PluginEnter,
    var title: String,

    var managerVersionCode: Int,
    var managerVersionName: String,
) : Serializable, IPluginStatus {

    fun managerApkFile(context: Context): File = PluginDir.managerApkFile(context, this)

    override fun toString(): String {
        return "uuid=${uuid}, title=${title}, $type, ${statusDescription()}"
    }

    //region 2. IPluginStatus
    //region Status 用 16 进制管理状态
    override fun updateStatus(s: Long, yes: Boolean) {
        super.updateStatus(s, yes)
        DEF.filter().putLong("DomainState", type)
    }

    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.type = this.type or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.type = this.type and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.type and status != 0L
    }
    //endregion
    //endregion
}