package com.timecat.module.plugin.host;

import android.content.Context;

import com.tencent.shadow.dynamic.host.DynamicPluginManager;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.timecat.module.plugin.database.Plugin;
import com.timecat.plugin.api.record.RecordApi;
import com.timecat.plugin.api.record.holder.DynamicRecordApi;

import java.io.File;

public class Shadow {

    public static PluginManager getPluginManager(File apk) {
        final FixedPathPmUpdater fixedPathPmUpdater = new FixedPathPmUpdater(apk);
        File tempPm = fixedPathPmUpdater.getLatest();
        if (tempPm != null) {
            return new DynamicPluginManager(fixedPathPmUpdater);
        }
        return null;
    }

    public static PluginManager getPluginManager(Context context, Plugin plugin) {
        return getPluginManager(plugin.managerApkFile(context));
    }

    public static RecordApi getRecordApi(Context context, Plugin plugin) {
        return new DynamicRecordApi(context, plugin.managerApkFile(context));
    }

}
