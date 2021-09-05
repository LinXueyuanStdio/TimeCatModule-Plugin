package com.timecat.module.plugin

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.identity.readonly.PluginHub
import com.timecat.module.plugin.host.PluginHelper
import com.timecat.module.plugin.host.Shadow

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件路由，对插件的调用都是对 PluginRouterActivity 的调用
 * 职责：综合管理中转，调用插件
 *      控制插件生命周期，必要时自动下载插件、更新插件
 * @usage ARouter
 */
class PluginRouterActivity :Activity(){

    private lateinit var mViewGroup: ViewGroup

    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plugin_activity_router)
        mViewGroup = findViewById(R.id.container)
        startPlugin()
    }

    fun startPlugin() {
        PluginHelper.getInstance().singlePool.execute {
            val bundle = Bundle()
            bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, PluginHelper.getInstance().pluginZipFile.absolutePath)
            bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, intent.getStringExtra(PluginHub.KEY_PLUGIN_PART_KEY))
            bundle.putString(PluginHub.KEY_CLASSNAME, intent.getStringExtra(PluginHub.KEY_CLASSNAME))

            val pluginManager = Shadow.getPluginManager(PluginHelper.getInstance().pluginManagerFile)
            pluginManager.enter(this@PluginRouterActivity, PluginHub.FROM_ID_START_ACTIVITY, bundle, object : EnterCallback {
                override fun onShowLoadingView(view: View) {
                    mHandler.post { mViewGroup.addView(view) }
                }

                override fun onCloseLoadingView() {
                    finish()
                }

                override fun onEnterComplete() {}
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewGroup.removeAllViews()
    }
}