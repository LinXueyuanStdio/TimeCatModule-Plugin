/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.timecat.plugin.api.record.holder

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tencent.shadow.core.utils.Md5
import com.timecat.data.room.record.RoomRecord
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.timecat.plugin.api.record.RecordApi
import com.timecat.plugin.api.record.RecordApiWrapper
import java.io.File

class DynamicRecordApi(
    context: Context,
    var latestImplApk: File
) : RecordApi {
    private var recordApi: RecordApiWrapper = EmptyRecordApiWrapper()
    private var mCurrentImplMd5: String? = null
    fun release() {
        this.recordApi.onDynamicDestroy()
    }

    private fun updateImpl(context: Context) {
        val md5 = Md5.md5File(latestImplApk)
        if (!TextUtils.equals(mCurrentImplMd5, md5)) {
            val implLoader = RecordApiImplLoader(context, latestImplApk)
            val newImpl = implLoader.load()
            val state = Bundle()
            recordApi.onDynamicSaveInstanceState(state)
            recordApi.onDynamicDestroy()
            newImpl.onDynamicCreate(state)
            recordApi = newImpl
            mCurrentImplMd5 = md5
        }
    }

    //region RecordApi
    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        recordApi.loadContext(path, context, parentUuid, record, homeService)
    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        recordApi.loadContextRecord(path, context, parentUuid, homeService)
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        recordApi.loadForVirtualPath(context, parentUuid, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        recordApi.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }

    override fun onPause(context: Context) {
        recordApi.onPause(context)
    }

    override fun onResume(context: Context) {
        recordApi.onResume(context)
    }

    override fun onDestroy(context: Context) {
        recordApi.onDestroy(context)
    }
    //endregion

    init {
        updateImpl(context)
    }
}