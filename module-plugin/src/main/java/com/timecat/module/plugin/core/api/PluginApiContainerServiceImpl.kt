package com.timecat.module.plugin.core.api

import android.content.Context
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.module.plugin.host.Shadow
import com.timecat.plugin.api.record.RecordApi
import com.xiaojinzi.component.anno.ServiceAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description 具体插件的动态化，这个类代理了动态化的 apk 实现
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_PluginApiContainerService])
class PluginApiContainerServiceImpl : ContainerService {

    lateinit var recordApi: RecordApi

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        context.launch(Dispatchers.IO) {
            val uuid = ApiParser.getUuid(parentUuid)
            val plugin = PluginDatabase.forFile(context).pluginDao().getByUuid(uuid)
            recordApi = Shadow.getRecordApi(context, plugin)
            withContext(Dispatchers.Main) {
                homeService.loadContextRecord(null)
            }
        }
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        recordApi.loadContext(path, context, parentUuid, record, homeService)
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        recordApi.loadForVirtualPath(context, parentUuid, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        recordApi.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }
}