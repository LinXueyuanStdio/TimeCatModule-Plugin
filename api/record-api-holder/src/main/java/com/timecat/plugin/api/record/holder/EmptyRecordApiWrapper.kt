package com.timecat.plugin.api.record.holder

import android.content.Context
import android.os.Bundle
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.FallbackContext
import com.timecat.middle.block.service.HomeService
import com.timecat.plugin.api.record.RecordApiWrapper

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
class EmptyRecordApiWrapper : RecordApiWrapper {
    val emptyRecordApi = FallbackContext()
    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        emptyRecordApi.loadContext(path, context, parentUuid, record, homeService)
    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        emptyRecordApi.loadContextRecord(path, context, parentUuid, homeService)
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        emptyRecordApi.loadForVirtualPath(context, parentUuid, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        emptyRecordApi.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }

    override fun onPause(context: Context) {
        LogUtil.e("onPause")
        emptyRecordApi.onPause(context)
    }

    override fun onResume(context: Context) {
        LogUtil.e("onResume")
        emptyRecordApi.onResume(context)
    }

    override fun onDestroy(context: Context) {
        LogUtil.e("onDestroy")
        emptyRecordApi.onDestroy(context)
    }

    override fun onDynamicCreate(bundle: Bundle) {
        LogUtil.e("onDynamicCreate")
    }

    override fun onDynamicSaveInstanceState(outState: Bundle) {
        LogUtil.e("onDynamicSaveInstanceState")
    }

    override fun onDynamicDestroy() {
        LogUtil.e("onDynamicDestroy")
    }
}