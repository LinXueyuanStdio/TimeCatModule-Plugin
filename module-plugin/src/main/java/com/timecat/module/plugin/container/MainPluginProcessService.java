package com.timecat.module.plugin.container;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;

import com.tencent.shadow.dynamic.host.PluginProcessService;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.plugin.shared.LoadPluginCallback;

/**
 * 一个PluginProcessService（简称PPS）代表一个插件进程。插件进程由PPS启动触发启动。
 * 新建PPS子类允许一个宿主中有多个互不影响的插件进程。
 */
public class MainPluginProcessService extends PluginProcessService {
    public MainPluginProcessService() {
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
