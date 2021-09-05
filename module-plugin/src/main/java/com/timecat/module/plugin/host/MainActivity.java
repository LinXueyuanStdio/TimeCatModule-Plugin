package com.timecat.module.plugin.host;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.timecat.identity.readonly.PluginHub;
import com.timecat.module.plugin.PluginRouterActivity;
import com.timecat.module.plugin.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TestHostTheme);

        LinearLayout rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);

        TextView infoTextView = new TextView(this);
        infoTextView.setText(R.string.main_activity_info);
        rootView.addView(infoTextView);

        final Spinner partKeySpinner = new Spinner(this);
        ArrayAdapter<String> partKeysAdapter = new ArrayAdapter<>(this, R.layout.part_key_adapter);
        partKeysAdapter.addAll(
        "start"
        );
        partKeySpinner.setAdapter(partKeysAdapter);

        rootView.addView(partKeySpinner);

        Button startPluginButton = new Button(this);
        startPluginButton.setText(R.string.start_plugin);
        startPluginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partKey = (String) partKeySpinner.getSelectedItem();
                Intent intent = new Intent(MainActivity.this, PluginRouterActivity.class);
                intent.putExtra(PluginHub.KEY_PLUGIN_PART_KEY, partKey);
                intent.putExtra(PluginHub.KEY_CLASSNAME, "com.tencent.shadow.sample.plugin.app.lib.gallery.splash.SplashActivity");
                startActivity(intent);
            }
        });
        rootView.addView(startPluginButton);

        setContentView(rootView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        throw new RuntimeException("必须赋予权限.");
                    }
                }
            }
        }
    }

}
