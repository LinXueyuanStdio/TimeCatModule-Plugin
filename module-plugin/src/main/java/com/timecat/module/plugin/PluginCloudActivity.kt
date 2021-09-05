package com.timecat.module.plugin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
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
class PluginCloudActivity : BaseRefreshListActivity() {
    override fun title(): String = "插件市场"
    val adapter = BaseAdapter(null)
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    override fun onRefresh() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {

            }
        }
    }
}