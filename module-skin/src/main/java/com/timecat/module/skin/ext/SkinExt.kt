package com.timecat.module.skin.ext

import android.content.Context
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.skin.CustomSDCardLoader
import com.timecat.identity.skin.SkinManager
import com.timecat.module.skin.database.Skin
import skin.support.SkinCompatManager

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/10
 * @description 必须放在 IO 线程里再调用以下方法
 * @usage null
 */
fun Context.applySkin(skin: Skin, listener: SkinCompatManager.SkinLoaderListener? = EmptySkinLoaderListener(this)) {
    SkinCompatManager.getInstance().loadSkin(skin.apkFilePath(this), listener, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD)
}

fun Context.applyDefaultSkin() {
    SkinManager.restoreDefaultTheme()
}

class EmptySkinLoaderListener(val ctx: Context) : SkinCompatManager.SkinLoaderListener {
    override fun onStart() {}
    override fun onSuccess() {
        ToastUtil.ok("喵")
    }

    override fun onFailed(errMsg: String) {
        ToastUtil.e(errMsg)
    }
}