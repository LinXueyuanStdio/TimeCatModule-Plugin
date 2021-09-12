package com.timecat.plugin.api.record;

import android.content.Context;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
public interface RecordApiFactory {
    RecordApiWrapper build(Context context);
}
