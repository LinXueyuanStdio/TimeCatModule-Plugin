package com.timecat.module.skin.adapter

import android.content.Context
import android.view.View
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.ext.showFloatMenu
import com.timecat.middle.block.ext.simpleItem
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.skin.R
import com.timecat.module.skin.core.api.ApiParser
import com.timecat.module.skin.database.Skin
import com.timecat.module.skin.database.SkinDatabase
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
    val skin: Skin,
    val listener: ItemCommonListener
) : BaseItem<SkinCardVH>(skin.uuid) {

    override fun getLayoutRes(): Int = R.layout.plugin_item_local_plugin

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): SkinCardVH = SkinCardVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: SkinCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.title.text = skin.title
        val stateText = "管理器：${skin.managerVersionName}(${skin.managerVersionCode})"
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
                            SkinDatabase.forFile(context).pluginDao().delete(skin)
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
        if (skin.canRecordApi()) {
            val path = ApiParser.toPath(skin)
            listener.navigateTo(skin.title, path, -100)
        } else if (skin.canPluginEnter()) {
            NAV.go(RouterHub.PLUGIN_PluginRouterActivity, "plugin", skin as Serializable)
        }
    }
}