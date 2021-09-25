package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class LocalPluginItem(
    val context: Context,
    val plugin: Plugin
) : BaseItem<PluginCardVH>(plugin.uuid) {

    override fun getLayoutRes(): Int = R.layout.plugin_item_local_plugin

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): PluginCardVH = PluginCardVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: PluginCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.title.text = plugin.title
        val stateText = "管理器：${plugin.managerVersionName}(${plugin.managerVersionCode})"
        holder.state.text = stateText
        holder.stateBtn.setShakelessClickListener {
            //升级

        }
        holder.root.setShakelessClickListener {
            //打开
        }

        if (payloads.isNullOrEmpty()) return
        val obj = payloads.first()
        if (obj is Block) {
            val block = obj
            // TODO 更新
        }
    }
}