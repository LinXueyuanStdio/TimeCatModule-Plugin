package com.timecat.module.skin;

import android.content.Intent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.commonsdk.utils.snap.GravitySnapHelper;
import com.timecat.component.router.app.NAV;
import com.timecat.data.system.model.api.Skin;
import com.timecat.data.system.model.api.SkinInfo;
import com.timecat.data.system.model.api.SkinSeries;
import com.timecat.data.system.network.RetrofitHelper;
import com.timecat.element.alert.ToastUtil;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.identity.skin.SkinManager;
import com.timecat.module.skin.data.Shelf;
import com.timecat.module.skin.info.SkinInfoBottomSheetDialog;
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity;
import com.xiaojinzi.component.anno.RouterAnno;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/16
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinActivity)
public class SkinActivity extends BaseRefreshListActivity  {
    ThemeAdapter<Shelf> themeAdapter;

    @Override
    protected void routerInject() {
        NAV.inject(this);
    }

    @NotNull
    @Override
    protected String title() {
        return "主题换肤";
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        themeAdapter = new ThemeAdapter<>();
        return themeAdapter;
    }

    @Override
    public void onRefresh() {
        RetrofitHelper.getSkinService().get("skin.json")
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Observer<Skin>() {
                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                        ToastUtil.e_long(e.getMessage());
                    }

                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NotNull Skin skin) {
                        List<Shelf> dataList = new ArrayList<>();
                        List<SkinInfo> skinInfos = new ArrayList<>();
                        skinInfos.add(new SkinInfo("恢复默认", "", "", "", "", "", "", "", ""));
                        skinInfos.add(new SkinInfo("本地皮肤", "", "", "", "", "", "", "", ""));
                        dataList.add(new Shelf(skinInfos, "官方", null, null, null));
                        for (SkinSeries s : skin.getData()) {
                            dataList.add(new Shelf(s.getData(), s.getName(), null, null, null));
                        }
                        themeAdapter.replaceData(dataList);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.current) {
            new SkinInfoBottomSheetDialog().showIfNeed(getSupportFragmentManager());
            return true;
        } else if (itemId == R.id.myTheme) {
            startActivity(new Intent(this, SkinDesignActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region 内部类
    public class ThemeAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


        public ThemeAdapter() {
            super(R.layout.t_adapter_theme);
        }


        @Override
        protected void convert(BaseViewHolder viewHolder, T item) {
            Shelf shelf = (Shelf) item;
            viewHolder.setText(R.id.name, shelf.name);

            ShelfListAdapter<?> shelfListAdapter = new ShelfListAdapter<>();
//            shelfListAdapter.openLoadAnimation();TODO
            RecyclerView recyclerView = viewHolder.getView(R.id.rv);
            recyclerView.setAdapter(shelfListAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(SkinActivity.this, 3));
            shelfListAdapter.replaceData(shelf.list);
            new GravitySnapHelper(Gravity.START, false).attachToRecyclerView(recyclerView);

            if (shelf.onMoreClickListener == null) {
                viewHolder.getView(R.id.more).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.more).setOnClickListener(shelf.onMoreClickListener);
            }
            if (shelf.onRefreshClickListener == null) {
                viewHolder.getView(R.id.refresh).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.refresh).setOnClickListener(shelf.onRefreshClickListener);
            }
            if (shelf.onAddClickListener == null) {
                viewHolder.getView(R.id.add).setVisibility(View.GONE);
            } else {
                viewHolder.getView(R.id.add).setOnClickListener(shelf.onAddClickListener);
            }
        }
    }

    public class ShelfListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


        public ShelfListAdapter() {
            super(R.layout.t_item_theme);
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, T item) {
            if (item instanceof SkinInfo) {
                SkinInfo site = (SkinInfo) item;
                String baseUrl = site.getSkin_url();
                Glide.with(SkinActivity.this).load(baseUrl + site.getSkin_avatar())
                     .apply(new RequestOptions().dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .centerCrop()
                                .placeholder(R.drawable.avatar_default))
                     .into(((ImageView) viewHolder.getView(R.id.imageView)));
                viewHolder.setText(R.id.title, site.getSkin_name());
                viewHolder.getView(R.id.itemView).setOnClickListener(v -> {
                    if ("恢复默认".equals(site.getSkin_name())) {
                        SkinManager.restoreDefaultTheme();
                    } else if ("本地皮肤".equals(site.getSkin_name())) {
                        SkinManager.loadSkin("local.apk", new SkinManager.SkinLoaderListener() {
                            @Override
                            public void onStart() {
                                LogUtil.se("onStart");
                            }

                            @Override
                            public void onSuccess() {
                                LogUtil.se("onSuccess");
                            }

                            @Override
                            public void onFailed(String errMsg) {
                                LogUtil.se(errMsg);
                            }
                        });
                    } else {
                        NAV.go(RouterHub.SKIN_SkinDetailActivity, "skinInfo", site);
                    }
                });
            }
        }
    }

    //endregion
}
