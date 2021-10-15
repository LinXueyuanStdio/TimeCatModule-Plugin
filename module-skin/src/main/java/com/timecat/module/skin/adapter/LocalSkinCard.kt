package com.timecat.module.skin.adapter

import android.content.Context
import android.view.View
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.ext.showFloatMenu
import com.timecat.middle.block.ext.simpleItem
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.skin.R
import com.timecat.module.skin.database.Skin
import com.timecat.module.skin.database.SkinDatabase
import com.timecat.module.skin.ext.applySkin
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class LocalSkinCard(
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
                            SkinDatabase.forFile(context).skinDao().delete(skin)
                            withContext(Dispatchers.Main) {
                                adapter.removeItem(adapter.getGlobalPositionOf(this@LocalSkinCard))
                            }
                        }
                    }
                )
            }
            true
        }
    }

    fun run() {
        context.launch(Dispatchers.IO) {
            context.applySkin(skin)
        }
    }
}