package com.timecat.module.skin.database

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
    tableName = "Skin",

    indices = [
        Index("uuid", unique = true)
    ]
)
data class Skin(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    /**
     * 唯一id，和云端Block一一对应
     */
    var uuid: String,
    /**
     * 包名
     */
    var packageName: String,
    /**
     * 实现类型
     */
    var type: Long = TYPE_None,
    var title: String,

    var managerVersionCode: Int,
    var managerVersionName: String,
) : Serializable, ISkinStatus {

    fun apkFile(context: Context): File = SkinDir.skinFile(context, this)
    fun apkFilePath(context: Context): String = apkFile(context).absolutePath

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