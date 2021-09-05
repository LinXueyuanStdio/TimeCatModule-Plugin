package com.timecat.module.plugin.host;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.shadow.dynamic.host.EnterCallback;
import com.timecat.identity.readonly.PluginHub;
import com.timecat.module.plugin.R;


public class PluginLoadActivity extends Activity {

    private ViewGroup mViewGroup;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mViewGroup = findViewById(R.id.container);

        startPlugin();
    }


    public void startPlugin() {

        PluginHelper.getInstance().singlePool.execute(new Runnable() {
            @Override
            public void run() {
                HostApplication.getApp().loadPluginManager(PluginHelper.getInstance().pluginManagerFile);

                Bundle bundle = new Bundle();
                bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, PluginHelper.getInstance().pluginZipFile.getAbsolutePath());
                bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, getIntent().getStringExtra(PluginHub.KEY_PLUGIN_PART_KEY));
                bundle.putString(PluginHub.KEY_CLASSNAME, getIntent().getStringExtra(PluginHub.KEY_CLASSNAME));

                HostApplication.getApp().getPluginManager()
                               .enter(PluginLoadActivity.this, PluginHub.FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
                                   @Override
                                   public void onShowLoadingView(final View view) {
                                       mHandler.post(new Runnable() {
                                           @Override
                                           public void run() {
                                               mViewGroup.addView(view);
                                           }
                                       });
                                   }

                                   @Override
                                   public void onCloseLoadingView() {
                                       finish();
                                   }

                                   @Override
                                   public void onEnterComplete() {

                                   }
                               });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewGroup.removeAllViews();
    }
}
