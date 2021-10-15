package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.MissionItem
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.zpj.downloader.BaseMission
import com.zpj.downloader.DownloadManager
import com.zpj.downloader.ZDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description TODO 这里不应该单个管理插件的下载，而应该管理所有类型的文件的下载
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PluginDownloadActivity)
class PluginDownloadActivity : BaseListActivity(), DownloadManager.DownloadManagerListener {
    val adapter = BaseAdapter(null)

    override fun title(): String = "插件下载"
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    override fun loadData() {
        ZDownloader.getDownloadManager().addDownloadManagerListener(this)
        ZDownloader.getAllMissions { missions ->
            lifecycleScope.launch(Dispatchers.IO) {
                val items = missions.map { MissionItem(context, it) }
                withContext(Dispatchers.Main) {
                    adapter.reload(items)
                }
            }
        }
    }

    override fun onDestroy() {
        ZDownloader.getDownloadManager().removeDownloadManagerListener(this)
        super.onDestroy()
    }

    override fun onMissionAdd(mission: BaseMission<*>) {
    }

    override fun onMissionDelete(mission: BaseMission<*>) {
    }

    override fun onMissionFinished(mission: BaseMission<*>) {
    }
}