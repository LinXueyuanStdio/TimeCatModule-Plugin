package com.timecat.module.skin

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter.replaceData
import com.chad.library.adapter.base.viewholder.BaseViewHolder.setText
import com.chad.library.adapter.base.viewholder.BaseViewHolder.getView
import com.chad.library.adapter.base.viewholder.BaseViewHolder.setTextColor
import com.chad.library.adapter.base.viewholder.BaseViewHolder.setBackgroundResource
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.identity.readonly.RouterHub
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.timecat.module.skin.SkinDesignActivity.ColorPickerAdapter
import com.timecat.module.skin.SkinDesignActivity.ColorPickerData
import skin.support.content.res.SkinCompatUserThemeManager
import skin.support.SkinCompatManager
import com.timecat.module.skin.R
import skin.support.content.res.ColorState
import android.text.TextUtils
import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.slider.Slider
import java.util.ArrayList

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/30
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinDesignActivity)
class SkinDesignActivity : BaseRefreshListActivity() {
    private lateinit var mAdapter: ColorPickerAdapter
    override fun title(): String {
        return "我的设计"
    }

    override fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder?> {
        mAdapter = ColorPickerAdapter()
        mAdapter!!.setOnColorChangedListener(object : OnColorChangedListener {
            override fun onColorChanged(data: ColorPickerData?) {
                val color = data!!.color
                SkinCompatUserThemeManager.get().addColorState(data.colorRes, color)
                SkinCompatUserThemeManager.get().apply()
                SkinCompatManager.getInstance().notifyUpdateSkin()
            }
        })
        return mAdapter
    }

    override fun onRefresh() {
        mAdapter!!.replaceData(prepareData())
        mRefreshLayout.isRefreshing = false
    }

    private fun prepareData(): List<ColorPickerData> {
        val dataList: MutableList<ColorPickerData> = ArrayList()
        dataList.add(generateData(R.color.master_colorPrimary))
        dataList.add(generateData(R.color.master_colorPrimaryDark))
        dataList.add(generateData(R.color.master_colorAccent))
        dataList.add(generateData(R.color.master_textColorPrimary))
        dataList.add(generateData(R.color.text_color))
        dataList.add(generateData(R.color.master_background))
        dataList.add(generateData(R.color.master_divider))
        return dataList
    }

    private fun generateData(@ColorRes colorRes: Int): ColorPickerData {
        val state = SkinCompatUserThemeManager.get().getColorState(colorRes)
        val data = ColorPickerData()
        data.colorRes = colorRes
        if (state == null) {
            data.name = resources.getResourceEntryName(colorRes)
        } else {
            data.name = state.colorName
            val colorDefault = state.colorDefault
            if (!TextUtils.isEmpty(colorDefault)) {
                if (colorDefault.length == 7) {
                    data.setValue(colorDefault.substring(1))
                } else if (colorDefault.length == 9) {
                    data.setValue(colorDefault.substring(3))
                    data.setAlpha(colorDefault.substring(1, 3))
                }
            }
        }
        return data
    }

    internal interface OnColorChangedListener {
        fun onColorChanged(data: ColorPickerData)
    }

    private inner class ColorPickerAdapter : BaseQuickAdapter<ColorPickerData?, BaseViewHolder>(R.layout.t_item_design_my_theme) {
        private var mOnColorChangedListener: OnColorChangedListener? = null
        fun setOnColorChangedListener(listener: OnColorChangedListener?) {
            mOnColorChangedListener = listener
        }

        private fun onColorChanged(item: ColorPickerData?) {
            mOnColorChangedListener!!.onColorChanged(item)
        }

        override fun convert(holder: BaseViewHolder, item: ColorPickerData?) {
            val color = Integer.valueOf(item!!.getValue(), 16)
            val alpha = Integer.valueOf(item.getAlpha(), 16)
            holder.setText(R.id.name, item.name)
            val mColorSeekBar = holder.getView<View>(R.id.color_seek_bar) as Slider
            mColorSeekBar.value = color.toFloat()
            val mAlphaSeekBar = holder.getView<View>(R.id.alpha_seek_bar) as Slider
            mAlphaSeekBar.value = alpha.toFloat()
            holder.setText(R.id.preview, item.color)
            holder.setTextColor(R.id.preview, Color.parseColor(item.color))
            holder.setBackgroundResource(R.id.preview, item.colorRes)
            mColorSeekBar.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
                val colorStr = Integer.toHexString(value.toInt())
                item.setValue(colorStr)
                holder.setText(R.id.preview, item.color)
                holder.setTextColor(R.id.preview, Color.parseColor(item.color))
                onColorChanged(item)
            })
            mAlphaSeekBar.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
                val alphaStr = Integer.toHexString(value.toInt())
                item.setAlpha(alphaStr)
                holder.setText(R.id.preview, item.color)
                holder.setTextColor(R.id.preview, Color.parseColor(item.color))
                onColorChanged(item)
            })
        }
    }

    class ColorPickerData {
        var name = ""
        private var value = "000000"
        private var alpha = "ff"
        var colorRes = 0
        fun setAlpha(alpha: String) {
            if (alpha.length == 2) {
                this.alpha = alpha
            } else if (alpha.length == 1) {
                this.alpha = "0$alpha"
            } else {
                this.alpha = "ff"
            }
        }

        fun getAlpha(): String {
            return alpha
        }

        fun setValue(value: String) {
            var value = value
            if (value.length == 6) {
                this.value = value
            } else if (value.length < 6) {
                val valueLen = value.length
                for (i in 0 until 6 - valueLen) {
                    value = "0$value"
                }
                this.value = value
            } else {
                this.value = "000000"
            }
        }

        fun getValue(): String {
            return value
        }

        val color: String
            get() = "#$alpha$value"
    }
}