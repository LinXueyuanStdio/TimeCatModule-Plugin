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
import com.timecat.middle.block.ext.launch
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.module.plugin.database.PluginDir
import com.timecat.module.plugin.download.PluginDownloadNotificationInterceptor
import com.timecat.module.plugin.ext.startPlugin
import com.timecat.module.plugin.ext.toPlugin
import com.timecat.module.plugin.ext.versionCode
import com.zpj.downloader.BaseMission
import com.zpj.downloader.ZDownloader
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.coroutines.Dispatchers
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
    var mission: BaseMission<*>? = null,
    /**
     * 本地插件对象
     */
    var plugin: Plugin? = null,
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
            val stateText = "管理器：${plugin?.managerVersionName}(${plugin?.managerVersionCode})"
            holder.state.text = stateText
        } else {
            val info = head2.updateInfo
            if (info.isNotEmpty()) {
                val newest = info.first()
                val stateText = "${newest.version_name}(${newest.version_code})    ${newest.size}\n" +
                    "更新时间${DateTime(newest.lastUpdateTime)}"
                holder.state.text = stateText
            }
        }
        if (missionHolder == null) {
            missionHolder = MissionHolder(holder, mission, {
                save()
            }) {
                run()
            }
        }
        if (plugin != null) {
            if (block.versionCode() > plugin?.managerVersionCode ?: 1) {
                holder.stateBtn.text = "升级"
            } else {
                holder.stateBtn.text = "打开"
            }
        } else {
            holder.stateBtn.text = "安装"
        }
        holder.stateBtn.setShakelessClickListener {
            if (plugin != null) {
                if (block.versionCode() > plugin?.managerVersionCode ?: 1) {
                    download(head.url, holder)
                } else {
                    run()
                }
            } else {
                download(head.url, holder)
            }
        }
        holder.root.setShakelessClickListener {
            NAV.go(RouterHub.APP_DETAIL_AppDetailActivity, "blockId", block.objectId)
        }
    }

    fun save() {
        context.launch(Dispatchers.IO) {
            val newPlugin = block.toPlugin()
            PluginDatabase.forFile(context).pluginDao().insert(newPlugin)
            plugin = newPlugin
        }
    }

    fun download(url: String, holder: PluginCardVH) {
        missionHolder?.detach()
        val newPlugin = block.toPlugin()
        mission = ZDownloader.download(url, PluginDir.sPluginManagerName)
            .setDownloadPath(newPlugin.managerApkFile(context).parent)
            .setNotificationInterceptor(PluginDownloadNotificationInterceptor())
        missionHolder = MissionHolder(holder, mission, {
            save()
        }) {
            run()
        }
        missionHolder?.start()
    }

    fun run() {
        if (plugin == null) {
            ToastUtil.w("下载")
            return
        }
        context.launch(Dispatchers.IO) {
            context.startPlugin(plugin!!)
        }
    }

    override fun onViewDetached(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: PluginCardVH, position: Int) {
        super.onViewDetached(adapter, holder, position)
        missionHolder?.detach()
    }
}