package com.timecat.module.skin.ext

import android.content.Context
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.identity.readonly.PluginHub
import com.timecat.middle.block.ext.prepareShowInService
import com.timecat.module.skin.database.Skin
import com.timecat.module.skin.host.Shadow

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/10
 * @description 必须放在 IO 线程里再调用以下方法
 * @usage null
 */
fun Context.startPlugin(skin: Skin) {
    val enterCallback = EmptyEnterCallback(this)
    val bundle = Bundle()
    val pluginManager = Shadow.getPluginManager(this, skin)
    pluginManager.enter(this, -1, bundle, enterCallback)
}

fun Context.startPluginActivity(skin: Skin, partKey: String = "plugin-shadow-app", activityName: String) {
    val bundle = Bundle()
    bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partKey)
    bundle.putString(PluginHub.KEY_CLASSNAME, activityName)
    startPluginActivity(skin, bundle)
}

fun Context.startPluginActivity(skin: Skin, bundle: Bundle) {
    val enterCallback = EmptyEnterCallback(this)
    val pluginManager = Shadow.getPluginManager(this, skin)
    pluginManager.enter(this, PluginHub.FROM_ID_START_ACTIVITY, bundle, enterCallback)
}

class EmptyEnterCallback(val ctx: Context) : EnterCallback {
    var dialog: MaterialDialog? = null
    override fun onShowLoadingView(view: View) {
        dialog = MaterialDialog(ctx).show {
            prepareShowInService()
            lifecycleOwner()
            customView(view = view)
            cancelOnTouchOutside(false)
        }
    }

    override fun onCloseLoadingView() {
        dialog?.dismiss()
    }

    override fun onEnterComplete() {
    }
}