package com.timecat.module.plugin.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.host.Shadow
import com.timecat.plugin.api.record.holder.DynamicRecordApi
import com.zpj.downloader.BaseMission
import com.zpj.downloader.constant.Error
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder
import kotlin.math.roundToInt

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
) : BaseItem<CloudPluginItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
        val frontView: ConstraintLayout by lazy { root.findViewById(R.id.front_view) }
        val containerHint: View by lazy { root.findViewById(R.id.container_hint) }
        val avatar: FlipView by lazy { root.findViewById(R.id.avatar) }
        val divider: View by lazy { root.findViewById(R.id.divider) }
        val title: TextView by lazy { root.findViewById(R.id.title) }
        val state: TextView by lazy { root.findViewById(R.id.state) }
        val stateBtn: Button by lazy { root.findViewById(R.id.sub_type) }
        val progress_bar: ProgressBar by lazy { root.findViewById(R.id.progress_bar) }
    }

    override fun getLayoutRes(): Int = R.layout.plugin_item_cloud_plugin

    var missionListener: MissionHolder? = null

    class MissionHolder(
        val holder: DetailVH,
        var mission: BaseMission<*>? = null,
        val onRun: () -> Unit,
    ) : BaseMission.MissionListener {
        init {
            mission?.addListener(this)
        }

        fun detach() {
            mission?.removeListener(this)
        }

        //region BaseMission.MissionListener
        override fun onInit() {
        }

        override fun onStart() {
            holder.progress_bar.beVisible()
            holder.stateBtn.text = "下载中"
            holder.stateBtn.setShakelessClickListener {
                mission?.pause()
            }
        }

        override fun onPause() {
            holder.stateBtn.text = "暂停"
            holder.stateBtn.setShakelessClickListener {
                mission?.restart()
            }
        }

        override fun onWaiting() {
            holder.stateBtn.text = "等待中"
            holder.stateBtn.setOnClickListener(null)
        }

        override fun onRetry() {
            holder.stateBtn.text = "下载中"
            holder.stateBtn.setShakelessClickListener {
                mission?.pause()
            }
        }

        override fun onProgress(update: BaseMission.ProgressInfo) {
            holder.progress_bar.progress = update.progress.roundToInt()
        }

        override fun onFinish() {
            holder.progress_bar.beGone()
            holder.stateBtn.text = "打开"
            holder.stateBtn.setShakelessClickListener {
                onRun()
            }
        }

        override fun onError(e: Error?) {
            holder.progress_bar.beGone()
            holder.stateBtn.text = "打开"
            holder.stateBtn.setShakelessClickListener {
                onRun()
            }
        }

        override fun onDelete() {
        }

        override fun onClear() {
        }
        //endregion
    }

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
        if (missionListener == null) {
            missionListener = MissionHolder(holder, mission) {
                run()
            }
        }
        holder.stateBtn.setShakelessClickListener {
            //升级
//            ZDownloader.download()
        }
        holder.root.setShakelessClickListener {
            run()
        }
    }

    fun run() {
        if (plugin == null) return
        val api = DynamicRecordApi(context, plugin.managerApkFile(context))
    }

    override fun onViewDetached(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: DetailVH, position: Int) {
        super.onViewDetached(adapter, holder, position)
        missionListener?.detach()
    }
}