package com.timecat.module.skin

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allSkin
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.skin.adapter.CloudSkinItem
import com.timecat.module.skin.database.SkinDatabase
import com.timecat.module.skin.info.SkinInfoBottomSheetDialog
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.zpj.downloader.ZDownloader
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件市场
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinCloudActivity)
class SkinCloudActivity : BaseRefreshListActivity() {
    override fun title(): String = "皮肤市场"
    val adapter = BaseAdapter(null)
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        mStatefulLayout?.showLoading("加载中，请耐心等待...")
        dispose?.dispose()
        dispose = requestBlock {
            query = allSkin()
            onSuccess = { blocks ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val missions = ZDownloader.getAllMissions()
                    val uuids = blocks.map { it.objectId }
                    val allSkins = SkinDatabase.forFile(context).skinDao().getAll(uuids)
                    val items = blocks.map { block ->
                        val localSkin = allSkins.find { it.uuid in uuids }
                        val mission = missions.find { localSkin?.apkFile(this@SkinCloudActivity)?.parent == it.downloadPath }
                        CloudSkinItem(context, block, mission, localSkin)
                    }
                    withContext(Dispatchers.Main) {
                        mRefreshLayout.isRefreshing = false
                        adapter.reload(items)
                        mStatefulLayout?.showContent()
                    }
                }
            }
            onEmpty = {
                mRefreshLayout.isRefreshing = false
                adapter.clear()
                mStatefulLayout?.showEmpty("啥也没有")
            }
            onError = {
                mRefreshLayout.isRefreshing = false
                adapter.clear()
                mStatefulLayout?.showError("出错\n${it.msg}") {
                    onRefresh()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.skin_theme, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.current) {
            SkinInfoBottomSheetDialog().showIfNeed(supportFragmentManager)
        } else if (item.itemId == R.id.myTheme) {
            NAV.go(RouterHub.SKIN_SkinDesignActivity)
        }
        return super.onOptionsItemSelected(item)
    }
}