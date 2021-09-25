package com.timecat.module.skin.data;

import android.view.View;

import java.util.List;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/7/31
 * @description null
 * @usage null
 */
public class Shelf {
    public List list;
    public String name;

    public Shelf(List list, String name, View.OnClickListener onMoreClickListener) {
        this.list = list;
        this.name = name;
        this.onMoreClickListener = onMoreClickListener;
    }

    public Shelf(List list, String name, View.OnClickListener onMoreClickListener, View.OnClickListener onAddClickListener) {
        this.list = list;
        this.name = name;
        this.onMoreClickListener = onMoreClickListener;
        this.onAddClickListener = onAddClickListener;
    }
    public Shelf(List list, String name, View.OnClickListener onMoreClickListener,
                 View.OnClickListener onAddClickListener, View.OnClickListener onRefreshClickListener) {
        this.list = list;
        this.name = name;
        this.onMoreClickListener = onMoreClickListener;
        this.onAddClickListener = onAddClickListener;
        this.onRefreshClickListener = onRefreshClickListener;
    }

    public View.OnClickListener onMoreClickListener;
    public View.OnClickListener onRefreshClickListener;
    public View.OnClickListener onAddClickListener;

    public Shelf(List list, String name) {
        this.list = list;
        this.name = name;
    }
}
