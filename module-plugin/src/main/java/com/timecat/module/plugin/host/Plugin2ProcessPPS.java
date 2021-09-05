package com.timecat.module.plugin.host;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.util.Log;

import com.tencent.shadow.dynamic.host.PluginProcessService;
import com.timecat.plugin.shared.LoadPluginCallback;

public class Plugin2ProcessPPS extends PluginProcessService {
    public Plugin2ProcessPPS() {
        LoadPluginCallback.setCallback(new LoadPluginCallback.Callback() {

            @Override
            public void beforeLoadPlugin(String partKey) {
                Log.d("Plugin2ProcessPPS", "beforeLoadPlugin(" + partKey + ")");
            }

            @Override
            public void afterLoadPlugin(String partKey, ApplicationInfo applicationInfo, ClassLoader pluginClassLoader, Resources pluginResources) {
                Log.d("Plugin2ProcessPPS", "afterLoadPlugin(" + partKey + "," + applicationInfo.className + "{metaData=" + applicationInfo.metaData + "}" + "," + pluginClassLoader + ")");
            }
        });
    }
}
