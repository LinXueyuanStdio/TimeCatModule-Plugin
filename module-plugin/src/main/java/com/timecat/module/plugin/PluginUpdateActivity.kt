package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.adapter.LocalPluginItem
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
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

    override fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll()
            val items = allPlugin.map { LocalPluginItem(context, it) }
            withContext(Dispatchers.Main) {
                adapter.reload(items)
            }
        }
    }
}