package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.plugin.R
import com.timecat.module.plugin.core.api.ApiParser
import com.timecat.module.plugin.database.Plugin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder
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
) : BaseItem<LocalPluginCard.DetailVH>(plugin.uuid) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
        val frontView: ConstraintLayout by lazy { root.findViewById(R.id.front_view) }
        val containerHint: View by lazy { root.findViewById(R.id.container_hint) }
        val avatar: FlipView by lazy { root.findViewById(R.id.avatar) }
        val divider: View by lazy { root.findViewById(R.id.divider) }
        val title: TextView by lazy { root.findViewById(R.id.title) }
        val state: TextView by lazy { root.findViewById(R.id.state) }
        val subType: Button by lazy { root.findViewById(R.id.sub_type) }
        val progress_bar: ProgressBar by lazy { root.findViewById(R.id.progress_bar) }
    }

    override fun getLayoutRes(): Int = R.layout.plugin_item_local_plugin

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.title.text = plugin.title
        val stateText = "管理器：${plugin.managerVersionName}(${plugin.managerVersionCode})\n" +
            "插件包：${plugin.pluginVersionName}(${plugin.pluginVersionCode})"
        holder.state.text = stateText
        holder.subType.text = "打开"
        holder.subType.setShakelessClickListener {
            NAV.go(RouterHub.PLUGIN_PluginRouterActivity, "plugin", plugin as Serializable)
        }
        holder.root.setShakelessClickListener {
            //打开
            val path = ApiParser.toPath(plugin)
            listener.navigateTo(plugin.title, path, -100)
        }

        if (payloads.isNullOrEmpty()) return
        val obj = payloads.first()
        if (obj is Block) {
            val block = obj
            // TODO 更新
        }
    }
}