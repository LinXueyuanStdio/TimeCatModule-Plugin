package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.ext.showFloatMenu
import com.timecat.middle.block.ext.simpleItem
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.plugin.R
import com.timecat.module.plugin.core.api.ApiParser
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.module.plugin.ext.startPlugin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class LocalPluginCard(
    val context: Context,
    val plugin: Plugin,
    val listener: ItemCommonListener
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
        holder.stateBtn.text = "打开"
        holder.stateBtn.setShakelessClickListener {
            run()
        }
        holder.root.setShakelessClickListener {
            run()
        }
        holder.root.setOnLongClickListener {
            showFloatMenu(it) {
                listOf(
                    simpleItem(0, "卸载插件", { true }) {
                        context.launch(Dispatchers.IO) {
                            PluginDatabase.forFile(context).pluginDao().delete(plugin)
                            withContext(Dispatchers.Main) {
                                adapter.removeItem(adapter.getGlobalPositionOf(this@LocalPluginCard))
                            }
                        }
                    }
                )
            }
            true
        }
    }

    fun run() {
        if (plugin.canRecordApi()) {
            val path = ApiParser.toPath(plugin)
            listener.navigateTo(plugin.title, path, -100)
        } else if (plugin.canPluginEnter()) {
            context.launch(Dispatchers.IO) {
                context.startPlugin(plugin)
            }
        }
    }
}