package com.timecat.middle.download.builder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.timecat.middle.download.ZNotify;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/12
 * @description null
 * @usage null
 */
public class BaseBuilder {


    private final Context context;
    private final NotificationManager notificationManager;

    //最基本的ui
    private int smallIcon = android.R.mipmap.sym_def_app_icon;
    private CharSequence contentTitle;
    private CharSequence contentText;

    private boolean headup;
    private CharSequence summaryText;

    //最基本的控制管理
    private int id = 0;

    private int bigIcon;
    private CharSequence ticker = "";

    public CharSequence subText;
    public int flag = NotificationCompat.FLAG_AUTO_CANCEL;
    public int priority = Notification.PRIORITY_DEFAULT;

    public Uri soundUri;
    public long[] vibratePatten;
    public int rgb;
    public int onMs;
    public int offMs;

    public int defaults = Notification.DEFAULT_ALL;//默认只有走马灯提醒
    public boolean sound = false;
    public boolean vibrate = false;
    public boolean lights = false;

    public BaseBuilder setLockScreenVisiablity(int lockScreenVisiablity) {
        this.lockScreenVisiablity = lockScreenVisiablity;
        return this;
    }

    public int lockScreenVisiablity = NotificationCompat.VISIBILITY_SECRET;

    public long when;
    //事件
    public PendingIntent contentIntent;
    public PendingIntent deleteIntent;
    public PendingIntent fullscreenIntent;

    public boolean onGoing = false;

    public BaseBuilder(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public BaseBuilder setOnGoing() {
        this.onGoing = true;
        return this;
    }

    public BaseBuilder setForgroundService(boolean is) {
        this.forgroundService = is;
        this.onGoing = is;
        return this;
    }

    public boolean forgroundService = false;

    //带按钮的

    public List<BtnActionBean> btnActionBeens;

    public BaseBuilder addBtn(int icon, CharSequence text, PendingIntent pendingIntent) {
        if (btnActionBeens == null) {
            btnActionBeens = new ArrayList<>();
        }
        if (btnActionBeens.size() > 5) {
            throw new RuntimeException("5 buttons at most!");
        }
        btnActionBeens.add(new BtnActionBean(icon, text, pendingIntent));

        return this;
    }

    public static class BtnActionBean {
        public int icon;
        public CharSequence text;
        public PendingIntent pendingIntent;

        public BtnActionBean(int icon, CharSequence text, PendingIntent pendingIntent) {
            this.icon = icon;
            this.text = text;
            this.pendingIntent = pendingIntent;
        }
    }


    public BaseBuilder setBase(int icon, CharSequence contentTitle, CharSequence contentText) {
        this.smallIcon = icon;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        return this;
    }

    public BaseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public BaseBuilder setContentTitle(CharSequence contentTitle) {
        this.contentTitle = contentTitle;
        return this;
    }

    public BaseBuilder setSummaryText(CharSequence summaryText) {
        this.summaryText = summaryText;
        return this;
    }

    public BaseBuilder setContentText(CharSequence contentText) {
        this.contentText = contentText;
        return this;
    }

    public BaseBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public BaseBuilder setContentIntent(PendingIntent contentIntent) {
        this.contentIntent = contentIntent;
        return this;
    }

    public BaseBuilder setDeleteIntent(PendingIntent deleteIntent) {
        this.deleteIntent = deleteIntent;
        return this;
    }

    //todo
    public BaseBuilder setFullScreenIntent(PendingIntent fullscreenIntent) {
        this.fullscreenIntent = fullscreenIntent;
        return this;
    }

    public BaseBuilder setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public BaseBuilder setBigIcon(int bigIcon) {
        this.bigIcon = bigIcon;
        return this;
    }

    public BaseBuilder setHeadup() {
        this.headup = true;
        return this;
    }

    public BaseBuilder setTicker(CharSequence ticker) {
        this.ticker = ticker;
        return this;
    }

    public BaseBuilder setSubtext(CharSequence subText) {
        this.subText = subText;
        return this;
    }

    public BaseBuilder setAction(boolean sound, boolean vibrate, boolean lights) {
        this.sound = sound;
        this.vibrate = vibrate;
        this.lights = lights;
        return this;
    }

    private NotificationCompat.Builder getNotificationCompat() {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, ZNotify.CHANNEL_ID);
        } else {
            //注意用下面这个方法，在8.0以上无法出现通知栏。8.0之前是正常的。这里需要增强判断逻辑
            builder = new NotificationCompat.Builder(context);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
//        cBuilder = new NotificationCompat.Builder(NotifyUtil.context);
        builder.setContentIntent(contentIntent);// 该通知要启动的Intent

        if (smallIcon > 0) {
            builder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        }
        if (bigIcon > 0) {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), bigIcon));
        }

        builder.setTicker(ticker);// 在顶部状态栏中的提示信息

        builder.setContentTitle(contentTitle);// 设置通知中心的标题
        if (!TextUtils.isEmpty(contentText)) {
            builder.setContentText(contentText);// 设置通知中心中的内容
        }

        builder.setWhen(System.currentTimeMillis());
        //cBuilder.setStyle()

        /*
         * 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失,
         * 不设置的话点击消息后也不清除，但可以滑动删除
         */
        builder.setAutoCancel(true);

        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
         * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
         */
        builder.setPriority(priority);

        builder.setWhen(System.currentTimeMillis());

        //int defaults = 0;

        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }
        builder.setDefaults(defaults);


        //按钮
        if (btnActionBeens != null && btnActionBeens.size() > 0) {
            for (BtnActionBean bean : btnActionBeens) {
                builder.addAction(bean.icon, bean.text, bean.pendingIntent);
            }
        }

        //headup
        if (headup) {
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
//            cBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//            cBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        if (TextUtils.isEmpty(ticker)) {
            builder.setTicker("你有新的消息");
        }
        builder.setOngoing(onGoing);
        builder.setFullScreenIntent(fullscreenIntent, true);
        builder.setVisibility(lockScreenVisiablity);
        builder.setOnlyAlertOnce(true);
        build(builder);
        return builder;
    }

    public void build(NotificationCompat.Builder builder) {

    }

    public void build(Notification.Builder builder) {

    }

    public Notification getNotification() {
        Notification build;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            //通知用到NotificationCompat()这个V4库中的方法。但是在实际使用时发现书上的代码已经过时并且Android8.0已经不支持这种写法
            Notification.Builder builder = getChannelNotification();
            build = builder.build();
        } else {
            NotificationCompat.Builder builder = getNotificationCompat();
            build = builder.build();
        }
