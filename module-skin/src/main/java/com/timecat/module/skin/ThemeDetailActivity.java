package com.timecat.module.skin;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.router.app.NAV;
import com.timecat.data.system.model.api.SkinInfo;
import com.timecat.data.system.network.RetrofitHelper;
import com.timecat.element.alert.ToastUtil;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.identity.skin.CustomSDCardLoader;
import com.timecat.module.skin.data.DownloadListener;
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity;
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno;
import com.xiaojinzi.component.anno.RouterAnno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import skin.support.SkinCompatManager;
import skin.support.annotation.Skinable;
import skin.support.utils.SkinFileUtils;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/16
 * @description null
 * @usage null
 */
@Skinable
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinDetailActivity)
public class ThemeDetailActivity extends BaseToolbarActivity {

    @AttrValueAutowiredAnno("skinInfo")
    SkinInfo skinInfo;
    TextView download;
    ProgressBar event_progress;

    @Override
    protected int layout() {
        return R.layout.t_theme_detail_activity;
    }

    @Override
    protected void routerInject() {
        NAV.inject(this);
    }

    @NonNull
    @Override
    protected String title() {
        if (skinInfo == null) return "主题";
        return skinInfo.getSkin_name();
    }

    @Override
    protected void initView() {
        LogUtil.e(skinInfo);
        event_progress = findViewById(R.id.event_progress);
        event_progress.setMax(100);
        if (skinInfo != null) {
            //intro 优先开始加载图片
            String baseUrl = skinInfo.getSkin_url();
            Glide.with(this).load(baseUrl + skinInfo.getSkin_avatar())
                    .apply(new RequestOptions().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerInside()
                            .placeholder(R.drawable.t_window_bg))
                    .into((ImageView) findViewById(R.id.avatar));
            Glide.with(this).load(baseUrl + skinInfo.getSkin_preview_0())
                    .apply(new RequestOptions().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerInside()
                            .placeholder(R.drawable.t_window_bg))
                    .into((ImageView) findViewById(R.id.preview_0));
            Glide.with(this).load(baseUrl + skinInfo.getSkin_preview_1())
                    .apply(new RequestOptions().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerInside()
                            .placeholder(R.drawable.t_window_bg))
                    .into((ImageView) findViewById(R.id.preview_1));
            Glide.with(this).load(baseUrl + skinInfo.getSkin_preview_2())
                    .apply(new RequestOptions().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerInside()
                            .placeholder(R.drawable.t_window_bg))
                    .into((ImageView) findViewById(R.id.preview_2));
            //intro
            TextView intro = findViewById(R.id.intro);
            String text = skinInfo.getSkin_filename() + "(" + skinInfo.getSkin_size() + ")\n" +
                    skinInfo.getSkin_intro();
            intro.setText(text);

            download = findViewById(R.id.download);
            String skinFileName = skinInfo.getSkin_filename();
            checkDownloadButtonStatus(skinFileName);
            download.setEnabled(true);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download.setEnabled(false);
                    if (existSkin(skinFileName)) {
                        if (isUsingSkin(skinFileName)) {
                            SkinCompatManager.getInstance().restoreDefaultTheme();
                            download.setEnabled(true);
                        } else {
                            loadSkin(skinFileName);
                            download.setEnabled(true);
                        }
                    } else {
                        downloadAndLoadSkin(baseUrl, skinFileName);
                    }
                }
            });
        }
    }

    Thread mThread;

    public void downloadFile(String url, String fileAbspath, final DownloadListener downloadListener) {
        File mFile = new File(fileAbspath);
        if (!FileUtils.isFileExists(mFile) && FileUtils.createOrExistsFile(mFile)) {
            RetrofitHelper.getSkinService().downloadFile(url)
                    .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                            downloadListener.onFailure(); //下载失败
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ResponseBody response) {
                            //下载文件放在子线程
                            mThread = new Thread(() -> writeFile2Disk(response, mFile, downloadListener));
                            mThread.start();
                        }
                    });
        } else {
            downloadListener.onFinish(fileAbspath); //下载完成
        }
    }

    //将下载的文件写入本地存储
    private void writeFile2Disk(ResponseBody response, File file, DownloadListener downloadListener) {
        downloadListener.onStart();

        if (response == null) {
            downloadListener.onFailure();
            return;
        }

        long currentLength = 0;
        OutputStream os = null;

        InputStream is = response.byteStream(); //获取下载输入流
        long totalLength = response.contentLength();

        try {
            os = new FileOutputStream(file); //输出流
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                //计算当前下载百分比，并经由回调传出
                downloadListener.onProgress((int) (100 * currentLength / totalLength));
                //当百分比为100时下载结束，调用结束回调，并传出下载后的本地路径
                if ((int) (100 * currentLength / totalLength) == 100) {
                    downloadListener.onFinish(file.getAbsolutePath()); //下载完成
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                is.close(); //关闭输入流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadAndLoadSkin(String url, String skinFileName) {
        checkDownloadButtonStatus(skinFileName);
        LogUtil.e(url + skinFileName);
        String skinPkgPath = CustomSDCardLoader.getSkinAbsPath(this, skinFileName);
        downloadFile(url + skinFileName, skinPkgPath, new DownloadListener() {
            @Override
            public void onStart() {
                if (download != null) {
                    download.post(() -> download.setText("下载中..."));
                }
            }

            @Override
            public void onProgress(int currentLength) {
                if (download != null) {
                    download.post(() -> event_progress.setProgress(currentLength));
                }
            }

            @Override
            public void onFinish(String localPath) {
                if (download != null) {
                    download.post(() -> {
                        loadSkin(skinFileName);
                        download.setEnabled(true);
                    });
                }
            }

            @Override
            public void onFailure() {
                if (download != null) {
                    download.post(() -> {
                        download.setEnabled(true);
                        ToastUtil.e("下载失败！");
                    });
                }
            }
        });

    }

    private void checkDownloadButtonStatus(String skinFileName) {
        if (existSkin(skinFileName)) {
            if (isUsingSkin(skinFileName)) {
                download.setText("使用中");
            } else {
                download.setText("应用");
            }
        } else {
            download.setText("下载");
        }
    }

    private boolean existSkin(String skinFileName) {
        String skinPkgPath = CustomSDCardLoader.getSkinAbsPath(this, skinFileName);
        return SkinFileUtils.isFileExists(skinPkgPath);
    }

    private boolean isUsingSkin(String skinFileName) {
        String curSkinName = SkinCompatManager.getInstance().getCurSkinName();
        return curSkinName != null && curSkinName.equalsIgnoreCase(skinFileName);
    }

    private void loadSkin(String skinFileName) {
        SkinCompatManager.getInstance().loadSkin(skinFileName, new SkinCompatManager.SkinLoaderListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                ToastUtil.ok("喵");
                checkDownloadButtonStatus(skinFileName);
            }

            @Override
            public void onFailed(String errMsg) {
                ToastUtil.e(errMsg);
                String skinPkgPath = CustomSDCardLoader.getSkinAbsPath(ThemeDetailActivity.this, skinFileName);
                File file = new File(skinPkgPath);
                if (file.exists() && file.delete()) {
                    checkDownloadButtonStatus(skinFileName);
                } else {
                    checkDownloadButtonStatus(skinFileName);
                }
            }
        }, CustomSDCardLoader.SKIN_LOADER_STRATEGY_SDCARD);
    }
}
