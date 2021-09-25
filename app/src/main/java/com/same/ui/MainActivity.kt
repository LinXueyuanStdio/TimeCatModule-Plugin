package com.same.ui

import android.content.Intent
import android.view.ViewGroup
import com.timecat.layout.ui.business.form.Next
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.plugin.PluginCloudActivity

class MainActivity : BaseSettingActivity() {
    override fun title(): String = "Debug"
    override fun addSettingItems(container: ViewGroup) {
        container.Next("插件市场") {
            startActivity(Intent(this, PluginCloudActivity::class.java))
        }
    }
}