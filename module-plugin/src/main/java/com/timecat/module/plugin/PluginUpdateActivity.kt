package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allPluginApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.CloudPluginItem
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.zpj.downloader.ZDownloader
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件更新管理，所有插件的更新，用户手动更新
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PluginUpdateActivity)
class PluginUpdateActivity : BaseListActivity() {
    val adapter = BaseAdapter(null)

    override fun title(): String = "插件更新"
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun loadData() {
        mStatefulLayout?.showLoading("检查更新中")
        lifecycleScope.launch(Dispatchers.IO) {
            val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll()
            loadCloud(allPlugin)
        }
    }

    private fun loadCloud(allPlugin: List<Plugin>) {
        dispose?.dispose()
        dispose = requestBlock {
            query = allPluginApp().apply {
                whereContainedIn("objectId", allPlugin.map { it.uuid })
            }
            onSuccess = { blocks ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val missions = ZDownloader.getAllMissions()
                    val pluginUuids = blocks.map { it.objectId }
                    val items = blocks.map { block ->
                        val localPlugin = allPlugin.find { it.uuid in pluginUuids }
                        val mission = missions.find { localPlugin?.managerApkFile(this@PluginUpdateActivity)?.parent == it.downloadPath }
                        CloudPluginItem(context, block, mission, localPlugin)
                    }
                    withContext(Dispatchers.Main) {
                        if (items.isEmpty()) {
                            mStatefulLayout?.showEmpty("您的插件全部是最新的！")
                        } else {
                            adapter.reload(items)
                            mStatefulLayout?.showContent()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }
}