//        if (flags!=null && flags.length>0){
//            for (int a=0 ; a<flags.length ; a++){
//                build.flags |= flags[a];
//            }
//        }
        return build;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification.Builder getChannelNotification() {

        if (notificationManager.getNotificationChannel(id + "") == null) {
            //android 8.0以上需要特殊处理，也就是targetSDKVersion为26以上
            NotificationChannel channel = new NotificationChannel(id + "", context.getPackageName(),
                    NotificationManager.IMPORTANCE_DEFAULT);
            //是否绕过请勿打扰模式
//            channel.canBypassDnd();
            //闪光灯
            channel.enableLights(false);
            //锁屏显示通知
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            //闪关灯的灯光颜色
//            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            //是否允许震动
            channel.enableVibration(false);
            //设置可绕过 请勿打扰模式
            channel.setBypassDnd(false);
            channel.setSound(null, null);
            channel.setVibrationPattern(new long[]{0});
            notificationManager.createNotificationChannel(channel);
        }


        Notification.Builder builder = new Notification.Builder(context, id + "");
//        Log.d("contentTitle", "contentTitle=" + contentTitle);
        Notification.Builder notificationBuilder = builder
                //设置标题
                .setContentTitle(contentTitle)
                //消息内容
                .setContentText(contentText)
                //设置通知的图标
                .setSmallIcon(smallIcon)
                //让通知左右滑的时候是否可以取消通知
                .setOngoing(onGoing)
                //设置优先级
                .setPriority(priority)
                //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        if (smallIcon > 0) {
            notificationBuilder.setSmallIcon(smallIcon);
        }
//        if (remoteViews!=null){
//            //设置自定义view通知栏
//            notificationBuilder.setContent(remoteViews);
//        }
        if (contentIntent != null) {
            notificationBuilder.setContentIntent(contentIntent);
        }
        if (!TextUtils.isEmpty(ticker)) {
            //设置状态栏的标题
            notificationBuilder.setTicker(ticker);
        }
        if (when != 0) {
            //设置通知时间，默认为系统发出通知的时间，通常不用设置
            notificationBuilder.setWhen(when);
        }
//        //设置sound
//        notificationBuilder.setSound(sound);

        if (defaults != 0) {
            //设置默认的提示音
            notificationBuilder.setDefaults(defaults);
        }
//        if (pattern!=null){
//            //自定义震动效果
//            notificationBuilder.setVibrate(pattern);
//        }
        build(notificationBuilder);
        return notificationBuilder;
    }

    public void show() {
        Notification notification = getNotification();
        if(onGoing){
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        } else {
            notification.flags = Notification.FLAG_AUTO_CANCEL;
        }
//        notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
        notificationManager.notify(id, notification);
    }


}

