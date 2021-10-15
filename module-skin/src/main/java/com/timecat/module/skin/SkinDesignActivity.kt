package com.timecat.module.skin

import android.text.TextUtils
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.skin.adapter.DesignItem
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.xiaojinzi.component.anno.RouterAnno
import skin.support.SkinCompatManager
import skin.support.content.res.SkinCompatUserThemeManager
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/30
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinDesignActivity)
class SkinDesignActivity : BaseRefreshListActivity() {
    private lateinit var mAdapter: BaseAdapter
    override fun title(): String = "我的设计"

    override fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder?> {
        mAdapter = BaseAdapter(null)
        return mAdapter
    }

    override fun onRefresh() {
        mAdapter.reload(prepareData())
        mRefreshLayout.isRefreshing = false
    }

    private fun prepareData(): List<DesignItem> {
        val dataList: MutableList<ColorPickerData> = ArrayList()
        dataList.add(generateData(R.color.master_colorPrimary))
        dataList.add(generateData(R.color.master_colorPrimaryDark))
        dataList.add(generateData(R.color.master_colorAccent))
        dataList.add(generateData(R.color.master_textColorPrimary))
        dataList.add(generateData(R.color.text_color))
        dataList.add(generateData(R.color.master_background))
        dataList.add(generateData(R.color.master_divider))
        return dataList.map {
            DesignItem(this, it, object : DesignItem.OnColorChangedListener {
                override fun onColorChanged(data: ColorPickerData) {
                    val color = data.color
                    SkinCompatUserThemeManager.get().addColorState(data.colorRes, color)
                    SkinCompatUserThemeManager.get().apply()
                    SkinCompatManager.getInstance().notifyUpdateSkin()
                }
            })
        }
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

}