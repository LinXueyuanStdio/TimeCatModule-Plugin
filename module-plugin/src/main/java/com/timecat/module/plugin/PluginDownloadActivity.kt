package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.LocalPluginItem
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.module.plugin.download.AppDownloadMission
import com.timecat.page.base.friend.toolbar.BaseListActivity
import com.zpj.downloader.BaseMission
import com.zpj.downloader.DownloadManager
import com.zpj.downloader.DownloadManager.OnLoadMissionListener
import com.zpj.downloader.ZDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.ArrayList

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件更新管理，所有插件的更新，用户手动更新
 * @usage
 */
class PluginDownloadActivity : BaseListActivity(), DownloadManager.DownloadManagerListener {
    val adapter = BaseAdapter(null)

    override fun title(): String = "插件下载"
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    override fun loadData() {
        ZDownloader.getDownloadManager().addDownloadManagerListener(this)
        ZDownloader.getAllMissions { missions ->
            lifecycleScope.launch(Dispatchers.IO) {
                val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll()
                val downloadingList: MutableList<AppDownloadMission> = ArrayList<AppDownloadMission>()
                val downloadedList: MutableList<AppDownloadMission> = ArrayList<AppDownloadMission>()
                for (mission in missions) {
                    if (mission.isFinished()) {
                        downloadedList.add(mission)
                    } else {
                        downloadingList.add(mission)
                    }
                }
                downloadingMultiData.setData(downloadingList)
                downloadedMultiData.setData(downloadedList)
                downloadMultiDataList.clear()
                downloadMultiDataList.add(downloadingMultiData)
                downloadMultiDataList.add(downloadedMultiData)
                val items = allPlugin.map { LocalPluginItem(context, it) }
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