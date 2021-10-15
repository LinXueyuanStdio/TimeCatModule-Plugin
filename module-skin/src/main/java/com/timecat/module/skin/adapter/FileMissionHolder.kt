package com.timecat.module.skin.adapter

import com.timecat.layout.ui.layout.setShakelessClickListener
import com.zpj.downloader.BaseMission
import com.zpj.downloader.ProgressUpdater
import com.zpj.downloader.constant.Error

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/10
 * @description null
 * @usage null
 */
class FileMissionHolder(
    holder: SkinCardVH,
    mission: BaseMission<*>,
    onEnd: () -> Unit,
) : MissionHolder(holder, mission, { onEnd() }, { onEnd() }) {
    override fun onStart() {
        super.onStart()
        holder.root.setShakelessClickListener {
            mission?.pause()
        }
    }

    override fun onPaused() {
        super.onPaused()
        holder.root.setShakelessClickListener {
            mission?.restart()
        }
    }

    override fun onWaiting() {
        super.onWaiting()
        holder.root.setOnClickListener(null)
    }

    override fun onRetrying() {
        super.onRetrying()
        holder.root.setShakelessClickListener {
            mission?.pause()
        }
    }

    override fun onProgress(update: ProgressUpdater) {
        super.onProgress(update)
        val stateText = "${update.progressStr}/${update.fileSizeStr}  ${update.speedStr}"
        holder.state.text = stateText
    }


    override fun onFinished() {
        super.onFinished()
        holder.state.text = "完成"
        holder.root.setShakelessClickListener {
            onRun()
        }
    }

    override fun onError(e: Error?) {
        super.onError(e)
        holder.root.setShakelessClickListener {
            mission?.restart()
        }
    }
}