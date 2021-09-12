package com.timecat.plugin.api.record;

import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
public interface RecordApiWrapper extends RecordApi {
    void onDynamicCreate(@NonNull Bundle bundle);
    void onDynamicSaveInstanceState(@NonNull Bundle outState);
    void onDynamicDestroy();
}
