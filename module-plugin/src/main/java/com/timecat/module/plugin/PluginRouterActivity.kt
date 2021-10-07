package com.timecat.module.plugin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.PluginHub
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.ext.launch
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.host.Shadow
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件路由，对插件的调用都是对 PluginRouterActivity 的调用
 * 职责：综合管理中转，调用插件
 *      控制插件生命周期，必要时自动下载插件、更新插件
 * @usage ARouter
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PluginRouterActivity)
class PluginRouterActivity : Activity() {
    @AttrValueAutowiredAnno("plugin")
    var plugin: Plugin? = null
    @AttrValueAutowiredAnno(PluginHub.KEY_PLUGIN_PART_KEY)
    var partKey: String? = null
    @AttrValueAutowiredAnno(PluginHub.KEY_CLASSNAME)
    var className: String? = null

    private lateinit var mViewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plugin_activity_router)
        mViewGroup = findViewById(R.id.container)
        if (plugin == null) {
            finish()
        } else {
            this.launch(Dispatchers.IO) {
                startPlugin(plugin!!)
            }
        }
    }

    private fun startPlugin(plugin: Plugin) {
        val bundle = Bundle()
        bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partKey)
        bundle.putString(PluginHub.KEY_CLASSNAME, className)

        val pluginManager = Shadow.getPluginManager(this, plugin)
        if (className == null) {
            pluginManager.enter(this, -1, bundle, enterCallback)
        } else {
            pluginManager.enter(this, PluginHub.FROM_ID_START_ACTIVITY, bundle, enterCallback)
        }
    }

    val enterCallback :EnterCallback = object : EnterCallback {
        override fun onShowLoadingView(view: View) {
            runOnUiThread {
                mViewGroup.addView(view)
            }
        }

        override fun onCloseLoadingView() {
            finish()
        }

        override fun onEnterComplete() {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewGroup.removeAllViews()
    }
}