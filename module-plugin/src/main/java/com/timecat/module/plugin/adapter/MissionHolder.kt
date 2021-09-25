package com.timecat.module.plugin.adapter

import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.zpj.downloader.BaseMission
import com.zpj.downloader.constant.Error
import kotlin.math.roundToInt

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/25
 * @description null
 * @usage null
 */
class MissionHolder(
    val holder: PluginCardVH,
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