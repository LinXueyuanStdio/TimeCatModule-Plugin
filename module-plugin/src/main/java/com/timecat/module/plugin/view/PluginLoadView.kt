package com.timecat.module.plugin.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ProgressBar

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
class PluginLoadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        ProgressBar(context).apply {

        }.also { addView(it) }
    }
}