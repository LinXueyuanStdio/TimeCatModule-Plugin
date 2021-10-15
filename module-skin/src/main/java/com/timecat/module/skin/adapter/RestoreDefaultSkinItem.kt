package com.timecat.module.skin.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.ColorUtils
import com.timecat.middle.block.ext.bindSelected
import com.timecat.module.skin.R
import com.timecat.module.skin.ext.applyDefaultSkin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description 云端插件
 * @usage null
 */
class RestoreDefaultSkinItem(
    val context: Context,
) : BaseItem<SkinCardVH>("restore_default") {

    override fun getLayoutRes(): Int = R.layout.skin_item_cloud_plugin

    var missionHolder: MissionHolder? = null

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
        holder.title.text = "恢复默认"
        holder.avatar.bindSelected(adapter.isSelected(adapter.getGlobalPositionOf(this)), "R.drawable.ic_launcher", ColorUtils.randomColor())
        holder.state.text = "恢复默认"
        holder.stateBtn.text = "恢复"
        holder.stateBtn.setShakelessClickListener {
            context.applyDefaultSkin()
        }
        holder.root.setShakelessClickListener {
            context.applyDefaultSkin()
        }
    }

    override fun onViewDetached(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: SkinCardVH, position: Int) {
        super.onViewDetached(adapter, holder, position)
        missionHolder?.detach()
    }
}