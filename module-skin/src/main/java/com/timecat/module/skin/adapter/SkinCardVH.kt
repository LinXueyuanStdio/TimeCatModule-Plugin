package com.timecat.module.skin.adapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.module.skin.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flipview.FlipView
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/25
 * @description null
 * @usage null
 */
class SkinCardVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
    val frontView: ConstraintLayout by lazy { root.findViewById(R.id.front_view) }
    val containerHint: View by lazy { root.findViewById(R.id.container_hint) }
    val avatar: FlipView by lazy { root.findViewById(R.id.avatar) }
    val divider: View by lazy { root.findViewById(R.id.divider) }
    val title: TextView by lazy { root.findViewById(R.id.title) }
    val state: TextView by lazy { root.findViewById(R.id.state) }
    val stateBtn: Button by lazy { root.findViewById(R.id.sub_type) }
    val progress_bar: ProgressBar by lazy { root.findViewById(R.id.progress_bar) }
}