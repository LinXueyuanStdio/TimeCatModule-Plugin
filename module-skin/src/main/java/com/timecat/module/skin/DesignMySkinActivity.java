package com.timecat.module.skin;

import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.slider.Slider;
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import skin.support.SkinCompatManager;
import skin.support.content.res.ColorState;
import skin.support.content.res.SkinCompatUserThemeManager;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/30
 * @description null
 * @usage null
 */
public class DesignMySkinActivity extends BaseRefreshListActivity {
    private ColorPickerAdapter mAdapter;

    @NotNull
    @Override
    protected String title() {
        return "我的设计";
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        mAdapter = new ColorPickerAdapter();
        mAdapter.setOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(ColorPickerData data) {
                String color = data.getColor();
                SkinCompatUserThemeManager.get().addColorState(data.colorRes, color);
                SkinCompatUserThemeManager.get().apply();
                SkinCompatManager.getInstance().notifyUpdateSkin();
            }
        });
        return mAdapter;
    }

    @Override
    public void onRefresh() {
        mAdapter.replaceData(prepareData());
        mRefreshLayout.setRefreshing(false);
    }

    private List<ColorPickerData> prepareData() {
        List<ColorPickerData> dataList = new ArrayList<>();
        dataList.add(generateData(R.color.master_colorPrimary));
        dataList.add(generateData(R.color.master_colorPrimaryDark));
        dataList.add(generateData(R.color.master_colorAccent));
        dataList.add(generateData(R.color.master_textColorPrimary));
        dataList.add(generateData(R.color.text_color));
        dataList.add(generateData(R.color.master_background));
        dataList.add(generateData(R.color.master_divider));
        return dataList;
    }

    private ColorPickerData generateData(@ColorRes int colorRes) {
        ColorState state = SkinCompatUserThemeManager.get().getColorState(colorRes);
        ColorPickerData data = new ColorPickerData();
        data.colorRes = colorRes;
        if (state == null) {
            data.name = getResources().getResourceEntryName(colorRes);
        } else {
            data.name = state.getColorName();
            String colorDefault = state.getColorDefault();
            if (!TextUtils.isEmpty(colorDefault)) {
                if (colorDefault.length() == 7) {
                    data.setValue(colorDefault.substring(1));
                } else if (colorDefault.length() == 9) {
                    data.setValue(colorDefault.substring(3));
                    data.setAlpha(colorDefault.substring(1, 3));
                }
            }
        }
        return data;
    }

    interface OnColorChangedListener {
        void onColorChanged(ColorPickerData data);
    }

    private class ColorPickerAdapter extends BaseQuickAdapter<ColorPickerData, BaseViewHolder> {
        private OnColorChangedListener mOnColorChangedListener;

        public ColorPickerAdapter() {
            super(R.layout.t_item_design_my_theme);
        }

        public void setOnColorChangedListener(OnColorChangedListener listener) {
            mOnColorChangedListener = listener;
        }

        private void onColorChanged(ColorPickerData item) {
            mOnColorChangedListener.onColorChanged(item);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, @Nullable ColorPickerData item) {
            int color = Integer.valueOf(item.getValue(), 16);
            int alpha = Integer.valueOf(item.getAlpha(), 16);
            holder.setText(R.id.name, item.name);
            Slider mColorSeekBar = ((Slider) holder.getView(R.id.color_seek_bar));
            mColorSeekBar.setValue(color);
            Slider mAlphaSeekBar = ((Slider) holder.getView(R.id.alpha_seek_bar));
            mAlphaSeekBar.setValue(alpha);
            holder.setText(R.id.preview, item.getColor());
            holder.setTextColor(R.id.preview, Color.parseColor(item.getColor()));
            holder.setBackgroundResource(R.id.preview, item.colorRes);

            mColorSeekBar.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    String colorStr = Integer.toHexString((int) value);
                    item.setValue(colorStr);
                    holder.setText(R.id.preview, item.getColor());
                    holder.setTextColor(R.id.preview, Color.parseColor(item.getColor()));
                    onColorChanged(item);
                }
            });
            mAlphaSeekBar.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    String alphaStr = Integer.toHexString((int) value);
                    item.setAlpha(alphaStr);
                    holder.setText(R.id.preview, item.getColor());
                    holder.setTextColor(R.id.preview, Color.parseColor(item.getColor()));
                    onColorChanged(item);
                }
            });
        }
    }

    private static class ColorPickerData {
        private String name = "";
        private String value = "000000";
        private String alpha = "ff";
        private int colorRes;

        public void setAlpha(String alpha) {
            if (alpha.length() == 2) {
                this.alpha = alpha;
            } else if (alpha.length() == 1) {
                this.alpha = "0" + alpha;
            } else {
                this.alpha = "ff";
            }
        }

        public String getAlpha() {
            return alpha;
        }

        public void setValue(String value) {
            if (value.length() == 6) {
                this.value = value;
            } else if (value.length() < 6) {
                int valueLen = value.length();
                for (int i = 0; i < 6 - valueLen; i++) {
                    value = "0" + value;
                }
                this.value = value;
            } else {
                this.value = "000000";
            }
        }

        public String getValue() {
            return value;
        }

        public String getColor() {
            return "#" + alpha + value;
        }
    }

}
