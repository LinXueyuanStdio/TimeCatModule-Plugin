package com.timecat.module.skin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allPluginApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.skin.adapter.CloudSkinItem
import com.timecat.module.skin.database.SkinDatabase
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
class SkinCloudActivity : BaseRefreshListActivity() {
    override fun title(): String = "插件市场"
    val adapter = BaseAdapter(null)
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        mStatefulLayout?.showLoading("加载中，请耐心等待...")
        dispose?.dispose()
        dispose = requestBlock {
            query = allPluginApp()
            onSuccess = { blocks ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val missions = ZDownloader.getAllMissions()
                    val pluginUuids = blocks.map { it.objectId }
                    val allPlugin = SkinDatabase.forFile(context).pluginDao().getAll(pluginUuids)
                    val items = blocks.map { block ->
                        val localPlugin = allPlugin.find { it.uuid in pluginUuids }
                        val mission = missions.find { localPlugin?.managerApkFile(this@SkinCloudActivity)?.parent == it.downloadPath }
                        CloudSkinItem(context, block, mission, localPlugin)
                    }
                    withContext(Dispatchers.Main) {
                        mRefreshLayout.isRefreshing = false
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