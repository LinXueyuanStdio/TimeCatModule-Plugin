package com.timecat.module.plugin.ext

import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.PluginApp
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.TYPE_PluginEnter

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/10
 * @description null
 * @usage null
 */
fun Block.toPlugin(): Plugin {
    val head = AppBlock.fromJson(structure)
    val head2 = PluginApp.fromJson(head.structure)
    val info = head2.updateInfo.firstOrNull()
    val versionCode = info?.version_code ?: 1
    val versionName = info?.version_name ?: "1.0.0"
    return Plugin(0, objectId, head2.packageName, TYPE_PluginEnter, title, versionCode, versionName)
}
fun Block.versionCode(): Int {
    val head = AppBlock.fromJson(structure)
    val head2 = PluginApp.fromJson(head.structure)
    val info = head2.updateInfo.firstOrNull()
    return info?.version_code ?: 1
}