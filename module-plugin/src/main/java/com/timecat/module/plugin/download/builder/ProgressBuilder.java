package com.timecat.module.plugin.download.builder;

import android.app.Notification;
import android.content.Context;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
public class ProgressBuilder extends BaseBuilder {
    public int max;
    public float progress;
    public boolean interminate = false;

    public ProgressBuilder(Context context) {
        super(context);
    }

    @Deprecated
    public ProgressBuilder setProgress(int max, int progress, boolean interminate){
        setProgressAndFormat(progress, interminate, "%d/%d");
        return this;
    }

    public ProgressBuilder setProgressAndFormat(float progress, boolean interminate, String format){
        this.max = 100;
        this.progress = progress;
        this.interminate = interminate;

        //contenttext的显示
        if(TextUtils.isEmpty(format) ){
            format = "进度:%.2f%%";
            setContentText(String.format(format, progress));
        }else {
            if(format.contains("%%")){//百分比类型
                int progressf = (int) (progress);
                setContentText(String.format(format, progressf));
            }else {
                setContentText(String.format(format, progress, 100));
            }
        }

        return this;
    }

    @Override
    public void build(NotificationCompat.Builder builder) {
        super.build(builder);
        builder.setProgress(max, (int) progress, interminate);

//        builder.setDefaults(0);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);
    }

    @Override
    public void build(Notification.Builder builder) {
        super.build(builder);
        builder.setProgress(max, (int) progress, interminate);
//        builder.setDefaults(0);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
    }
}

