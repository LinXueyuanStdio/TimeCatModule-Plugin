package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allPluginApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.CloudPluginItem
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
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
 * @description 插件市场
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PluginCloudActivity)
class PluginCloudActivity : BaseRefreshListActivity() {
    override fun title(): String = "插件市场"
    val adapter = BaseAdapter(null)
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        mStatefulLayout?.showLoading()
        dispose?.dispose()
        dispose = requestBlock {
            query = allPluginApp()
            onSuccess = { blocks ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val missions = ZDownloader.getAllMissions()
                    val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll(blocks.map { it.objectId })
                    val items = blocks.map { block ->
                        val localPlugin = allPlugin.find { block.objectId == it.uuid }
                        val mission = missions.find { block.objectId == it.uuid }
                        CloudPluginItem(context, block, mission, localPlugin)
                    }
                    withContext(Dispatchers.Main) {
                        adapter.reload(items)
                        mStatefulLayout?.showContent()
                    }
                }
            }
            onEmpty = {
                mRefreshLayout.isRefreshing = false
                adapter.clear()
                mStatefulLayout?.showEmpty("啥也没有")
            }
            onError = {
                mRefreshLayout.isRefreshing = false
                adapter.clear()
                mStatefulLayout?.showError("出错\n${it.msg}") {
                    onRefresh()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }
}