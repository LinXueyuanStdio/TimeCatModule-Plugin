package com.timecat.module.plugin.container;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;

import com.tencent.shadow.dynamic.host.PluginProcessService;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.plugin.shared.LoadPluginCallback;

public class SecondPluginProcessPPS extends PluginProcessService {
    public SecondPluginProcessPPS() {
        LoadPluginCallback.setCallback(new LoadPluginCallback.Callback() {

            @Override
            public void beforeLoadPlugin(String partKey) {
                LogUtil.d("beforeLoadPlugin(" + partKey + ")");
            }

            @Override
            public void afterLoadPlugin(String partKey, ApplicationInfo applicationInfo, ClassLoader pluginClassLoader, Resources pluginResources) {
                LogUtil.d("afterLoadPlugin(" + partKey + "," + applicationInfo.className + "{metaData=" + applicationInfo.metaData + "}" + "," + pluginClassLoader + ")");
            }
        });
    }
}
