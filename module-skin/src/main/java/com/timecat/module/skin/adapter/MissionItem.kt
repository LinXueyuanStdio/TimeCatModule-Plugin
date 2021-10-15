package com.timecat.module.skin.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.showDialog
import com.timecat.module.skin.R
import com.zpj.downloader.BaseMission
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class MissionItem(
    val context: Context,
    val mission: BaseMission<*>
) : BaseItem<SkinCardVH>(mission.uuid) {

    override fun getLayoutRes(): Int = R.layout.skin_item_local_plugin

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): SkinCardVH = SkinCardVH(view, adapter)

    var missionHolder: MissionHolder? = null

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: SkinCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        if (missionHolder == null) {
            missionHolder = FileMissionHolder(holder, mission) {
                adapter.removeItem(adapter.getGlobalPositionOf(this))
            }
        }

        holder.title.text = mission.getName()
        val stateText = "${mission.getDownloadedSizeStr()}/${mission.getFileSizeStr()}"
        holder.state.text = stateText
        when {
            mission.isPause() -> {
                holder.stateBtn.text = "继续"
                holder.stateBtn.setShakelessClickListener {
                    mission.restart()
                }
                holder.root.setShakelessClickListener {
                    mission.restart()
                }
            }
            mission.isRunning() -> {
                holder.stateBtn.text = "暂停"
                holder.stateBtn.setShakelessClickListener {
                    mission.pause()
                }
                holder.root.setShakelessClickListener {
                    mission.pause()
                }
            }
            else -> {
                holder.stateBtn.text = "查看"
                holder.stateBtn.setShakelessClickListener {
                    context.showDialog {
                        title(text = "下载任务详情")
                        message(text = "${mission.name}\nurl: ${mission.getOriginUrl()}")
                    }
                }
                holder.root.setShakelessClickListener {
                    context.showDialog {
                        title(text = "下载任务详情")
                        message(text = "${mission.name}\nurl: ${mission.getOriginUrl()}")
                    }
                }
            }
        }
    }

    override fun onViewDetached(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>, holder: SkinCardVH, position: Int) {
        super.onViewDetached(adapter, holder, position)
        missionHolder?.detach()
    }
}