package com.same.ui

import android.view.ViewGroup
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Next
import com.timecat.middle.setting.BaseSettingActivity

class MainActivity : BaseSettingActivity() {
    override fun title(): String = "Debug"
    override fun addSettingItems(container: ViewGroup) {
        container.Next("插件市场") {
            NAV.go(RouterHub.PLUGIN_PluginCloudActivity)
        }
        container.Next("皮肤市场") {
            NAV.go(RouterHub.SKIN_SkinActivity)
        }
        container.Next("我的设计") {
            NAV.go(RouterHub.SKIN_SkinDesignActivity)
        }
    }
}