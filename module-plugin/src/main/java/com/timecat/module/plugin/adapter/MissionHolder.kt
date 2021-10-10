package com.timecat.module.plugin.adapter

import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.plugin.database.PluginDatabase
import com.zpj.downloader.BaseMission
import com.zpj.downloader.ProgressUpdater
import com.zpj.downloader.constant.Error
import kotlin.math.roundToInt

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/25
 * @description null
 * @usage null
 */
open class MissionHolder(
    val holder: PluginCardVH,
    var mission: BaseMission<*>? = null,
    val onSave: () -> Unit,
    val onRun: () -> Unit,
) : BaseMission.MissionListener {
    init {
        mission?.addListener(this)
    }

    fun start() {
        mission?.start()
        ToastUtil.w_long("正在下载到 ${mission?.downloadPath}")
        LogUtil.e(mission)
    }

    fun detach() {
        mission?.removeListener(this)
    }

    //region BaseMission.MissionListener
    override fun onPrepare() {
    }

    override fun onStart() {
        holder.progress_bar.beVisible()
        holder.stateBtn.text = "下载中"
        holder.stateBtn.setShakelessClickListener {
            mission?.pause()
        }
    }

    override fun onPaused() {
        holder.stateBtn.text = "继续"
        holder.stateBtn.setShakelessClickListener {
            mission?.restart()
        }
    }

    override fun onWaiting() {
        holder.stateBtn.text = "等待中"
        holder.stateBtn.setOnClickListener(null)
    }

    override fun onRetrying() {
        holder.stateBtn.text = "下载中"
        holder.stateBtn.setShakelessClickListener {
            mission?.pause()
        }
    }

    override fun onProgress(update: ProgressUpdater) {
        holder.progress_bar.progress = update.progress.roundToInt()
    }

    override fun onFinished() {
        onSave()
        holder.progress_bar.beGone()
        holder.stateBtn.text = "打开"
        holder.stateBtn.setShakelessClickListener {
            onRun()
        }
    }

    override fun onError(e: Error?) {
        holder.progress_bar.beGone()
        holder.stateBtn.text = "下载"
        holder.stateBtn.setShakelessClickListener {
            mission?.restart()
        }
    }

    override fun onDelete() {
    }

    override fun onClear() {
    }
    //endregion
}