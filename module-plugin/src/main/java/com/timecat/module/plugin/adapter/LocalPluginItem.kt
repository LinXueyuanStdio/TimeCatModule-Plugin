package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.standard.progress.CircularProgressButton
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder

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
) : BaseItem<LocalPluginItem.DetailVH>(plugin.uuid) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
        val frontView: ConstraintLayout by lazy { root.findViewById(R.id.front_view) }
        val containerHint: View by lazy { root.findViewById(R.id.container_hint) }
        val avatar: FlipView by lazy { root.findViewById(R.id.avatar) }
        val divider: View by lazy { root.findViewById(R.id.divider) }
        val title: TextView by lazy { root.findViewById(R.id.title) }
        val state: TextView by lazy { root.findViewById(R.id.state) }
        val subType: CircularProgressButton by lazy { root.findViewById(R.id.sub_type) }
        val more: ImageView by lazy { root.findViewById(R.id.more) }
        val delay: TextView by lazy { root.findViewById(R.id.delay) }
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
        holder.subType.idleText = ""
    }
}