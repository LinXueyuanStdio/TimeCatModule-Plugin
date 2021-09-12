package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import com.zpj.downloader.BaseMission
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description 云端插件
 * @usage null
 */
class CloudPluginItem(
    val context: Context,
    val block: Block,
    val mission: BaseMission<*>? = null,
    val plugin: Plugin? = null,
) : BaseItem<CloudPluginItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
        val frontView: ConstraintLayout by lazy { root.findViewById(R.id.front_view) }
        val containerHint: View by lazy { root.findViewById(R.id.container_hint) }
        val avatar: FlipView by lazy { root.findViewById(R.id.avatar) }
        val divider: View by lazy { root.findViewById(R.id.divider) }
        val title: TextView by lazy { root.findViewById(R.id.title) }
        val state: TextView by lazy { root.findViewById(R.id.state) }
        val subType: Button by lazy { root.findViewById(R.id.sub_type) }
        val progress_bar: ProgressBar by lazy { root.findViewById(R.id.progress_bar) }

        fun bindMission(mission: BaseMission<*>) {

        }
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
        holder.title.text = block.title
        plugin?.let {
            val stateText = "管理器：${plugin.managerVersionName}(${plugin.managerVersionCode})\n" +
                "插件包：${plugin.pluginVersionName}(${plugin.pluginVersionCode})"
            holder.state.text = stateText
        }
        mission?.let {
            holder.bindMission(it)
        }
        holder.subType.setShakelessClickListener {
            //升级

        }
        holder.root.setShakelessClickListener {
            //打开
        }
    }
}