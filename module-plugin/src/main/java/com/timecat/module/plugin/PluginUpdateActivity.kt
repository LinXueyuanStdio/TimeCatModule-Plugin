package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allPluginApp
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.LocalPluginItem
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
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
class PluginUpdateActivity : BaseListActivity() {
    val adapter = BaseAdapter(null)

    override fun title(): String = "插件更新"
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun loadData() {
        mStatefulLayout?.showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll()
            val items = allPlugin.map { plugin ->
                LocalPluginItem(context, plugin)
            }
            withContext(Dispatchers.Main) {
                if (items.isEmpty()) {
                    mStatefulLayout?.showEmpty()
                } else {
                    mStatefulLayout?.showContent()
                    adapter.reload(items)
                    loadCloud(allPlugin)
                }
            }
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
                    val id2Block = blocks.associate { it.objectId to it }
                    val items = adapter.currentItems.filter { it.id in id2Block }
                    withContext(Dispatchers.Main) {
                        items.forEach {
                            adapter.updateItem(it, id2Block[it.id])
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