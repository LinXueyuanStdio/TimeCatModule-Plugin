package com.timecat.module.plugin.core;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.identity.readonly.PluginHub;
import com.timecat.identity.service.PluginService;
import com.xiaojinzi.component.anno.ServiceAnno;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/12
 * @description null
 * @usage null
 */
@ServiceAnno(PluginService.class)
public class PluginServiceImpl implements PluginService {
    private Context context;

    public PluginServiceImpl(Context context) {
        this.context = context;
    }

    /**
     * 调用一个插件，如果插件没下载，就下载后再调用
     * @param input 传递给插件管理器的、关于插件的信息
     *              一方面，让管理器能根据这些信息调用到插件
     *              另一方面，传递对应的数据给插件作为输入
     * @param output 回调给当前宿主，说明下载、调用的状态，分失败，成功，调用中等等
     */
    @Override
    public void start(InputToPlugin input, OutputFromPlugin output) {

    }

    @Override
    public boolean existPlugin(String filename) {
        return true;
    }

    private Bundle getBundle(String zipAbsPathForPlugin,
            String partName,
            String activity_class_name,
            Bundle extra,
            Uri data,
            String action) {
        Bundle bundle = new Bundle();
        LogUtil.se(zipAbsPathForPlugin);
        LogUtil.se(partName);
        LogUtil.se(activity_class_name);
        bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, zipAbsPathForPlugin);
        bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partName);
        bundle.putString(PluginHub.KEY_CLASSNAME, activity_class_name);
        bundle.putBundle(PluginHub.KEY_EXTRAS, extra);
        bundle.putString(PluginHub.KEY_ACTION, action);
        bundle.putParcelable(PluginHub.KEY_DATA, data);
        return bundle;
    }

}
