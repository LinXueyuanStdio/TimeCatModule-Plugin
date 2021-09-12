package com.timecat.module.plugin.core

import android.content.Context
import com.google.android.material.chip.Chip
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.endless.NotMoreItem
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.service.*
import com.timecat.module.plugin.adapter.LocalPluginCard
import com.timecat.module.plugin.adapter.LocalPluginItem
import com.timecat.module.plugin.database.PluginDatabase
import com.xiaojinzi.component.anno.ServiceAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description 插件组件的数据库
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_PluginContainerService])
class PluginContainerServiceImpl : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        homeService.loadMenu(EmptyMenuContext())
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadHeader(listOf())
        homeService.loadChipButtons(listOf(
            Chip(context).apply {
                text = "插件市场"
                setShakelessClickListener {
                    NAV.go(RouterHub.PLUGIN_PluginCloudActivity)
                }
            },
            Chip(context).apply {
                text = "更新"
                setShakelessClickListener {
                    NAV.go(RouterHub.PLUGIN_PluginUpdateActivity)
                }
            }
        ))
        homeService.reloadData()
    }

    private val pageSize: Int = 10
    private val notMoreItem: NotMoreItem = NotMoreItem()

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {

        context.launch(Dispatchers.IO) {
            val listener = homeService.itemCommonListener()
            configAdapterEndlessLoad(listener.adapter(), false, pageSize, 4, notMoreItem) { lastPosition: Int, currentPage: Int ->
                listener.loadMore(lastPosition, currentPage)
            }
            val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll(pageSize, 0)
            val currentItems = allPlugin.map { plugin ->
                LocalPluginCard(context, plugin, listener)
            }
            withContext(Dispatchers.Main) {
                callback.onVirtualLoadSuccess(currentItems)
            }
        }
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        context.launch(Dispatchers.IO) {
            val listener = homeService.itemCommonListener()
            val allPlugin = PluginDatabase.forFile(context).pluginDao().getAll(pageSize, offset)
            val currentItems = allPlugin.map { plugin ->
                LocalPluginCard(context, plugin, listener)
            }
            withContext(Dispatchers.Main) {
                callback.onVirtualLoadSuccess(currentItems)
            }
        }
    }
}