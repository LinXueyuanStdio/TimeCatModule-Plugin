package com.same.ui

import android.view.ViewGroup
import com.didichuxing.doraemonkit.DoKit
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Next
import com.timecat.middle.setting.BaseSettingActivity

class MainActivity : BaseSettingActivity() {
    override fun title(): String = "Debug"
    override fun addSettingItems(container: ViewGroup) {
        DoKit.Builder(application)
            .productId("需要使用平台功能的话，需要到dokit.cn平台申请id")
            .build()
        LogUtil.OPEN_LOG = true
        LogUtil.DEBUG = true

        container.Next("插件市场") {
            NAV.go(RouterHub.PLUGIN_PluginCloudActivity)
        }
        container.Next("插件下载中心") {
            NAV.go(RouterHub.PLUGIN_PluginDownloadActivity)
        }

        container.Next("皮肤市场") {
            NAV.go(RouterHub.SKIN_SkinCloudActivity)
        }
        container.Next("皮肤下载中心") {
            NAV.go(RouterHub.SKIN_SkinDownloadActivity)
        }
        container.Next("我的设计") {
            NAV.go(RouterHub.SKIN_SkinDesignActivity)
        }
    }
}