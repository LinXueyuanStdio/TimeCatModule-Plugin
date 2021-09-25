package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.PluginApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.ColorUtils
import com.timecat.middle.block.ext.bindSelected
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import com.zpj.downloader.BaseMission
import com.zpj.downloader.ZDownloader
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.joda.time.DateTime
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description 云端插件
 * @usage null
 */
class CloudPluginItem(
    val context: Context,
    /**
     * 云端插件对象
     */
    val block: Block,
    /**
     * 下载对象
     * 生命周期比列表项长
     */
    val mission: BaseMission<*>? = null,
    /**
     * 本地插件对象
     */
    val plugin: Plugin? = null,
) : BaseItem<PluginCardVH>(block.objectId) {

    override fun getLayoutRes(): Int = R.layout.plugin_item_cloud_plugin

    var missionHolder: MissionHolder? = null

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
        holder.title.text = block.title
        val head = AppBlock.fromJson(block.structure)
        holder.avatar.bindSelected(adapter.isSelected(adapter.getGlobalPositionOf(this)), head.header.avatar, ColorUtils.randomColor())
        val head2 = PluginApp.fromJson(head.structure)
        if (plugin != null) {
            val stateText = "管理器：${plugin.managerVersionName}(${plugin.managerVersionCode})"
            holder.state.text = stateText
        } else {
            val info = head2.updateInfo
            if (info.isNotEmpty()) {
                val newest = info.first()
                val stateText = "${newest.version_name}(${newest.version_code})    ${newest.size}    更新时间${DateTime(newest.lastUpdateTime)}"
                holder.state.text = stateText
            }
        }
        if (missionHolder == null) {
            missionHolder = MissionHolder(holder, mission) {
                run()
            }
        }
        holder.stateBtn.setShakelessClickListener {
            //升级
            if (plugin == null) {
                ZDownloader.download(head.url)
            } else {
                run()
            }
        }
        holder.root.setShakelessClickListener {
            NAV.go(RouterHub.APP_DETAIL_AppDetailActivity, "blockId", block.objectId)
        }
    }

    fun run() {
        if (plugin == null) {
            ToastUtil.w("下载")
            return
        }
        NAV.go(RouterHub.PLUGIN_PluginRouterActivity, "plugin", plugin as Serializable)
    }

    override fun onViewDetached(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: PluginCardVH, position: Int) {
        super.onViewDetached(adapter, holder, position)
        missionHolder?.detach()
    }
}