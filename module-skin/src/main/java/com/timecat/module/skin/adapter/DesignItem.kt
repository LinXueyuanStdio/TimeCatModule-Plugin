package com.timecat.module.skin.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.skin.ColorPickerData
import com.timecat.module.skin.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class DesignItem(
    val context: Context,
    val item: ColorPickerData,
    val listener: OnColorChangedListener
) : BaseItem<DesignItem.DesignCardVH>(item.name) {
    interface OnColorChangedListener {
        fun onColorChanged(data: ColorPickerData)
    }

    class DesignCardVH(val root: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(root, adapter) {
        val preview: Button by lazy { root.findViewById(R.id.preview) }
        val color_seek_bar: Slider by lazy { root.findViewById(R.id.color_seek_bar) }
        val alpha_seek_bar: Slider by lazy { root.findViewById(R.id.alpha_seek_bar) }
        val title: TextView by lazy { root.findViewById(R.id.name) }
    }

    override fun getLayoutRes(): Int = R.layout.skin_item_design_my_theme

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DesignCardVH = DesignCardVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DesignCardVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val color = Integer.valueOf(item.getValue(), 16)
        val alpha = Integer.valueOf(item.getAlpha(), 16)
        holder.title.text = item.name
        val mColorSeekBar = holder.color_seek_bar
        mColorSeekBar.value = color.toFloat()
        val mAlphaSeekBar = holder.alpha_seek_bar
        mAlphaSeekBar.value = alpha.toFloat()
        holder.preview.setText(item.color)
        holder.preview.setTextColor(Color.parseColor(item.color))
        holder.preview.setBackgroundResource(item.colorRes)
        mColorSeekBar.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            val colorStr = Integer.toHexString(value.toInt())
            item.setValue(colorStr)
            holder.preview.setText(item.color)
            holder.preview.setTextColor(Color.parseColor(item.color))
            listener.onColorChanged(item)
        })
        mAlphaSeekBar.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            val alphaStr = Integer.toHexString(value.toInt())
            item.setAlpha(alphaStr)
            holder.preview.setText(item.color)
            holder.preview.setTextColor(Color.parseColor(item.color))
            listener.onColorChanged(item)
        })
    }

}