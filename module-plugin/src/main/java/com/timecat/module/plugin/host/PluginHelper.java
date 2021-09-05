package com.timecat.module.plugin.host;

import android.content.Context;

import com.timecat.component.commonsdk.utils.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PluginHelper {

    /**
     * 动态加载的插件管理apk
     */
    public final static String sPluginManagerName = "plugin-manager.apk";

    /**
     * 动态加载的插件包，里面包含以下几个部分，插件apk，插件框架apk（loader apk和runtime apk）, apk信息配置关系json文件
     */
    public final static String sPluginZip = "plugin-release.zip";

    public File pluginManagerFile;

    public File pluginZipFile;

    public ExecutorService singlePool = Executors.newSingleThreadExecutor();

    private Context mContext;

    private static PluginHelper sInstance = new PluginHelper();

    public static PluginHelper getInstance() {
        return sInstance;
    }

    private PluginHelper() {
    }

    public void init(Context context) {
        pluginManagerFile = new File(context.getFilesDir(), sPluginManagerName);
        pluginZipFile = new File(context.getFilesDir(), sPluginZip);

        mContext = context.getApplicationContext();

        singlePool.execute(new Runnable() {
            @Override
            public void run() {
                preparePlugin();
            }
        });

    }

    private void preparePlugin() {
        try {
            InputStream is = mContext.getAssets().open(sPluginManagerName);
            OutputStream out = new FileOutputStream(pluginManagerFile);
            FileUtil.copy(is, out);

            InputStream zip = mContext.getAssets().open(sPluginZip);
            OutputStream out2 = new FileOutputStream(pluginZipFile);
            FileUtil.copy(zip, out2);
        } catch (IOException e) {
            throw new RuntimeException("从assets中复制apk出错", e);
        }
    }
}
