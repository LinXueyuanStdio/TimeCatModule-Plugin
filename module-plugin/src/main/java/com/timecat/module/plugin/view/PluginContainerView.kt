package com.timecat.module.plugin.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.component.commonsdk.extension.beGone

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
class PluginContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), EnterCallback {
    val title: TextView = TextView(context).apply {
        gravity = Gravity.CENTER
    }
    val bar: ProgressBar = ProgressBar(context)

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        addView(bar)
        addView(title)
        title.text = "空闲"
    }

    override fun onShowLoadingView(view: View?) {
        title.text = "onShowLoadingView"
        addView(view)
    }

    override fun onCloseLoadingView() {
        title.text = "onCloseLoadingView"
    }

    override fun onEnterComplete() {
        title.text = "onEnterComplete"
        bar.beGone()
    }
}